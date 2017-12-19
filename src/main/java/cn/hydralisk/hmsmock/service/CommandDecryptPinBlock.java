package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.PinPlainGenerator;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 得到pin的明文
 * @author hugan
 * @version $Id: CommandDecryptPinBlock.java, v 0.1 2015-7-28 下午02:15:14 hugan Exp $
 */
public class CommandDecryptPinBlock extends CommonCommand {

    private static final int PIN_KEY_BENGIN_INDEX = 3;

    private static final int ENCRYPT_PIN = 8;

    /** 
     * @see cn.hydralisk.hmsmock.service.CommonCommand#execute(byte[])
     */
    @Override
    public byte[] execute(byte[] requestData) {
        int keyLength = CommonUtils.convert2DecimalLength(requestData[PIN_KEY_BENGIN_INDEX - 1]);
        ByteArrayBuffer commands = new ByteArrayBuffer();
        commands.append(requestData);
        byte[] pinKey = commands.subArray(PIN_KEY_BENGIN_INDEX, keyLength + PIN_KEY_BENGIN_INDEX
                                                                - 1);
        int pinKeyPlainStartIndex = keyLength + PIN_KEY_BENGIN_INDEX;
        byte[] pinKeyPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), pinKey);

        ByteArrayBuffer command = new ByteArrayBuffer();
        command.append(requestData);
        byte[] pin = command.subArray(pinKeyPlainStartIndex + 1, pinKeyPlainStartIndex
                                                                 + ENCRYPT_PIN);
        byte[] pinPlain = ThreeDesUtils.decryptKey(pinKeyPlain, pin);
        int cardIdStartIndex = pinKeyPlainStartIndex + ENCRYPT_PIN;

        ByteArrayBuffer commandCard = new ByteArrayBuffer();
        commandCard.append(requestData);
        byte[] cardNo = commandCard
            .subArray(cardIdStartIndex+1, new String(requestData).length() - 1);

        return PinPlainGenerator.generate(CommonUtils.byte2hex(pinPlain), new String(cardNo));
    }
}
