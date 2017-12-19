package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.service.CommandGeneratePinBlock;
import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestGeneratePinBlock.java, v 0.1 2014-10-16 上午10:59:21 master.yang Exp $
 */
public class TestGeneratePinBlock {

    public static void main(String[] args) {
        byte[] requestData = new byte[] {
                                         (byte) 0xd1, (byte) 0x22, //command
                                         (byte) 0x10, //pin key length
                                         
                                         //pin key under lmk
                                         (byte) 0x77, (byte) 0x11, (byte) 0xc4, (byte) 0x39,
                                         (byte) 0xf6, (byte) 0x5a, (byte) 0xd4, (byte) 0x7a,
                                         (byte) 0x5d, (byte) 0xc2, (byte) 0x5e, (byte) 0x44,
                                         (byte) 0xb4, (byte) 0x34, (byte) 0x48, (byte) 0xd2,

                                         (byte) 0x01, //pin format
                                         
                                         (byte) 0x06, //pin length
                                         
                                         //pin  
                                         (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x30,
                                         (byte) 0x30, (byte) 0x35, 
                                         
                                         //card no
                                         (byte) 0x36, (byte) 0x32, (byte) 0x32, (byte) 0x36,
                                         (byte) 0x32, (byte) 0x34, (byte) 0x33, (byte) 0x31,
                                         (byte) 0x30, (byte) 0x32, (byte) 0x32, (byte) 0x34,
                                         (byte) 0x35, (byte) 0x36, (byte) 0x36, (byte) 0x38 };

        System.out.println(CommonUtils.byte2hexHexSytle(requestData));
        CommandGeneratePinBlock command = new CommandGeneratePinBlock();
        byte[] responseData = command.execute(requestData);
        System.out.println(CommonUtils.byte2hexHexSytle(responseData));
    }
}
