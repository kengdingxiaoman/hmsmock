package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.Ansi99MacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestMac99NoXor.java, v 0.1 2015-1-16 下午5:41:46 master.yang Exp $
 */
public class TestMac99NoXor {
    
    public static void main(String[] args) {
        String xorResult = "38453637423345434645413843434241";
        
        String macKey = "2A3B192D9D1FBB86";

        byte[] macKeyPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(macKey));

        byte[] mac = Ansi99MacCalculator.calculate(macKeyPlain, HmsConstants.MAC_INITIAL_VECTOR,
            CommonUtils.hex2byte(xorResult));

        System.out.println("mac is " + CommonUtils.byte2hex(mac));
    }
}
