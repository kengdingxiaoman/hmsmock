package cn.hydralisk.hmsmock.util;

/**
 * 
 * @author master.yang
 * @version $Id: PinPlainGenerator.java, v 0.1 2015-7-28 下午05:27:36 hugan Exp $
 */
public class PinPlainGenerator {
    public static final int PIN_BLOCK_START_INDEX = 1;
    public static final int PIN_BLOCK_END_INDEX = 3;
    public static final byte PIN_BLOCK_PLAIN_LENFTH = 6;

    public static byte[] generate(String pinblock, String accno) {
        byte arrAccno[] = getAccno(accno);
        byte arrPinBlock[] = CommonUtils.hex2byte(pinblock);
        byte arrRet[] = new byte[8];
        for (int i = 0; i < 8; i++) {
            arrRet[i] = (byte) (arrPinBlock[i] ^ arrAccno[i]);
        }
        ByteArrayBuffer buffer = new ByteArrayBuffer();
        buffer.append(arrRet);
        byte[] pin = buffer.subArray(PIN_BLOCK_START_INDEX, PIN_BLOCK_END_INDEX);
        String pinPlain = CommonUtils.byte2hex(pin);
        ByteArrayBuffer responseData = new ByteArrayBuffer();
        responseData.append(PIN_BLOCK_PLAIN_LENFTH);
        responseData.append(pinPlain.getBytes());
        
        return responseData.toByteArray();
    }

    private static byte[] getAccno(String accno) {
        int len = accno.length();
        byte arrTemp[] = accno.substring(len < 13 ? 0 : len - 13, len - 1).getBytes();
        byte arrAccno[] = new byte[12];
        for (int i = 0; i < 12; i++) {
            arrAccno[i] = (i <= arrTemp.length ? arrTemp[i] : (byte) 0x00);
        }
        byte encode[] = new byte[8];
        encode[0] = (byte) 0x00;
        encode[1] = (byte) 0x00;
        encode[2] = (byte) uniteBytes(arrAccno[0], arrAccno[1]);
        encode[3] = (byte) uniteBytes(arrAccno[2], arrAccno[3]);
        encode[4] = (byte) uniteBytes(arrAccno[4], arrAccno[5]);
        encode[5] = (byte) uniteBytes(arrAccno[6], arrAccno[7]);
        encode[6] = (byte) uniteBytes(arrAccno[8], arrAccno[9]);
        encode[7] = (byte) uniteBytes(arrAccno[10], arrAccno[11]);
        return encode;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }
}
