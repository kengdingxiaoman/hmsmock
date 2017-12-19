package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestRandom.java, v 0.1 2014-12-6 上午9:32:39 master.yang Exp $
 */
public class TestRandom {

    public static void main(String[] args) {
        System.out.println(CommonUtils.genRandomHexStr(32));
        
        System.out.println(CommonUtils.genRandomHexStr(16));
    }
}
