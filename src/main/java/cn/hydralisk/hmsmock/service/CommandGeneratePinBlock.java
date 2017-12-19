package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.KeyLength;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.PinBlockGenerator;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: CommandGeneratePinBlock.java, v 0.1 2014-10-15 下午3:57:36 master.yang Exp $
 */
public class CommandGeneratePinBlock extends CommonCommand {

    private static final int PIK_KEY_LENGTH_INDEX = 2;

    private static final int PIK_KEY_BEGIN_INDEX = 3;

    /** 
     * @see cn.hydralisk.hmsmock.service.CommonCommand#execute(byte[])
     */
    @Override
    public byte[] execute(byte[] requestData) {
        ByteArrayBuffer command = new ByteArrayBuffer();
        command.append(requestData);

        KeyLength pinKeyLength = extractPinKeyLength(command);

        byte[] pinKey = extractPinKey(command, pinKeyLength);
        print(pinKey);

        int currentIndex = PIK_KEY_BEGIN_INDEX + pinKeyLength.getLength();
        byte pinFormat = extractPinFormat(command, currentIndex);
        print(pinFormat);

        currentIndex = currentIndex + 1;
        byte pinLength = extractPinLength(command, currentIndex);
        print(pinLength);

        currentIndex = currentIndex + 1;
        byte[] pin = extractPin(command, currentIndex, CommonUtils.convert2DecimalLength(pinLength));
        print(pin);

        currentIndex = currentIndex + CommonUtils.convert2DecimalLength(pinLength);
        byte[] cardNo = extractCardNo(command, currentIndex);
        print(cardNo);

        //开始计算pin block
        byte[] pinBlock = generatePinBlock(pin, cardNo);

        byte[] pinKeyPlaintext = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), pinKey);

        byte[] pinBlockCiphertext = ThreeDesUtils.encryptKey(pinKeyPlaintext, pinBlock);

        ByteArrayBuffer responseData = new ByteArrayBuffer();
        responseData.append(pinBlockCiphertext);
        return responseData.toByteArray();
    }

    private KeyLength extractPinKeyLength(ByteArrayBuffer command) {
        if (PIK_KEY_LENGTH_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_LENGTH);
        }
        byte pinKeyLength = command.subArray(PIK_KEY_LENGTH_INDEX);
        if (!KeyLength.isSupportKeyLength(pinKeyLength)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_KEY_LENGTH);
        }
        return KeyLength.getMatchedKeyLength(pinKeyLength);
    }

    private byte[] extractPinKey(ByteArrayBuffer command, KeyLength pinKeyLength) {
        int pinKeyEndIndex = PIK_KEY_LENGTH_INDEX + pinKeyLength.getLength();
        if (pinKeyEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_KEY);
        }
        return command.subArray(PIK_KEY_BEGIN_INDEX, pinKeyEndIndex);
    }

    private byte extractPinFormat(ByteArrayBuffer command, int currentIndex) {
        int pinFormatIndex = currentIndex;
        if (pinFormatIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_FORMAT);
        }
        return command.subArray(pinFormatIndex);
    }

    private byte extractPinLength(ByteArrayBuffer command, int currentIndex) {
        int pinLengthIndex = currentIndex;
        if (pinLengthIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN_LENGTH);
        }
        return command.subArray(pinLengthIndex);
    }

    private byte[] extractPin(ByteArrayBuffer command, int currentIndex, int pinLength) {
        int pinBeginIndex = currentIndex;
        int pinEndIndex = currentIndex + pinLength - 1;
        if (pinEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_PIN);
        }
        return command.subArray(pinBeginIndex, pinEndIndex);
    }

    private byte[] extractCardNo(ByteArrayBuffer command, int currentIndex) {
        int cardNoBeginIndex = currentIndex;
        return command.subArray(cardNoBeginIndex, command.length() - 1);
    }

    private byte[] generatePinBlock(byte[] pin, byte[] cardNo) {
        return PinBlockGenerator.generate(new String(pin), new String(cardNo));
    }
}
