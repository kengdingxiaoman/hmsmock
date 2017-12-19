package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestByteToDouble.java, v 0.1 2014-12-19 上午10:03:40 master.yang Exp $
 */
public class TestByteToDouble {
    
    public static void main(String[] args) {
        byte[] b = new byte[]{(byte) 0xFE};
        
        String str = CommonUtils.byte2hex(b);
        
        System.out.println(str);
        
        b = str.getBytes();
        
        System.out.println(CommonUtils.byte2hex(b));
    }
}
