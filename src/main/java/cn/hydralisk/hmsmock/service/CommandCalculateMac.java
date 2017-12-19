package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.KeyLength;
import cn.hydralisk.hmsmock.constants.enums.MacAlgorithm;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.*;

/**
 * 计算MAC
 * @author master.yang
 * @version $Id: CommandCalculateMac.java, v 0.1 2014-8-15 下午2:58:47 master.yang Exp $
 */
public class CommandCalculateMac extends CommonCommand {

    private static final int MAC_ALGORITHM_INDEX = 2;

    private static final int MAC_KEY_LENGTH_INDEX = 3;

    private static final int MAC_KEY_BEGIN_INDEX = 4;

    private static final int INITIAL_VECTOR_FIXED_LENGTH = 8;

    /** 
     * @see cn.hydralisk.hmsmock.service.CommonCommand#execute(byte[])
     */
    @Override
    public byte[] execute(byte[] requestData) {
        ByteArrayBuffer command = new ByteArrayBuffer();
        command.append(requestData);

        MacAlgorithm macAlgorithm = extractMacAlgorithm(command);

        KeyLength macKeyLength = extractMacKeyLength(command);
        byte[] macKey = extractMacKey(command, macKeyLength);

        byte[] macKeyPlaintext = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), macKey);

        byte[] initialVector = extractInitialVector(command, macKeyLength);
        byte[] data = extractData(command, macKeyLength);
        byte[] mac = calculateMac(macAlgorithm, macKeyPlaintext, initialVector, data);

        ByteArrayBuffer responseData = new ByteArrayBuffer();
        responseData.append(mac);
        return responseData.toByteArray();
    }

    private MacAlgorithm extractMacAlgorithm(ByteArrayBuffer command) {
        if (MAC_ALGORITHM_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_MAC_CALCULATE_TYPE);
        }
        byte macAlgorithm = command.subArray(MAC_ALGORITHM_INDEX);
        if (!MacAlgorithm.isSupport(macAlgorithm)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_MAC_CALCULATE_ALGORITHM);
        }
        return MacAlgorithm.getMatchedAlgorithm(macAlgorithm);
    }

    private KeyLength extractMacKeyLength(ByteArrayBuffer command) {
        if (MAC_KEY_LENGTH_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_LENGTH);
        }
        byte macKeyLength = command.subArray(MAC_KEY_LENGTH_INDEX);
        if (!KeyLength.isSupportKeyLength(macKeyLength)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_KEY_LENGTH);
        }
        return KeyLength.getMatchedKeyLength(macKeyLength);
    }

    private byte[] extractMacKey(ByteArrayBuffer command, KeyLength keyLength) {
        int keyEndIndex = MAC_KEY_BEGIN_INDEX + keyLength.getLength() - 1;
        if (keyEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_MAC_KEY);
        }
        return command.subArray(MAC_KEY_BEGIN_INDEX, keyEndIndex);
    }

    private byte[] extractInitialVector(ByteArrayBuffer command, KeyLength keyLength) {
        int initialVectorBeginIndex = MAC_KEY_BEGIN_INDEX + keyLength.getLength() - 1 + 1;
        int initialVectorEndIndex = initialVectorBeginIndex + INITIAL_VECTOR_FIXED_LENGTH - 1;
        if (initialVectorEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_MAC_INITIAL_VECTOR);
        }
        return command.subArray(initialVectorBeginIndex, initialVectorEndIndex);
    }

    private byte[] extractData(ByteArrayBuffer command, KeyLength keyLength) {
        int dataLengthBeginIndex = MAC_KEY_BEGIN_INDEX + keyLength.getLength() - 1 + 1
                                   + INITIAL_VECTOR_FIXED_LENGTH - 1 + 1;
        int dataLengthEndIndex = dataLengthBeginIndex + 1;
        if (dataLengthEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_MAC_INITIAL_VECTOR);
        }
        byte[] dataLength = command.subArray(dataLengthBeginIndex, dataLengthEndIndex);

        int dataBeginIndex = dataLengthEndIndex + 1;
        int dataEndIndex = dataBeginIndex + CommonUtils.convert2DecimalLength(dataLength) - 1;
        if (dataEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_DATA);
        }
        return command.subArray(dataBeginIndex, dataEndIndex);
    }

    private byte[] calculateMac(MacAlgorithm macAlgorithm, byte[] macKey, byte[] initialVector,
                                byte[] data) {
        if (MacAlgorithm.ANSI919.equals(macAlgorithm)) {
            return Ansi919MacCalculator.calculate(macKey, initialVector, data);
        }
        if (MacAlgorithm.ANSI99.equals(macAlgorithm)) {
            return Ansi99MacCalculator.calculate(macKey, initialVector, data);
        }
        if (MacAlgorithm.ECB.equals(macAlgorithm)) {
            return EcbMacCalculator.calculate(macKey, initialVector, data);
        }
        throw new CommonException(RespErrorByte.UNSUPPORT_MAC_CALCULATE_ALGORITHM);
    }
}
