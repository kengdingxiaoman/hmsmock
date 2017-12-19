package cn.hydralisk.hmsmock.util;

import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;

import java.util.List;

/**
 * ANSI9.9算法
 * @author master.yang
 * @version $Id: Ansi99MacCalculator.java, v 0.1 2014-10-15 下午6:25:35 master.yang Exp $
 */
public class Ansi99MacCalculator extends CommonMacCalculator {

    private static final int MAC_KEY_REQUIRED_LENGTH = 8;

    public static byte[] calculate(byte[] macKey, byte[] initialVector, byte[] data) {
        if (macKey.length != MAC_KEY_REQUIRED_LENGTH) {
            throw new CommonException(RespErrorByte.KEY_LENGTH_NOT_EQUAL_REQUIRED);
        }

        byte[] dataAfterSupplement = supplementData(data);

        List<byte[]> dataGroups = groupData(dataAfterSupplement);

        byte[] mac = new byte[8];
        mac = xor(dataGroups.get(0), initialVector);
        mac = DesUtils.encryptKey(macKey, mac);

        int groupsLength = dataGroups.size();
        for (int index = 1; index < groupsLength; index++) {
            mac = xor(mac, dataGroups.get(index));
            mac = DesUtils.encryptKey(macKey, mac);
        }

        return mac;
    }
}
