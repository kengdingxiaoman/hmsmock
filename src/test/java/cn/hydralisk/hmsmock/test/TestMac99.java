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
 * @version $Id: TestMac99.java, v 0.1 2015-1-16 下午5:16:32 master.yang Exp $
 */
public class TestMac99 {

    public static void main(String[] args) {
        String dataStr = "02007024048020C0981119955997003000000021500000000000000000010242"
                         + "831510021000349559970030000000215D000021018155463035343030303031303534"
                         + "333130303533313130303031313536DD54F10FBECE95F32600000000000000000822000001";

        byte[] data = CommonUtils.hex2byte(dataStr);

        byte[] dataAfterSupplement = CommonMacCalculator.supplementData(data);

        List<byte[]> dataGroups = CommonMacCalculator.groupData(dataAfterSupplement);

        byte[] xorResult = new byte[8];
        xorResult = CommonMacCalculator.xor(dataGroups.get(0), HmsConstants.MAC_INITIAL_VECTOR);

        int groupsLength = dataGroups.size();
        for (int index = 1; index < groupsLength; index++) {
            xorResult = CommonMacCalculator.xor(xorResult, dataGroups.get(index));
        }

        System.out.println("xorResult is " + CommonUtils.byte2hex(xorResult));

        String macKey = "2A3B192D9D1FBB86";

        byte[] macKeyPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(macKey));

        byte[] xorResultDouble = CommonUtils.byte2Double(xorResult);
        byte[] mac = Ansi99MacCalculator.calculate(macKeyPlain, HmsConstants.MAC_INITIAL_VECTOR,
            xorResultDouble);

        System.out.println("mac is " + CommonUtils.byte2hex(mac));
    }
}
