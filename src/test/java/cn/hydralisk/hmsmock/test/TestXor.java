package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author master.yang
 * @version $Id: TestXor.java, v 0.1 2014-10-17 上午11:40:06 master.yang Exp $
 */
public class TestXor {

    public static void main(String[] args) {
        byte[] data = new byte[] { (byte) 0x02, (byte) 0x00, (byte) 0x30, (byte) 0x24, (byte) 0x04, (byte) 0xC0, (byte) 0x20, (byte) 0xC0, 
                                   (byte) 0x98, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                                   (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x12, (byte) 0x34, (byte) 0x62, (byte) 0x30, (byte) 0x10, 
                                   (byte) 0x02, (byte) 0x10, (byte) 0x00, (byte) 0x06, (byte) 0x30, (byte) 0x62, (byte) 0x25, (byte) 0x00, 
                                   (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x22, (byte) 0xD3, (byte) 0x01, (byte) 0x02,
                                  (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x31, (byte) 0x30, (byte) 0x30, (byte) 0x30, 
                                  (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x34, (byte) 0x31, (byte) 0x30, (byte) 0x30, (byte) 0x30, 
                                  (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, 
                                  (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x31, (byte) 0x35, (byte) 0x36, (byte) 0x21, (byte) 0xFD, 
                                  (byte) 0xF2, (byte) 0x25, (byte) 0xF0, (byte) 0xBB, (byte) 0x35, (byte) 0x6F, (byte) 0x26, (byte) 0x00,
                                  (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, 
                                  (byte) 0x22, (byte) 0x12, (byte) 0x34, (byte) 0x56 };

        byte[] initialVector = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        byte[] dataAfterSupplement = supplementData(data);

        List<byte[]> dataGroups = groupData(dataAfterSupplement);

        byte[] mac = new byte[8];
        mac = xor(dataGroups.get(0), initialVector);

        int groupsLength = dataGroups.size();
        for (int index = 1; index < groupsLength; index++) {
            mac = xor(mac, dataGroups.get(index));
        }

        System.out.println(CommonUtils.byte2hexSpacingWithBlank(mac));

        System.out.println(CommonUtils.byte2hexHexSytle((CommonUtils.byte2hex(mac).toUpperCase())
            .getBytes()));
    }

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
