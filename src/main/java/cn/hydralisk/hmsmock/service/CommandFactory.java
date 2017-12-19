package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.CommandType;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.CommonUtils;

public abstract class CommandFactory {

    public static Command generateCommand(byte[] requestData) {
        if (requestData == null) {
            throw new CommonException(RespErrorByte.NO_REQUEST_DATA);
        }
        byte[] commandType = extractCommandType(requestData);

        if (isMatchedCommand(CommandType.GENERATE_KEY, commandType)) {
            return new CommandGenerateKey();
        }
        if (isMatchedCommand(CommandType.DECRYPT_KEY_UNDER_ZMK, commandType)) {
            return new CommandDecryptKeyUnderZmk();
        }
        if(isMatchedCommand(CommandType.DECRYPT_PINBLOCK, commandType)){
            return new CommandDecryptPinBlock();
        }
        if (isMatchedCommand(CommandType.ENCRYPT_PLAINTEXT, commandType)) {
            return new CommandEncryptPlaintext();
        }
        if (isMatchedCommand(CommandType.DECRYPT_CIPHERTEXT, commandType)) {
            return new CommandDecryptCiphertext();
        }
        if (isMatchedCommand(CommandType.CALCULATE_MAC, commandType)) {
            return new CommandCalculateMac();
        }
        if (isMatchedCommand(CommandType.GENERATE_PIN_BLOCK, commandType)) {
            return new CommandGeneratePinBlock();
        }
        if (isMatchedCommand(CommandType.SWITCH_PIN_BLOCK, commandType)) {
            return new CommandSwitchPinBlock();
        }
        if (isMatchedCommand(CommandType.CONVERT_TO_UNDER_ANOTHER_KEY, commandType)) {
            return new CommandConvertToUnderAnotherKey();
        }
        throw new CommonException(RespErrorByte.UNSUPPORT_COMMAND);
    }

    private static byte[] extractCommandType(byte[] requestData) {
        if (requestData.length < HmsConstants.COMMAND_TYPE_AT_LEAST_LENGTH) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_COMMAND);
        }

        /**
         * 命令有可能是2位(0xd1 0x07)，也可能是1位(0x41)，由于使用的大部分是2位，所以优先考虑位数多的
         */
        for (int commandTypeLength = HmsConstants.COMMAND_TYPE_MAX_LENGTH; commandTypeLength >= 1; commandTypeLength--) {
            if (requestData.length < commandTypeLength) {
                continue;
            }

            byte[] commandType = new byte[commandTypeLength];
            System.arraycopy(requestData, 0, commandType, 0, commandTypeLength);
            if (CommandType.hasMathchedCommand(commandType)) {
                return commandType;
            }
        }
        return null;
    }

    private static boolean isMatchedCommand(CommandType commandType, byte[] requestCommandType) {
        return CommonUtils.isEquals(commandType.getCommand(), requestCommandType);
    }
}
