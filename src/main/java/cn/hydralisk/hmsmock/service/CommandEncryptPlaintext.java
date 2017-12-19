package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.EncryptionType;
import cn.hydralisk.hmsmock.constants.enums.KeyLength;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 对明文进行加密
 * @author master.yang
 * @version $Id: CommandEncryptPlaintext.java, v 0.1 2014-7-7 上午10:37:08 master.yang Exp $
 */
public class CommandEncryptPlaintext extends CommonCommand {

    private static final int ENCRYPTION_TYPE_INDEX = 2;

    private static final int KEY_BEGIN_INDEX = 3;

    /** 
     * @see cn.hydralisk.hmsmock.service.CommonCommand#execute(byte[])
     */
    @Override
    public byte[] execute(byte[] requestData) {
        ByteArrayBuffer command = new ByteArrayBuffer();
        command.append(requestData);

        confirmSupportEncryptionType(command);

        KeyLength keyLength = confirmKeyLength(command);
        byte[] keyUnderLmk = extractKeyUnderLmk(command, keyLength);
        byte[] plaintext = extractPlaintext(command, keyLength);

        byte[] keyPlaintext = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), keyUnderLmk);
        byte[] ciphertext = ThreeDesUtils.encryptKey(keyPlaintext, plaintext);

        ByteArrayBuffer responseData = new ByteArrayBuffer();
        responseData.append(CommonUtils.convert2DataHexLength(plaintext.length));
        responseData.append(ciphertext);
        return responseData.toByteArray();
    }

    private void confirmSupportEncryptionType(ByteArrayBuffer command) {
        if (ENCRYPTION_TYPE_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_ENCRYPTION_TYPE);
        }
        byte encryptionType = command.subArray(ENCRYPTION_TYPE_INDEX);
        if (!EncryptionType.isSupportEncryptionType(encryptionType)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_ENCRYPTION_TYPE);
        }
    }

    private KeyLength confirmKeyLength(ByteArrayBuffer command) {
        KeyLength[] keyLengths = KeyLength.values();
        for (KeyLength keyLength : keyLengths) {
            if (isCommandLengthMatched(command, keyLength)) {
                return keyLength;
            }
        }
        throw new CommonException(RespErrorByte.UNSUPPORT_KEY_LENGTH);
    }

    private boolean isCommandLengthMatched(ByteArrayBuffer command, KeyLength keyLength) {
        int plaintextLengthBeginIndex = KEY_BEGIN_INDEX + keyLength.getLength();
        int plaintextLengthEndIndex = plaintextLengthBeginIndex + 1;
        int plaintextBeginIndex = plaintextLengthEndIndex + 1;

        if (plaintextLengthEndIndex >= command.length()) {
            return false;
        }

        byte[] _dataLength = command.subArray(plaintextLengthBeginIndex, plaintextLengthEndIndex);
        int plaintextLength = CommonUtils.convert2DecimalLength(_dataLength);
        int plaintextEndIndex = plaintextBeginIndex + plaintextLength - 1;

        if (plaintextEndIndex == command.length() - 1) {
            return true;
        }
        return false;
    }

    private byte[] extractKeyUnderLmk(ByteArrayBuffer command, KeyLength keyLength) {
        int keyEndIndex = KEY_BEGIN_INDEX + keyLength.getLength() - 1;
        return command.subArray(KEY_BEGIN_INDEX, keyEndIndex);
    }

    private byte[] extractPlaintext(ByteArrayBuffer command, KeyLength keyLength) {
        int plaintextLengthBeginIndex = KEY_BEGIN_INDEX + keyLength.getLength();
        int plaintextLengthEndIndex = plaintextLengthBeginIndex + 1;
        int plaintextBeginIndex = plaintextLengthEndIndex + 1;
        byte[] _dataLength = command.subArray(plaintextLengthBeginIndex, plaintextLengthEndIndex);
        int plaintextLength = CommonUtils.convert2DecimalLength(_dataLength);
        int plaintextEndIndex = plaintextBeginIndex + plaintextLength - 1;
        return command.subArray(plaintextBeginIndex, plaintextEndIndex);
    }
}
