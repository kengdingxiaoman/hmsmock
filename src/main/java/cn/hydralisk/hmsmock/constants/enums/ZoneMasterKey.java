package cn.hydralisk.hmsmock.constants.enums;

import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 模拟存放的 ZMK
 */
public enum ZoneMasterKey {

    INDEX_ELEVEN(new byte[] { (byte) 0x00, (byte) 0x0b }, "54FBB31AED991B05ABC03E3A51376E91"),
    INDEX_TWELVE(new byte[] { (byte) 0x00, (byte) 0x0c }, "2531A634013188D9422904FE2EB91862"),
    INDEX_TWENTY_ONE(new byte[] { (byte) 0x00, (byte) 0x15 }, "3402B4B48F37AB6D4E920944188D2CAA"),
    INDEX_TWENTY_SIX(new byte[] { (byte) 0x00, (byte) 0x1A }, "d9205d42b909f0f92a871bcd0d8e43ff"),
    INDEX_TWENTY_SEVEN(new byte[] { (byte) 0x00, (byte) 0x1B }, "c5519d0fbe9829dd7e5234ec0fb2fa57"),
    INDEX_THIRTY(new byte[] { (byte) 0x00, (byte) 0x1e }, "ffe5afcde212aabcfaa24ee7f7494fcc"),
    INDEX_THIRTY_ONE(new byte[] { (byte) 0x00, (byte) 0x1f }, "C1FDEEB429C5F3CA2EFD6126D83CFDEB"),
    INDEX_THIRTY_TWO(new byte[] { (byte) 0x00, (byte) 0x20 }, "C2C5F5893BA1D4FBC427EA6928F592AA"),
    INDEX_THIRTY_THREE(new byte[] { (byte) 0x00, (byte) 0x21 }, "BF33EAA5A2BBBF21CD6EE915A647BE52"),
    INDEX_THIRTY_FOUR(new byte[]{(byte)0x00,(byte)0x22},"5ECA80133A93B92AA4D2E0A9FDA4C4BF"),
    INDEX_THIRTY_FIVE(new byte[]{(byte)0x00,(byte)0x23},"7C48A18FAF4FA9BB05BAA989EAA421AB"),
    INDEX_FORTY(new byte[] { (byte) 0x00, (byte) 0x28 }, "06597a3a842e1299ee9ca2aa242d3069"),
    INDEX_FORTY_ONE(new byte[]{(byte) 0x00, (byte) 0x29 },"DF3C8930D9ED613EE5379DCA7D67D0CE"),
    INDEX_FORTY_FOUR(new byte[]{(byte) 0x00, (byte) 0x2C },"3FE1AE16E4FE039BBB056C09C490EFDC"),
    NOT_USE(new byte[] { (byte) 0xff, (byte) 0xff }, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

    private byte[] index;
    private String value;

    ZoneMasterKey(byte[] index, String value) {
        this.index = index;
        this.value = value;
    }

    public static boolean isExistentZmk(byte[] index) {
        ZoneMasterKey[] zoneMasterKeys = ZoneMasterKey.values();
        for (ZoneMasterKey zoneMasterKey : zoneMasterKeys) {
            if (CommonUtils.isEquals(zoneMasterKey.getIndex(), index)) {
                return true;
            }
        }
        return false;
    }

    public static ZoneMasterKey getMatchedZoneMasterKey(byte[] index) {
        ZoneMasterKey[] zoneMasterKeys = ZoneMasterKey.values();
        for (ZoneMasterKey zoneMasterKey : zoneMasterKeys) {
            if (CommonUtils.isEquals(zoneMasterKey.getIndex(), index)) {
                return zoneMasterKey;
            }
        }
        return null;
    }

    public byte[] getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}
