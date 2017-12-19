package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.Ansi99MacCalculator;
import cn.hydralisk.hmsmock.util.CommonMacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

import java.util.List;

/**
 * 
 * @author master.yang
 * @version $Id: TestMac.java, v 0.1 2014-12-17 下午4:37:14 master.yang Exp $
 */
public class TestMac {

    public static void main(String[] args) {
        String macKeyUnderLmk = "12E8DB23265BCD68";

        byte[] macKey = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY),
            CommonUtils.hex2byte(macKeyUnderLmk));

        String dataStr = "80012X6AE05F08FB5309E71160200 166226650600800335 000000 000000000001 1217174318 000079 7011 00 0800313100 0800906611 12345678 123456789012345";
        for (int index = 2; index < 100; index++) {
            byte[] mac = Ansi99MacCalculator.calculate(macKey, ThreeDesUtils.KCV_CHECK_VALUE,
                generateData(dataStr.substring(index)));

            System.out.println(CommonUtils.byte2hex(mac));
        }

        //        byte[] mac = Ansi99MacCalculator.calculate(macKey, ThreeDesUtils.KCV_CHECK_VALUE,
        //            generateData(dataStr));
        //
        //        System.out.println(CommonUtils.byte2hex(mac));
    }

    public static byte[] generateData(String dataStr) {
        byte[] data = dataStr.getBytes();
        data = CommonMacCalculator.supplementData(data);

        List<byte[]> dataGroups = CommonMacCalculator.groupData(data);

        data = dataGroups.get(0);
        int groupsLength = dataGroups.size();
        for (int index = 1; index < groupsLength; index++) {
            data = CommonMacCalculator.xor(data, dataGroups.get(index));
        }

        System.out.println(CommonUtils.byte2hex(data));

        return CommonUtils.byte2hex(data).getBytes();
    }
}
