package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.EcbMacCalculator;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestEcbMac.java, v 0.1 2015-1-16 下午5:11:56 master.yang Exp $
 */
public class TestEcbMac {

    public static void main(String[] args) {
        String data = "02007024048020C0981119955997003000000021500000" +
        		      "000000000000010242831510021000349559970030000000215D" +
        		      "0000210181554630353430303030313035343331303035333131" +
        		      "30303031313536DD54F10FBECE95F32600000000000000000822000001";

        String macKey = "BD56FB43474A67D2F4FD56D60D7D7044";
        
        String lmk = "5986bfbd461beb417a1b4e4f4c077e22";
        byte[] lmkByte = CommonUtils.hex2byte(lmk);
        ByteArrayBuffer buffer = new ByteArrayBuffer();
        buffer.append((byte) ((byte) 0x48 ^ lmkByte[0]));
        for (int i = 1; i < lmkByte.length; i++) {
            buffer.append(lmkByte[i]);
        }
        byte[] realLmk = buffer.toByteArray();

        byte[] macKeyPlain = ThreeDesUtils.decryptKey(realLmk, CommonUtils.hex2byte(macKey));

        byte[] mac = EcbMacCalculator.calculate(macKeyPlain, HmsConstants.MAC_INITIAL_VECTOR,
            CommonUtils.hex2byte(data));

        System.out.println("mac is " + CommonUtils.byte2hex(mac));
        
        //be9cee53ba3ef6dd
    }
}
