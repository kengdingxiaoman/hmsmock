package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * 
 * @author master.yang
 * @version $Id: TestEncryptSens.java, v 0.1 2015-1-8 下午3:01:08 master.yang Exp $
 */
public class TestEncryptSens{
    
    public static void main(String[] args) {
        String sens = "800002149|20150108800002149151346|2860.00|6222801426311246120|6222801426311246120=40045207231020000||4004|CCD4DA27ECC0DF03|";
        
        String tmkUnderLmk = "7034099967f2c7ce2eefefc096518ec9";
        byte[] tmkPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(tmkUnderLmk));

        String packKey = "34473da4d93d2605dbf11359608b0a36";
        byte[] packKeyPlain = ThreeDesUtils.decryptKey(tmkPlain, CommonUtils.hex2byte(packKey));

        System.out.println("packkey plain : " + CommonUtils.byte2hex(packKeyPlain));
        
        byte[] sensCipher = ThreeDesUtils.encryptKey(packKeyPlain, supplementData(sens.getBytes()));
        
        System.out.println(CommonUtils.byte2hex(sensCipher));
        
        byte[] sensBase64 = Base64.encode(sensCipher);

        String sensBase64Str = new String(sensBase64);

        System.out.println("sens after base64 : " + sensBase64Str);

        sensBase64Str = sensBase64Str.replace("+", "-");
        sensBase64Str = sensBase64Str.replace("/", "_");
        System.out.println("sens final : " + sensBase64Str);
    }
    
    private static byte[] supplementData(byte[] data) {
        int dataLength = data.length;

        int surplus = dataLength % 8;

        if (surplus == 0) {
            return data;
        }

        ByteArrayBuffer _data = new ByteArrayBuffer();
        _data.append(data);

        int needSupplementTimes = 8 - surplus;
        for (int index = 1; index <= needSupplementTimes; index++) {
            _data.append((byte) 0x00);
        }
        return _data.toByteArray();
    }
}
