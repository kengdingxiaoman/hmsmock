package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.KeyLength;
import cn.hydralisk.hmsmock.constants.enums.KeyType;
import cn.hydralisk.hmsmock.constants.enums.ZoneMasterKey;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

public class CommandGenerateKey extends CommonCommand {

	private static final int KEY_LENGTH_INDEX = 2;

	private static final int KEY_TYPE_INDEX = 3;

	private static final int ZMK_INDEX_BEGIN_INDEX = 4;

	private static final int ZMK_INDEX_END_INDEX = 5;

	private static final int TMK_UNDER_LMK_LENGTH_INDEX = 6;

	@Override
	public byte[] execute(byte[] requestData) {
		ByteArrayBuffer command = new ByteArrayBuffer();
		command.append(requestData);

		byte keyType = extractKeyType(command);

		if (isGenerateTmkey(keyType)) {
			return generateTmKey(command);
		} else {
			return generateWorkKey(command);
		}
	}

	private byte[] generateTmKey(ByteArrayBuffer command) {
		KeyLength keyLength = extractKeyLength(command);
		String randomTmKey = CommonUtils.genRandomKey(keyLength.getLength());

		CommonUtils.println(CommonUtils.byte2hexHexSytle(CommonUtils
				.hex2byte(randomTmKey)));

		byte[] tmkUnderLmk = ThreeDesUtils.encryptKey(
				CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY),
				CommonUtils.hex2byte(randomTmKey));

		byte[] tmkUnderZmk = ThreeDesUtils.encryptKey(
				CommonUtils.hex2byte(obtainZmk(command)),
				CommonUtils.hex2byte(randomTmKey));

		byte[] tmkKcv = ThreeDesUtils.encryptKey(
				CommonUtils.hex2byte(randomTmKey),
				ThreeDesUtils.KCV_CHECK_VALUE);

		ByteArrayBuffer responseData = new ByteArrayBuffer();
		responseData.append(keyLength.getLengthByteStyle());
		responseData.append(tmkUnderLmk);
		responseData.append(tmkUnderZmk);
		responseData.append(tmkKcv);
		return responseData.toByteArray();
	}

	private byte extractKeyType(ByteArrayBuffer command) {
		if (KEY_TYPE_INDEX >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_TYPE);
		}
		byte keyType = command.subArray(KEY_TYPE_INDEX);
		if (!KeyType.isSupportKeyType(keyType)) {
			throw new CommonException(RespErrorByte.UNSUPPORT_KEY_TYPE);
		}
		return keyType;
	}

	private KeyLength extractKeyLength(ByteArrayBuffer command) {
		if (KEY_LENGTH_INDEX >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_LENGTH);
		}
		byte keyLength = command.subArray(KEY_LENGTH_INDEX);
		if (!KeyLength.isSupportKeyLength(keyLength)) {
			throw new CommonException(RespErrorByte.UNSUPPORT_KEY_LENGTH);
		}
		return KeyLength.getMatchedKeyLength(keyLength);
	}

	private String obtainZmk(ByteArrayBuffer command) {
		if (ZMK_INDEX_END_INDEX >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_ZMK_INDEX);
		}
		byte[] zmkIndex = command.subArray(ZMK_INDEX_BEGIN_INDEX,
				ZMK_INDEX_END_INDEX);
		if (!ZoneMasterKey.isExistentZmk(zmkIndex)) {
			throw new CommonException(RespErrorByte.UNSUPPORT_ZMK_INDEX);
		}
		return ZoneMasterKey.getMatchedZoneMasterKey(zmkIndex).getValue();
	}

	private byte[] generateWorkKey(ByteArrayBuffer command) {
		KeyLength workKeyLength = extractKeyLength(command);
		String randomWorkKey = CommonUtils.genRandomKey(workKeyLength
				.getLength());

		CommonUtils.println(CommonUtils.byte2hexHexSytle(CommonUtils
				.hex2byte(randomWorkKey)));

		confirmNoNeedUseZmk(command);
		confirmTmkLengthMatchedWorkKeyLength(command, workKeyLength);

		byte[] tmkUnderLmk = extractTmkUnderLmk(command, workKeyLength);

		byte[] workKeyUnderLmk = ThreeDesUtils.encryptKey(
				CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY),
				CommonUtils.hex2byte(randomWorkKey));

		byte[] tmkPlaintext = ThreeDesUtils.decryptKey(
				CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY),
				tmkUnderLmk);

		byte[] workKeyUnderTmk = ThreeDesUtils.encryptKey(tmkPlaintext,
				CommonUtils.hex2byte(randomWorkKey));

		byte[] keyKcv = ThreeDesUtils.encryptKey(
				CommonUtils.hex2byte(randomWorkKey),
				ThreeDesUtils.KCV_CHECK_VALUE);

		ByteArrayBuffer responseData = new ByteArrayBuffer();
		responseData.append(workKeyLength.getLengthByteStyle());
		responseData.append(workKeyUnderLmk);
		responseData.append(workKeyUnderTmk);
		responseData.append(keyKcv);
		return responseData.toByteArray();
	}

	private void confirmNoNeedUseZmk(ByteArrayBuffer command) {
		if (ZMK_INDEX_END_INDEX >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_ZMK_INDEX);
		}
		byte[] zmkIndex = command.subArray(ZMK_INDEX_BEGIN_INDEX,
				ZMK_INDEX_END_INDEX);
		if (!CommonUtils.isEquals(zmkIndex, HmsConstants.NOT_USE_ZMK_MARK)) {
			throw new CommonException(RespErrorByte.NOT_USE_NO_NEED_ZMK_MARK);
		}
	}

	private void confirmTmkLengthMatchedWorkKeyLength(ByteArrayBuffer command,
			KeyLength workKeyLength) {
		if (TMK_UNDER_LMK_LENGTH_INDEX >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_TMK_LENGTH);
		}
		byte tmkLength = command.subArray(TMK_UNDER_LMK_LENGTH_INDEX);
		if (tmkLength != workKeyLength.getLengthByteStyle()) {
			throw new CommonException(
					RespErrorByte.TMK_LENGTH_NOT_MATCHED_WORK_LENGTH);
		}
	}

	private byte[] extractTmkUnderLmk(ByteArrayBuffer command,
			KeyLength workKeyLength) {
		int tmkUnderLmkBeginIndex = TMK_UNDER_LMK_LENGTH_INDEX + 1;
		int tmkUnderLmkEndIndex = TMK_UNDER_LMK_LENGTH_INDEX
				+ workKeyLength.getLength();

		if (tmkUnderLmkEndIndex >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_TMK_UNDER_LMK);
		}
		return command.subArray(tmkUnderLmkBeginIndex, tmkUnderLmkEndIndex);
	}

	private boolean isGenerateTmkey(byte keyType) {
		return KeyType.TMK.getType() == keyType;
	}
}
