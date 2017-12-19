package cn.hydralisk.hmsmock.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Des算法，如果3段的密钥是一样的，那么使用3Des做运算就等于是Des
 * @author master.yang
 * @version $Id: DesUtils.java, v 0.1 2014-8-15 下午4:14:19 master.yang Exp $
 */
public class DesUtils {

    private static final String ALGORITHM = "DESede/ECB/NoPadding";

    public static byte[] decryptKey(byte[] mainKey, byte[] plainKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] k = new byte[24];
            System.arraycopy(mainKey, 0, k, 0, 8);
            System.arraycopy(mainKey, 0, k, 8, 8);
            System.arraycopy(mainKey, 0, k, 16, 8);
            SecretKey deskey = new SecretKeySpec(k, ALGORITHM);
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(plainKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptKey(byte[] mainKey, byte[] plainKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] k = new byte[24];
            System.arraycopy(mainKey, 0, k, 0, 8);
            System.arraycopy(mainKey, 0, k, 8, 8);
            System.arraycopy(mainKey, 0, k, 16, 8);
            SecretKey deskey = new SecretKeySpec(k, ALGORITHM);
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(plainKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
