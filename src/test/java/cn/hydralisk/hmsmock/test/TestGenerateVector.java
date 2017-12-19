package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonMacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestGenerateVector.java, v 0.1 2015-9-24 上午9:03:38 master.yang Exp $
 */
public class TestGenerateVector {

    public static void main(String[] args) {
        String vector1 = CommonUtils.genRandomHexStr(32);
        String vector2 = CommonUtils.genRandomHexStr(32);
        
        System.out.println("vector1 is :" + vector1);
        System.out.println("vector2 is :" + vector2);

        byte[] key = CommonMacCalculator.xor(CommonUtils.hex2byte(vector1), CommonUtils.hex2byte(vector2));
        
        System.out.println("key is : " + CommonUtils.byte2hex(key));
        
        byte[] kcv = ThreeDesUtils.encryptKey(key,
            ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println("kcv is : " + CommonUtils.byte2hex(kcv));
    }
}
