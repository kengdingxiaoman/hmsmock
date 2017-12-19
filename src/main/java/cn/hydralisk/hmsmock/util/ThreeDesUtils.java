package cn.hydralisk.hmsmock.util;

import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class ThreeDesUtils {

    private static final String ALGORITHM = "DESede/ECB/NoPadding";

    private static final int KEY_FIXED_LENGTH = 24;

    public static final byte[] KCV_CHECK_VALUE = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                                             0x00, 0x00 };

    public static byte[] decryptKey(byte[] mainKey, byte[] plainKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] k = normalizeKey(mainKey);
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
            byte[] k = normalizeKey(mainKey);
            SecretKey deskey = new SecretKeySpec(k, ALGORITHM);
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(plainKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3Des加密长度必须是24，但有的密钥长度可能不足，所以添加到24
     * 
     * @param mainKey
     * @return
     */
    private static byte[] normalizeKey(byte[] mainKey) {
        byte[] key = new byte[KEY_FIXED_LENGTH];
        int mainKeyLength = mainKey.length;

        if (mainKeyLength > KEY_FIXED_LENGTH) {
            throw new CommonException(RespErrorByte.KEY_LENGTH_EXCEED_LIMIT);
        }

        int needTimes = KEY_FIXED_LENGTH / mainKeyLength;
        if ((KEY_FIXED_LENGTH % mainKeyLength) != 0) {
            needTimes = needTimes + 1; //mainKey长度不是24的时候
        }

        int copyLength = 0;
        int currentIndex = 0;
        for (int times = 0; times < needTimes; times++) {
            copyLength = KEY_FIXED_LENGTH - currentIndex;

            if (copyLength > mainKeyLength) {
                copyLength = mainKeyLength;
            }

            System.arraycopy(mainKey, 0, key, currentIndex, copyLength);
            currentIndex = currentIndex + copyLength;
        }
        return key;
    }
}
