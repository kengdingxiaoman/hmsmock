package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.Ansi99MacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestAnsi99.java, v 0.1 2014-10-17 下午12:58:22 master.yang Exp $
 */
public class TestAnsi99 {

    public static void main(String[] args) {
        byte[] macKey = new byte[] { (byte) 0x9e, (byte) 0xef, (byte) 0x91, (byte) 0xea,
                                    (byte) 0x9e, (byte) 0x97, (byte) 0x6d, (byte) 0x9e };

        byte[] initialVector = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                           (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

        byte[] data = new byte[] { (byte) 0x37, (byte) 0x39, (byte) 0x30, (byte) 0x36, (byte) 0x43,
                                  (byte) 0x37, (byte) 0x45, (byte) 0x38, (byte) 0x31, (byte) 0x32,
                                  (byte) 0x37, (byte) 0x41, (byte) 0x30, (byte) 0x33, (byte) 0x31,
                                  (byte) 0x37 };

        byte[] mac = Ansi99MacCalculator.calculate(macKey, initialVector, data);

        System.out.println(CommonUtils.byte2hex(mac));

        System.out.println(CommonUtils.byte2hexHexSytle((CommonUtils.byte2hex(mac).toUpperCase())
            .getBytes()));
    }
}
