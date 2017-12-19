package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.enums.ZoneMasterKey;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 
 * @author master.yang
 * @version $Id: TestThreeDes.java, v 0.1 2014-8-15 下午1:53:38 master.yang Exp $
 */
public class TestThreeDes {

    public static void main(String[] args) {
        String tmkUnderZmk = "3DE96CE6F9BC9AE294F9B669E646DF30";

        byte[] tmk = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(ZoneMasterKey.INDEX_ELEVEN.getValue()),
            CommonUtils.hex2byte(tmkUnderZmk));

        byte[] kcv = ThreeDesUtils.encryptKey(tmk, ThreeDesUtils.KCV_CHECK_VALUE);

        System.out.println(CommonUtils.byte2hex(kcv).toUpperCase());
    }

    //6584b25385c6bae18b920d737c500871
    //6584B25385C6BAE18B920D737C500871

    //E16F7748143297BF8EA5339E801E34B4
}
