package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.service.CommandConvertToUnderAnotherKey;
import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestConvertToAnotherKey.java, v 0.1 2014-10-11 下午3:05:39 master.yang Exp $
 */
public class TestConvertToAnotherKey {

    public static void main(String[] args) {
        testConvertTmkUnderLmk();
        
        System.out.println("================================================================");
        
        //testConvertWorkKeyUnderTmk();
        
        System.out.println("================================================================");
        
        //testConvertWorkKeyUnderTmkQianBao();
        
        System.out.println("================================================================");
        
        testNoKcv();
    }
    
    public static void testConvertTmkUnderLmk(){
        byte[] requestData = new byte[] { (byte) 0xd1, (byte) 0x02, 
                                          (byte) 0x10, (byte) 0x10, //key length
                                         (byte) 0x00, (byte) 0x15, //zmk index
                                         (byte) 0x01, //key type : tmk
                                         
                                         //tmk under zmk: 77 11 c4 39 f6 5a d4 7a 5d c2 5e 44 b4 34 48 d2
                                         (byte)0x77, (byte)0x11, (byte)0xc4, (byte)0x39,
                                         (byte)0xf6, (byte)0x5a, (byte)0xd4, (byte)0x7a,
                                         (byte)0x5d, (byte)0xc2, (byte)0x5e, (byte)0x44,
                                         (byte)0xb4, (byte)0x34, (byte)0x48, (byte)0xd2,
                                         
                                         (byte) 0x08, //kcv length 23
                                         
                                         //kcv: 4a e3 05 2c 53 4b 4e 38 
                                         (byte) 0x4a, (byte) 0xe3, (byte) 0x05, (byte) 0x2c,
                                         (byte) 0x53, (byte) 0x4b, (byte) 0x4e, (byte) 0x38};

        System.out.println(CommonUtils.byte2hexHexSytle(requestData));

        CommandConvertToUnderAnotherKey command = new CommandConvertToUnderAnotherKey();
        byte[] responseData = command.execute(requestData);
        System.out.println(CommonUtils.byte2hexHexSytle(responseData));
    }
    
    public static void testConvertWorkKeyUnderTmk(){
        byte[] requestData = new byte[] { (byte) 0xd1, (byte) 0x02, 
                                          (byte) 0x10, (byte) 0x10, //key length
                                         (byte) 0xff, (byte) 0xff, //not use zmk index
                                         (byte) 0x11, //key type : pik
                                         
                                         
                                         //tmk under lmk: dd f9 3f 64 0a 3a cb 9c 25 20 e3 39 28 b3 d2 75
                                         (byte)0xdd, (byte)0xf9, (byte)0x3f, (byte)0x64,
                                         (byte)0x0a, (byte)0x3a, (byte)0xcb, (byte)0x9c,
                                         (byte)0x25, (byte)0x20, (byte)0xe3, (byte)0x39,
                                         (byte)0x28, (byte)0xb3, (byte)0xd2, (byte)0x75,
                                         
                                         //workKey under tmk: d2 86 d8 5d 52 26 25 ce 96 05 c5 ab 7d 43 cf a0 
                                         (byte)0xd2, (byte)0x86, (byte)0xd8, (byte)0x5d,
                                         (byte)0x52, (byte)0x26, (byte)0x25, (byte)0xce,
                                         (byte)0x96, (byte)0x05, (byte)0xc5, (byte)0xab,
                                         (byte)0x7d, (byte)0x43, (byte)0xcf, (byte)0xa0,
                                         
                                         (byte) 0x08, //kcv length 39
                                         
                                         //kcv: 10 9b 6f a5 09 cf f0 58  
                                         (byte) 0x10, (byte) 0x9b, (byte) 0x6f, (byte) 0xa5,
                                         (byte) 0x09, (byte) 0xcf, (byte) 0xf0, (byte) 0x58};

        System.out.println(CommonUtils.byte2hexHexSytle(requestData));

        CommandConvertToUnderAnotherKey command = new CommandConvertToUnderAnotherKey();
        byte[] responseData = command.execute(requestData);
        System.out.println(CommonUtils.byte2hexHexSytle(responseData));
    }
    
    public static void testConvertWorkKeyUnderTmkQianBao(){
        byte[] requestData = new byte[] { (byte) 0xd1, (byte) 0x02, 
                                          (byte) 0x08, (byte) 0x10, //key length
                                         (byte) 0xff, (byte) 0xff, //not use zmk index
                                         (byte) 0x11, //key type : pik
                                         
                                         
                                         //tmk under lmk: dd f9 3f 64 0a 3a cb 9c 25 20 e3 39 28 b3 d2 75
                                         (byte)0xdd, (byte)0xf9, (byte)0x3f, (byte)0x64,
                                         (byte)0x0a, (byte)0x3a, (byte)0xcb, (byte)0x9c,
                                         (byte)0x25, (byte)0x20, (byte)0xe3, (byte)0x39,
                                         (byte)0x28, (byte)0xb3, (byte)0xd2, (byte)0x75,
                                         
                                         //workKey under tmk: d2 86 d8 5d 52 26 25 ce 96 05 c5 ab 7d 43 cf a0 
                                         (byte)0xc9, (byte)0xe7, (byte)0x8e, (byte)0x24,
                                         (byte)0x34, (byte)0x64, (byte)0xe3, (byte)0x88,
                                         
                                         (byte) 0x04, //kcv length 39
                                         
                                         //kcv: 10 9b 6f a5 09 cf f0 58  
                                         (byte) 0x90, (byte) 0x55, (byte) 0x0b, (byte) 0x3a};

        System.out.println(CommonUtils.byte2hexHexSytle(requestData));

        CommandConvertToUnderAnotherKey command = new CommandConvertToUnderAnotherKey();
        byte[] responseData = command.execute(requestData);
        System.out.println(CommonUtils.byte2hexHexSytle(responseData));
    }
    
    public static void testNoKcv(){
        byte[] requestData = new byte[] { (byte) 0xd1, (byte) 0x02, 
                                          (byte) 0x08, (byte) 0x10, //key length
                                         (byte) 0xff, (byte) 0xff, //not use zmk index
                                         (byte) 0x11, //key type : pik
                                         
                                         
                                         //tmk under lmk: dd f9 3f 64 0a 3a cb 9c 25 20 e3 39 28 b3 d2 75
                                         (byte)0xdd, (byte)0xf9, (byte)0x3f, (byte)0x64,
                                         (byte)0x0a, (byte)0x3a, (byte)0xcb, (byte)0x9c,
                                         (byte)0x25, (byte)0x20, (byte)0xe3, (byte)0x39,
                                         (byte)0x28, (byte)0xb3, (byte)0xd2, (byte)0x75,
                                         
                                         //workKey under tmk: d2 86 d8 5d 52 26 25 ce 96 05 c5 ab 7d 43 cf a0 
                                         (byte)0xc9, (byte)0xe7, (byte)0x8e, (byte)0x24,
                                         (byte)0x34, (byte)0x64, (byte)0xe3, (byte)0x88,
                                         
                                         (byte) 0x00, //kcv length 39
                                         
                                         //kcv: 10 9b 6f a5 09 cf f0 58  
                                         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        System.out.println(CommonUtils.byte2hexHexSytle(requestData));

        CommandConvertToUnderAnotherKey command = new CommandConvertToUnderAnotherKey();
        byte[] responseData = command.execute(requestData);
        System.out.println("responseData: " + CommonUtils.byte2hexHexSytle(responseData));
    }
}
