package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * 
 * @author master.yang
 * @version $Id: GenerateSens2.java, v 0.1 2015-1-8 下午12:39:21 master.yang Exp $
 */
public class GenerateSens2 {

    public static void main(String[] args) {
        String packKey = "18e54178ae8d39b0a736d8f7f7e933f5";
        System.out.println("packkey : " + packKey);

        String sensPlain = "800001299|20141209800001299105401|622.00|6222001203100124786|6222001203100124786=06081205489991335|996222001203100124786=156000000000000000000354899921600000608000000000000000000000=000000000000=00000000|0806|353BA3863CD422B8";

        System.out.println("sensPlain : " + sensPlain);

        byte[] senAfterSupplement = supplementData(sensPlain.getBytes());

        System.out.println("sen after supplement : " + CommonUtils.byte2hex(senAfterSupplement));

        byte[] sensCipherByte = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(packKey),
            senAfterSupplement);

        System.out.println("sensCipher : " + CommonUtils.byte2hex(sensCipherByte));

        byte[] sensBase64 = Base64.encode(sensCipherByte);

        String sensBase64Str = new String(sensBase64);

        System.out.println("sens1: " + sensBase64Str);

        sensBase64Str = sensBase64Str.replace("+", "-");
        sensBase64Str = sensBase64Str.replace("/", "_");
        System.out.println("sens2: " + sensBase64Str);
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
