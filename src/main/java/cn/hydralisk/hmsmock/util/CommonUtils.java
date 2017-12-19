package cn.hydralisk.hmsmock.util;

import cn.hydralisk.hmsmock.constants.CommonConstants;
import cn.hydralisk.hmsmock.constants.HmsConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public abstract class CommonUtils {

    private static final String HEX_PREFIX = "0x";

    public static final int LENGTH_DESCRIPTION_FIXED_LENGTH = 4; // 例如"00 c0"

    public static final int LENGTH_SHORT_DESCRIPTION_FIXED_LENGTH = 2; // 例如"c0"

    public static final String LENGTH_DESCRIPTION_PAD_STR = "0";

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String LOCAL_MASTER_KEY_CIPHERTEXT = FileUtils.getMasterKey();

    public static String genRandomKey(int keyLength) {
        return genRandomHexStr(keyLength * 2); // 0x31 => "31"
    }

    /**
     * 随机生成N位16进制串 形如：1A02EEAFC1ABC657CEDA93C82BCF6B18 只包含0~9, A~F的随机串
     * 
     * @return
     */
    public static String genRandomHexStr(int requiredLength) {
        if (requiredLength <= 0) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        boolean isNum = false;
        for (int i = 0; i < requiredLength; i++) {
            isNum = random.nextInt(2) % 2 == 0;
            if (isNum) {
                sb.append(String.valueOf(random.nextInt(10)));
            } else {
                sb.append((char) (65 + random.nextInt(6)));
            }
        }
        return sb.toString();
    }

    public static boolean isEquals(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }

        int length = a.length;
        for (int i = 0; i < length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * hex 转成 byte[]
     * 
     * 例如：hex2byte("21d153ef") = 0x21, 0xd1, 0x53, 0xef
     * 
     * @param str
     * @return
     */
    public static final byte[] hex2byte(String str) {
        byte[] ret = null;
        if (StringUtils.isBlank(str)) {
            return ret;
        }
        str = StringUtils.deleteWhitespace(str);
        if (str.toCharArray().length % 2 != 0) {
            str = "0" + str; // 如果是奇数位
        }
        char[] arr = str.toCharArray();
        ret = new byte[str.length() / 2];
        int length = str.length();

        for (int i = 0, j = 0, l = length; i < l; i++, j++) {
            StringBuffer swap = new StringBuffer().append(arr[i++]).append(arr[i]);
            int byteint = Integer.parseInt(swap.toString(), 16) & 0xFF;
            ret[j] = new Integer(byteint).byteValue();
        }
        return ret;
    }

    /**
     * byte数组转换为ASCII形式字符串 例如：byte2hex(0x21, 0xd1, 0x53, 0xef) = "21d153ef"
     * 
     * @param bytes
     * @return
     */
    public static final String byte2hex(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            return StringUtils.EMPTY;
        }

        StringBuffer ret = new StringBuffer();
        int bytesLength = bytes.length;
        for (int i = 0; i < bytesLength; i++) {
            byte b = bytes[i];
            String hexString = (Integer.toHexString(b & 0XFF));
            if (hexString.length() == 1) {
                ret.append("0");
            }
            ret.append(hexString);
        }
        return ret.toString();
    }

    /**
     * byte数组转换为ASCII形式字符串 例如：byte2hexSpacingWithBlank(0x21, 0xd1, 0x53, 0xef) =
     * "21 d1 53 ef"
     * 
     * @param bytes
     * @return
     */
    public static final String byte2hexSpacingWithBlank(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            return StringUtils.EMPTY;
        }

        StringBuffer ret = new StringBuffer();
        int bytesLength = bytes.length;
        for (int i = 0; i < bytesLength; i++) {
            byte b = bytes[i];
            String hexString = (Integer.toHexString(b & 0XFF));
            if (hexString.length() == 1) {
                ret.append("0");
            }
            ret.append(hexString);
            ret.append(CommonConstants.BLANK);
        }
        return ret.toString();
    }

    public static final String byte2hexHexSytle(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            return StringUtils.EMPTY;
        }

        StringBuffer ret = new StringBuffer();
        int bytesLength = bytes.length;
        for (int i = 0; i < bytesLength; i++) {
            byte b = bytes[i];

            ret.append(HEX_PREFIX);
            String hexString = (Integer.toHexString(b & 0XFF));
            if (hexString.length() == 1) {
                ret.append("0");
            }

            ret.append(hexString);
            ret.append(CommonConstants.BLANK);
        }
        return ret.toString();
    }

    public static final String byte2hexHexSytle(byte b) {
        StringBuffer ret = new StringBuffer();
        ret.append(HEX_PREFIX);
        String hexString = (Integer.toHexString(b & 0XFF));
        if (hexString.length() == 1) {
            ret.append("0");
        }
        ret.append(hexString);
        return ret.toString();
    }

    public static final String getCurrentDateTime() {
        return new SimpleDateFormat(FULL_DATE_FORMAT).format(Calendar.getInstance().getTime());
    }

    /*
     * 0x06 0x34 => "0634" => 1588
     */
    public static int convert2DecimalLength(byte[] dataLength) {
        return Integer.valueOf(byte2hex(dataLength), 16);
    }

    public static int convert2DecimalLength(byte dataLength) {
        return convert2DecimalLength(new byte[] { dataLength });
    }

    public static byte[] convert2DataHexLength(int length) {
        String hexString = Integer.toHexString(length);
        String hexStringAfterPad = StringUtils.leftPad(hexString, LENGTH_DESCRIPTION_FIXED_LENGTH,
            LENGTH_DESCRIPTION_PAD_STR);
        return hex2byte(hexStringAfterPad);
    }

    public static byte convert2DataShortHexLength(int length) {
        String hexString = Integer.toHexString(length);
        String hexStringAfterPad = StringUtils.leftPad(hexString,
            LENGTH_SHORT_DESCRIPTION_FIXED_LENGTH, LENGTH_DESCRIPTION_PAD_STR);
        return hex2byte(hexStringAfterPad)[0];
    }

    public static String decryptLocalMasterKey() {
        byte[] localMasterKey = ThreeDesUtils.decryptKey(
            hex2byte(HmsConstants.LOCAL_MASTER_KEY_KEY), hex2byte(LOCAL_MASTER_KEY_CIPHERTEXT));
        return byte2hex(localMasterKey);
    }

    public static byte[] byte2Double(byte[] b) {
        String tempStr = CommonUtils.byte2hex(b);
        return tempStr.toUpperCase().getBytes(); //转换成大写
    }

    public static void println(String str) {
        System.out.println(str);
    }
}
