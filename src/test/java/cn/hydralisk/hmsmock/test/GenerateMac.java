package cn.hydralisk.hmsmock.sample;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.Ansi919MacCalculator;
import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 
 * @author master.yang
 * @version $Id: GenerateMac.java, v 0.1 2015-1-8 上午11:37:35 master.yang Exp $
 */
public class GenerateMac {

    public static void main(String[] args) {
        String macKey = "888DD666FCD9A24659474326E53F25CC";
        System.out.println("mac key : " + macKey);
        
        String sens = "{\"VER\":\"66\",\"PR\":\"1001\",\"PAN\":\"6222801426311246120\",\"AMT\":\"2860.00\",\"PSEQ\":\"120651\",\"EXP\":\"4004\",\"SVR\":\"022\",\"ICSEQ\":\"\",\"PINLEN\":\"0\",\"POSID\":\"800002149\",\"SENS\":\"ky1Qb_5itc1Aoq56WbXMrwkn5CIJCwbgbjHiITEgDYmAvRRfXHjBrYG352Agy5gbnnStHiP0_g4x_7Qc87Z44zueYvssBN3ehAlRWBQlvbXoQX2npol-byjDzSdG7mbQq7emd-ZPLCp1WNE47mTyZQ==\",\"ORDID\":\"20150108800002149120651\",\"IC\":\"\"}";
        System.out.println("sens data : " + sens);

        byte[] mac = Ansi919MacCalculator.calculate(CommonUtils.hex2byte(macKey),
            HmsConstants.MAC_INITIAL_VECTOR, CommonUtils.hex2byte(macKey));

        System.out.println("mac : " + CommonUtils.byte2hex(mac));
    }
}
