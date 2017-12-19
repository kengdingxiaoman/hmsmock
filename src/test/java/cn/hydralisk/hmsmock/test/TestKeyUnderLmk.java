package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestKeyUnderLmk.java, v 0.1 2014-12-10 下午2:58:16 master.yang Exp $
 */
public class TestKeyUnderLmk {

    public static void main(String[] args) {
        String tmk = "0123456789ABCDEF0123456789ABCDEF";

        byte[] kcv = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(tmk),
            ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println(CommonUtils.byte2hex(kcv));

        byte[] tmkUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(tmk));

        System.out.println(CommonUtils.byte2hex(tmkUnderLmk));
    }
}
