package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonMacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestGenerateKeyByVector.java, v 0.1 2015-9-24 下午12:24:20 master.yang Exp $
 */
public class TestGenerateKeyByVector {

    public static void main(String[] args) {
        String vector1 = "6BF241641FB94F26AD3293C2EF9E7FA8";

        String vector2 = "6dab3b5e9b975dbf43ae3168cbb34fc1";

        String vector3 = "00000000000000000000000000000000";

        byte[] key = CommonMacCalculator.xor(CommonUtils.hex2byte(vector1),
            CommonUtils.hex2byte(vector2));

        key = CommonMacCalculator.xor(key, CommonUtils.hex2byte(vector3));

        byte[] kcv = ThreeDesUtils.encryptKey(key, ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println(CommonUtils.byte2hex(key));

        System.out.println(CommonUtils.byte2hex(kcv));
    }
}
