package cn.hydralisk.hmsmock.sample;

import com.orca.hmsmock.hms.util.CommonMacCalculator;
import com.orca.hmsmock.hms.util.CommonUtils;
import com.orca.hmsmock.hms.util.ThreeDesUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * 
 * @author master.yang
 * @version $Id: GenerateSens.java, v 0.1 2015-1-8 上午11:36:53 master.yang Exp $
 */
public class GenerateSens {

    public static void main(String[] args) {
        String packKey = "A2EFE08A32E90FE5D3EBC18E4C71711E";
        System.out.println("packkey : " + packKey);

        String sensPlain = "887633367|20141103898760077000012|8876.23|6225211110578837|6225288880578837=9870120012342020000|||06c5c087668f9978";
        System.out.println("sensPlain : " + sensPlain);

        byte[] senAfterSupplement = CommonMacCalculator.supplementData(sensPlain.getBytes());

        System.out.println("sen after supplement : " + CommonUtils.byte2hex(senAfterSupplement));

        byte[] sensCipher = ThreeDesUtils.encryptKey(CommonUtils.hex2byte(packKey),
            senAfterSupplement);

        System.out.println("sensCipher : " + CommonUtils.byte2hex(sensCipher));

        byte[] sensBase64 = Base64.encode(sensCipher);

        String sensBase64Str = new String(sensBase64);

        System.out.println("sens after base64 : " + sensBase64Str);

        sensBase64Str = sensBase64Str.replace("+", "-");
        sensBase64Str = sensBase64Str.replace("/", "_");
        System.out.println("sens final : " + sensBase64Str);
    }
}
