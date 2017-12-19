package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.enums.ZoneMasterKey;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

public class CommandDecryptKeyUnderZmk extends CommonCommand {

	private static final int CIPHERTEXT_BEGIN_INDEX = 1;

	private static final int CIPHERTEXT_END_INDEX = 8;

	private static final int ZMK_INDEX_BEGIN_INDEX = 9;

	private static final int ZMK_INDEX_END_INDEX = 10;

	@Override
	public byte[] execute(byte[] requestData) {
		ByteArrayBuffer command = new ByteArrayBuffer();
		command.append(requestData);

		byte[] ciphertext = extractCiphertext(command);
		String zmk = obtainZmk(command);

		byte[] plaintext = ThreeDesUtils.decryptKey(CommonUtils.hex2byte(zmk),
				ciphertext);
		ByteArrayBuffer responseData = new ByteArrayBuffer();
		responseData.append(plaintext);
		return responseData.toByteArray();
	}

	private byte[] extractCiphertext(ByteArrayBuffer command) {
		if (CIPHERTEXT_END_INDEX >= command.length()) {
			throw new CommonException(RespErrorByte.ABSENCE_OF_CIPHERTEXT);
		}
		byte[] ciphertext = command.subArray(CIPHERTEXT_BEGIN_INDEX,
				CIPHERTEXT_END_INDEX);
		return ciphertext;
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
}
