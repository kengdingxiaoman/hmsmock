package cn.hydralisk.hmsmock.util;

import java.util.List;

/**
 * mac ECB算法实现
 * @author master.yang
 * @version $Id: EcbMacCalculator.java, v 0.1 2014-12-19 上午9:57:02 master.yang Exp $
 */
public class EcbMacCalculator extends CommonMacCalculator {

    public static byte[] calculate(byte[] macKey, byte[] initialVector, byte[] data) {
        byte[] dataAfterSupplement = supplementData(data);

        List<byte[]> dataGroups = groupData(dataAfterSupplement);

        byte[] xorResult = new byte[8];
        xorResult = xor(dataGroups.get(0), initialVector);

        int groupsLength = dataGroups.size();
        for (int index = 1; index < groupsLength; index++) {
            xorResult = xor(xorResult, dataGroups.get(index));
        }

        byte[] xorResultDouble = CommonUtils.byte2Double(xorResult);

        byte[] xorResultDoubleLeft = new byte[8];
        byte[] xorResultDoubleRight = new byte[8];
        System.arraycopy(xorResultDouble, 0, xorResultDoubleLeft, 0, 8);
        System.arraycopy(xorResultDouble, 8, xorResultDoubleRight, 0, 8);

        byte[] mac = new byte[8];
        mac = ThreeDesUtils.encryptKey(macKey, xorResultDoubleLeft);
        mac = xor(mac, xorResultDoubleRight);
        mac = ThreeDesUtils.encryptKey(macKey, mac);

        return mac;
    }
}
