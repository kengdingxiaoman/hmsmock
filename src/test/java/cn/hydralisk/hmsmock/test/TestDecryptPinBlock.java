package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.PinBlockGenerator;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * 测试解密 pinblock
 * @author master.yang
 * @version $Id: TestDecryptPinBlock.java, v 0.1 2014-12-9 上午11:19:22 master.yang Exp $
 */
public class TestDecryptPinBlock{

    public static void main(String[] args) {
        String sens = "PfzZsTKPK-5QTfOIJRsuxQRCL-hv74pLbrltnUNm--v6WRojqXnWFF"
                      + "Ckr24bEA5PAGkdaGdApdZQSTSNER_C77ks76wbhGmjJB3TYu6ZskYzN0V-5QIi2e"
                      + "eOOPaWmiBwpx6C5LG1d4u5LO-sG4RpoyQd02LumbJGIaeYydT6LygBiQluvgb5mC"
                      + "1TsPaDkj6unbqNifQUBquDOaJ9dTEyiwGJCW6-BvmYAYkJbr4G-Zhyo7Wt64Xncg"
                      + "GJCW6-BvmYVK5V2KQ4pj0MPiKTvkKJ1GdIFYFGz6KRo1HJtcQyOWsZ-xi5CcgXsQ==";

        System.out.println(sens);
        sens = sens.replace("-", "+");
        sens = sens.replace("_", "/");
        System.out.println(sens);

        byte[] sensCipher = Base64.decode(sens);

        String packkey = "18e54178ae8d39b0a736d8f7f7e933f5";
        byte[] packkeyPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(packkey));

        System.out.println("packkey plain : " + CommonUtils.byte2hex(packkeyPlain));
        
        byte[] sensPlain = ThreeDesUtils.decryptKey(packkeyPlain, sensCipher);

        System.out.println(new String(sensPlain));

        String pinBlock = "353BA3863CD422B8";
        System.out.println("pinBlock : " + pinBlock);

        String pan = "6222001203100124786";
        String pin = "111111";
        byte[] pinBlockByte = PinBlockGenerator.generate(pin, pan);

        String pinkey = "1024f56da51341b9d59de0ac60ca6dcd";
        byte[] pinkeyPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(pinkey));

        byte[] resultPinblock = ThreeDesUtils.encryptKey(pinkeyPlain, pinBlockByte);

        System.out.println("generate pinblock : "
                           + CommonUtils.byte2hex(resultPinblock).toUpperCase());
    }
}
