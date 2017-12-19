package cn.hydralisk.hmsmock.util;

import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;

import java.util.List;

/**
 * ANSI9.19算法
 * @author master.yang
 * @version $Id: Ansi919MacCalculator.java, v 0.1 2014-8-15 下午3:43:45 master.yang Exp $
 */
public class Ansi919MacCalculator extends CommonMacCalculator {

    private static final int MAC_KEY_REQUIRED_LENGTH = 16;

    public static byte[] calculate(byte[] macKey, byte[] initialVector, byte[] data) {
        if (macKey.length != MAC_KEY_REQUIRED_LENGTH) {
            throw new CommonException(RespErrorByte.KEY_LENGTH_NOT_EQUAL_REQUIRED);
        }

        byte[] macKeyLeft = new byte[8];
        byte[] macKeyRight = new byte[8];
        System.arraycopy(macKey, 0, macKeyLeft, 0, 8);
        System.arraycopy(macKey, 8, macKeyRight, 0, 8);

        byte[] dataAfterSupplement = supplementData(data);

        List<byte[]> dataGroups = groupData(dataAfterSupplement);

        byte[] mac = new byte[8];
        mac = xor(dataGroups.get(0), initialVector);
        mac = DesUtils.encryptKey(macKeyLeft, mac);

        int groupsLength = dataGroups.size();
        for (int index = 1; index < groupsLength; index++) {
            mac = xor(mac, dataGroups.get(index));
            mac = DesUtils.encryptKey(macKeyLeft, mac);
        }

        mac = DesUtils.decryptKey(macKeyRight, mac);
        mac = DesUtils.encryptKey(macKeyLeft, mac);

        return mac;
    }
}
