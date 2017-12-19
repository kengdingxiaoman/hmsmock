package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestBcd.java, v 0.1 2014-12-17 下午5:00:10 master.yang Exp $
 */
public class TestBcd {

    public static void main(String[] args) {
        String testStr = "02 00";

        byte[] b = testStr.getBytes();

        System.out.println(CommonUtils.byte2hex(b)); //print: 3032203030
    }
}
