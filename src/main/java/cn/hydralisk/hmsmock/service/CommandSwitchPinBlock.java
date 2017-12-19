package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.PinFormat;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

public class CommandSwitchPinBlock extends CommonCommand {

    private static final int PIN_KEY_LENGTH_SRC_INDEX = 2;

    @Override
    public byte[] execute(byte[] requestData) {
        ByteArrayBuffer command = new ByteArrayBuffer();
        command.append(requestData);

        int currentIndex = PIN_KEY_LENGTH_SRC_INDEX;
        byte pinKeyLengthSrc = extractPinKeyLengthSrc(command);
        currentIndex = currentIndex + 1;

        byte[] pinKeySrc = extractPinKeySrc(command, currentIndex, pinKeyLengthSrc);
        println("pinKeySrc: ", pinKeySrc);
        currentIndex = currentIndex + CommonUtils.convert2DecimalLength(pinKeyLengthSrc);

        byte pinKeyLengthDest = extractPinKeyLengthDest(command, currentIndex);
        currentIndex = currentIndex + 1;

        byte[] pinKeyDest = extractPinKeyDest(command, currentIndex, pinKeyLengthDest);
        println("pinKeyDest: ", pinKeyDest);
        currentIndex = currentIndex + CommonUtils.convert2DecimalLength(pinKeyLengthDest);

        byte pinFormatSrc = extractPinFormatSrc(command, currentIndex);
        println("pinFormatSrc: ", pinFormatSrc);
        confirmIsSupportivePinFormat(pinFormatSrc);
        currentIndex = currentIndex + 1;

        byte pinFormatDest = extractPinFormatDest(command, currentIndex);
        println("pinFormatDest: ", pinFormatDest);
        confirmIsSupportivePinFormat(pinFormatDest);
        currentIndex = currentIndex + 1;

        byte[] pinBlock = extractPinBlock(command, currentIndex);
        println("pinBlock: ", pinBlock);
        currentIndex = currentIndex + HmsConstants.PIN_BLOCK_LENGTH;

        int nearestEndSymbolIndex = calcNearestEndSymbolIndex(command, currentIndex);
        byte[] accountNoSrc = extractAccountSrc(command, currentIndex, nearestEndSymbolIndex);
        println("accountNoSrc: ", accountNoSrc);
        currentIndex = nearestEndSymbolIndex + 1;

        nearestEndSymbolIndex = calcNearestEndSymbolIndex(command, currentIndex);
        byte[] accountNoDest = extractAccountDest(command, currentIndex, nearestEndSymbolIndex);
        println("accountNoDest: ", accountNoDest);
        currentIndex = nearestEndSymbolIndex + 1;

        confirmNoUseLessByteAtTail(command, currentIndex);

        byte[] newPinBlock = convertPinBlock(pinBlock, pinKeySrc, pinKeyDest);

        ByteArrayBuffer responseData = new ByteArrayBuffer();
        responseData.append(newPinBlock);
        return responseData.toByteArray();
    }

    private byte extractPinKeyLengthSrc(ByteArrayBuffer command) {
        if (PIN_KEY_LENGTH_SRC_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_CIPHERTEXT);
        }
        return command.subArray(PIN_KEY_LENGTH_SRC_INDEX);
    }

    private byte[] extractPinKeySrc(ByteArrayBuffer command, int currentIndex, byte pinKeyLengthSrc) {
        int pinKeySrcBeginIndex = currentIndex;
        int pinKeySrcEndIndex = currentIndex - 1
                                + CommonUtils.convert2DecimalLength(pinKeyLengthSrc);

        if (pinKeySrcEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_KEY_SRC);
        }
        return command.subArray(pinKeySrcBeginIndex, pinKeySrcEndIndex);
    }

    private byte extractPinKeyLengthDest(ByteArrayBuffer command, int currentIndex) {
        int pinKeyLengthDestIndex = currentIndex;
        if (pinKeyLengthDestIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_KEY_LENGTH_DEST);
        }
        return command.subArray(pinKeyLengthDestIndex);
    }

    private byte[] extractPinKeyDest(ByteArrayBuffer command, int currentIndex, byte pinKeyLengthSrc) {
        int pinKeyDestBeginIndex = currentIndex;
        int pinKeyDestEndIndex = currentIndex - 1
                                 + CommonUtils.convert2DecimalLength(pinKeyLengthSrc);

        if (pinKeyDestEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_KEY_DEST);
        }
        return command.subArray(pinKeyDestBeginIndex, pinKeyDestEndIndex);
    }

    private byte extractPinFormatSrc(ByteArrayBuffer command, int currentIndex) {
        int pinFormatSrcIndex = currentIndex;
        if (pinFormatSrcIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_FORMAT_SRC);
        }
        return command.subArray(pinFormatSrcIndex);
    }

    private byte extractPinFormatDest(ByteArrayBuffer command, int currentIndex) {
        int pinFormatDestIndex = currentIndex;
        if (pinFormatDestIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_FORMAT_DEST);
        }
        return command.subArray(pinFormatDestIndex);
    }

    private byte[] extractPinBlock(ByteArrayBuffer command, int currentIndex) {
        int pinBlockBeginIndex = currentIndex;
        int pinBlockEndIndex = currentIndex + HmsConstants.PIN_BLOCK_LENGTH - 1;
        if (pinBlockEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_BLOCK);
        }
        return command.subArray(pinBlockBeginIndex, pinBlockEndIndex);
    }

    private byte[] extractAccountSrc(ByteArrayBuffer command, int currentIndex,
                                     int firstEndSymbolIndex) {
        int accountSrcBeginIndex = currentIndex;
        int accountSrcEndIndex = firstEndSymbolIndex - 1;
        if (accountSrcEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_ACCOUNT_NO_SRC);
        }
        return command.subArray(accountSrcBeginIndex, accountSrcEndIndex);
    }

    private byte[] extractAccountDest(ByteArrayBuffer command, int currentIndex,
                                      int firstEndSymbolIndex) {
        int accountDestBeginIndex = currentIndex;
        int accountDestEndIndex = firstEndSymbolIndex - 1;
        if (accountDestEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_ACCOUNT_NO_DEST);
        }
        return command.subArray(accountDestBeginIndex, accountDestEndIndex);
    }

    private int calcNearestEndSymbolIndex(ByteArrayBuffer command, int currentIndex) {
        if (currentIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_ACCOUNT_NO_END_SYMBOL);
        }
        byte[] remainingData = command.subArray(currentIndex, command.length() - 1);
        int index = currentIndex;
        for (byte b : remainingData) {
            if (b == HmsConstants.ACCOUNT_NO_END_SYMBOL) {
                return index;
            }
            index++;
        }
        throw new CommonException(RespErrorByte.NOT_FOUND_ACCOUNT_NO_END_SYMBOL);
    }

    private void confirmIsSupportivePinFormat(byte pinFormat) {
        if (!PinFormat.isSupportivePinFormat(pinFormat)) {
            throw new CommonException(RespErrorByte.NOT_SUPPORTIVE_PIN_FORMAT);
        }
    }

    private void confirmNoUseLessByteAtTail(ByteArrayBuffer command, int currentIndex) {
        if (!(command.length() == currentIndex)) {
            throw new CommonException(RespErrorByte.HAS_USELESS_BYTE_AT_TAIL);
        }
    }

    private byte[] convertPinBlock(byte[] pinBlock, byte[] pinKeySrc, byte[] pinKeyDest) {
        byte[] pinKeySrcPlaintext = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), pinKeySrc);

        byte[] pinKeyDestPlaintext = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), pinKeyDest);

        byte[] realPinBlock = ThreeDesUtils.decryptKey(pinKeySrcPlaintext, pinBlock);
        byte[] newPinBlock = ThreeDesUtils.encryptKey(pinKeyDestPlaintext, realPinBlock);

        return newPinBlock;
    }

    private void println(String desc, byte b) {
        println(desc, new byte[] { b });
    }

    private void println(String desc, byte[] b) {
        CommonUtils.println(desc + ": " + CommonUtils.byte2hexHexSytle(b));
    }
}
