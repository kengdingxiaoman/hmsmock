package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.ZoneMasterKey;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestGenerate.java, v 0.1 2014-10-11 下午3:08:24 master.yang Exp $
 */
public class TestGenerate {

    /**
     * tmkUnderZmk: 77 11 c4 39 f6 5a d4 7a 5d c2 5e 44 b4 34 48 d2 
     * kcv: 4a e3 05 2c 53 4b 4e 38 
     * tmkUnderLmk: dd f9 3f 64 0a 3a cb 9c 25 20 e3 39 28 b3 d2 75 
     * workKey: 48 cd a9 f9 ae 16 5e a6 fe 36 b0 ba cc 2a 45 7f
     * workKey under tmk: d2 86 d8 5d 52 26 25 ce 96 05 c5 ab 7d 43 cf a0 
     * workKey kcv: 10 9b 6f a5 09 cf f0 58 
     * workKey under lmk: aa 59 a9 70 b5 0a e9 cd bc 18 6e 03 27 36 5f d3
     */
    public static void main(String[] args) {
        String tmk = "6207764623C75ED5A8D9A27554B3D5A2";

        byte[] tmkUnderZmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(ZoneMasterKey.INDEX_TWENTY_ONE.getValue()),
            CommonUtils.hex2byte(tmk));
        System.out.println(CommonUtils.byte2hexSpacingWithBlank(tmkUnderZmk));

        byte[] kcv = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(tmk),
            ThreeDesUtils.KCV_CHECK_VALUE);
        System.out.println(CommonUtils.byte2hexSpacingWithBlank(kcv));

        byte[] tmkUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(tmk));
        System.out.println(CommonUtils.byte2hexSpacingWithBlank(tmkUnderLmk));

        String randomWorkKey = CommonUtils.genRandomHexStr(32);
        System.out
            .println(CommonUtils.byte2hexSpacingWithBlank(CommonUtils.hex2byte(randomWorkKey)));

        byte[] workKeyUnderTmk = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(tmk),
            CommonUtils.hex2byte(randomWorkKey));
        System.out.println(CommonUtils.byte2hexSpacingWithBlank(workKeyUnderTmk));

        byte[] workKeyKcv = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(randomWorkKey),
            ThreeDesUtils.KCV_CHECK_VALUE);
        System.out.println(CommonUtils.byte2hexSpacingWithBlank(workKeyKcv));

        byte[] workKeyUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY),
            CommonUtils.hex2byte(randomWorkKey));
        System.out.println(CommonUtils.byte2hexSpacingWithBlank(workKeyUnderLmk));

        String qianbaoTmk = "6207764623C75ED5A8D9A27554B3D5A2";
        byte[] qianbaoTmkUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(qianbaoTmk));

        System.out.println(CommonUtils.byte2hex(qianbaoTmkUnderLmk).toUpperCase());
        
        String xinyeTmk = "EF40025D32D698C8408C1543F17923B0";
        byte[] xinyeTmkUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(xinyeTmk));

        System.out.println(CommonUtils.byte2hex(xinyeTmkUnderLmk).toUpperCase());
    }
}
