package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * 
 * @author master.yang
 * @version $Id: TestDecryptSens.java, v 0.1 2015-1-8 下午2:51:19 master.yang Exp $
 */
public class TestDecryptSens{

    public static void main(String[] args) {
        String sens = "ky1Qb_5itc1Aoq56WbXMrwkn5CIJCwbgSPlOlsymRBmAvRRfXHjBrYG"
                      + "352Agy5gbnnStHiP0_g4x_7Qc87Z44zueYvss"
                      + "BN3ehAlRWBQlvbXoQX2npol-byjDzSdG7mbQq7" + "emd-ZPLCp1WNE47mTyZQ==";

        sens = sens.replace("-", "+");
        sens = sens.replace("_", "/");
        System.out.println(sens);

        byte[] sensCipher = Base64.decode(sens);

        String tmkUnderLmk = "7034099967f2c7ce2eefefc096518ec9";
        byte[] tmkPlain = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), CommonUtils.hex2byte(tmkUnderLmk));

        String packKey = "c8b05bde55706db09db9d05637cc6c07";
        byte[] packKeyPlain = ThreeDesUtils.decryptKey(tmkPlain, CommonUtils.hex2byte(packKey));

        System.out.println("packkey plain : " + CommonUtils.byte2hex(packKeyPlain));

        byte[] sensPlain = ThreeDesUtils.decryptKey(packKeyPlain, sensCipher);
        System.out.println(CommonUtils.byte2hex(sensPlain));
    }
}
