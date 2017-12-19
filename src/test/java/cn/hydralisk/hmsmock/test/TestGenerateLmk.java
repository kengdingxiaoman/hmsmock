package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.CommonMacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestGenerateLmk.java, v 0.1 2014-11-20 下午2:19:43 master.yang Exp $
 */
public class TestGenerateLmk {

    public static void main(String[] args) {
        //test0();
        test1();
        //test2();
        //test3();
    }

    public static void test0() {
        String tmkKeyUnderLmk = "af00e6695a835dba54e5b8434f3e5a8c";

        byte[] tmkKey = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY),
            CommonUtils.hex2byte(tmkKeyUnderLmk));

        String pinKeyUnderTmk = "073a4f05eeeb0e2a44f0fbf4867242dc";

        byte[] pinKey = ThreeDesUtils.decryptKey(tmkKey, CommonUtils.hex2byte(pinKeyUnderTmk));

        byte[] pinKeyKcv = ThreeDesUtils.encryptKey(pinKey, ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println(CommonUtils.byte2hex(pinKey));
        System.out.println(CommonUtils.byte2hex(pinKeyKcv));
    }

    public static void test1() {
        String vector1 = "86779352507258EFBEF07FDEFC61627C";
        String vector2 = "DF4F89E36C3C517210E6A248C158F159";

        byte[] v1 = CommonUtils.hex2byte(vector1);
        byte[] v2 = CommonUtils.hex2byte(vector2);

        byte[] mainKey = CommonMacCalculator.xor(v1, v2);

        byte[] kcv = ThreeDesUtils.encryptKey(mainKey, ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println(CommonUtils.byte2hex(kcv));

        byte[] mainKeyUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), mainKey);

        System.out.println((CommonUtils.byte2hex(mainKeyUnderLmk)).toUpperCase());
    }

    public static void test2() {
        String vector1 = "138CE5B32C0E9126C291E970EA98764C";
        String vector2 = "0c5f27f0536077fa1ba9d76aa3711100";

        byte[] v1 = CommonUtils.hex2byte(vector1);
        byte[] v2 = CommonUtils.hex2byte(vector2);

        byte[] result = CommonMacCalculator.xor(v1, v2);

        System.out.println(CommonUtils.byte2hex(result));
    }

    public static void test3() {
        String key = "99999999988888888201411271742170";
        String mainKey = "12345678123456781234567812345678";

        byte[] mainPlain = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(key),
            CommonUtils.hex2byte(mainKey));

        System.out.println(CommonUtils.byte2hex(mainPlain));

        String main = "88f34f812a1b7d5688f34f812a1b7d56";

        byte[] kcvCheck = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(main),
            ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println(CommonUtils.byte2hex(kcvCheck));

    }
}
