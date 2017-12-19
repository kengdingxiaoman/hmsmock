package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.EcbMacCalculator;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestEcbCalculator.java, v 0.1 2014-12-19 上午10:11:58 master.yang Exp $
 */
public class TestEcbCalculator {

    public static void main(String[] args) {
        String mackKey = "BD56FB43474A67D2";

        String lmk = "5986bfbd461beb417a1b4e4f4c077e22";
        byte[] lmkByte = CommonUtils.hex2byte(lmk);
        ByteArrayBuffer buffer = new ByteArrayBuffer();
        buffer.append((byte) ((byte) 0x48 ^ lmkByte[0]));
        for (int i = 1; i < lmkByte.length; i++) {
            buffer.append(lmkByte[i]);
        }

        byte[] bianZhongLmk = buffer.toByteArray();

        byte[] macKeyPlain = ThreeDesUtils.decryptKey(bianZhongLmk, CommonUtils.hex2byte(mackKey));

        String initialVector = "0000000000000000";

        String data = "373739303643373745383132374130333137";

        byte[] mac = EcbMacCalculator.calculate(macKeyPlain, CommonUtils.hex2byte(initialVector),
            CommonUtils.hex2byte(data));

        System.out.println(CommonUtils.byte2hex(mac));
    }
}
