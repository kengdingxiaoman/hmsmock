package cn.hydralisk.hmsmock.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算MAC时会使用到的公共的一些方法
 * @author master.yang
 * @version $Id: CommonMacCalculator.java, v 0.1 2014-10-15 下午6:31:16 master.yang Exp $
 */
public class CommonMacCalculator {

    public static byte[] supplementData(byte[] data) {
        int dataLength = data.length;

        int surplus = dataLength % 8;

        if (surplus == 0) {
            return data;
        }

        ByteArrayBuffer _data = new ByteArrayBuffer();
        _data.append(data);

        int needSupplementTimes = 8 - surplus;
        for (int index = 1; index <= needSupplementTimes; index++) {
            _data.append((byte) 0x00);
        }
        return _data.toByteArray();
    }

    public static List<byte[]> groupData(byte[] data) {
        List<byte[]> groups = new ArrayList<byte[]>();

        ByteArrayBuffer _data = new ByteArrayBuffer();
        int dataLength = data.length;
        int indexInGroup = 0;
        for (int index = 0; index < dataLength; index++) {
            _data.append(data[index]);
            indexInGroup++;

            if (indexInGroup == 8) {
                groups.add(_data.toByteArray());

                indexInGroup = 0;
                _data.clear();
            }
        }

        return groups;
    }

    public static byte[] xor(byte[] a, byte[] b) {
        int length = a.length;
        byte[] result = new byte[length];
        for (int index = 0; index < length; index++) {
            result[index] = (byte) (a[index] ^ b[index]);
        }
        return result;
    }
}
