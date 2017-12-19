package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.enums.ZoneMasterKey;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestZmkUnderLmk.java, v 0.1 2014-12-31 上午9:56:26 master.yang Exp $
 */
public class TestZmkUnderLmk {

    public static void main(String[] args) {
        normal();
        bianzhong();
    }

    private static void normal() {
        String lmk = "5986bfbd461beb417a1b4e4f4c077e22";

        byte[] zmkUnderLmkByte = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(lmk),
            CommonUtils.hex2byte(ZoneMasterKey.INDEX_ELEVEN.getValue()));

        System.out.println(CommonUtils.byte2hex(zmkUnderLmkByte));
    }

    private static void bianzhong() {
        String lmk = "5986bfbd461beb417a1b4e4f4c077e22";

        String zmkUnderLmk2 = "e00528fc235df6827115a4ca943d82a8";

        byte[] zmk = ThreeDesUtils.decryptKey(CommonUtils.hex2byte(lmk),
            CommonUtils.hex2byte(zmkUnderLmk2));

        byte[] zmkKcv = ThreeDesUtils.encryptKey(zmk, ThreeDesUtils.KCV_CHECK_VALUE);

        //System.out.println(CommonUtils.byte2hex(zmk));
        System.out.println(CommonUtils.byte2hex(zmkKcv));

        byte[] lmkByte = CommonUtils.hex2byte(lmk);
        ByteArrayBuffer buffer = new ByteArrayBuffer();
        buffer.append((byte) ((byte) 0x05 ^ lmkByte[0]));
        for (int i = 1; i < lmkByte.length; i++) {
            buffer.append(lmkByte[i]);
        }

        byte[] lmkAfterBianZhong = buffer.toByteArray();

        byte[] tmkUnderLmk = ThreeDesUtils.decryptKey(lmkAfterBianZhong, zmk);

        System.out.println("bankTransInfo: " + CommonUtils.byte2hex(tmkUnderLmk));
    }
}
