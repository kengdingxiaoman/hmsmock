package cn.hydralisk.hmsmock.constants.enums;

/**
 * MAC算法标志
 * @author master.yang
 * @version $Id: MacAlgorithm.java, v 0.1 2014-8-15 下午3:00:57 master.yang Exp $
 */
public enum MacAlgorithm {

    ANSI99((byte) 0x02), ANSI919((byte) 0x03), ECB((byte) 0x04);

    private byte algorithm;

    MacAlgorithm(byte algorithm) {
        this.algorithm = algorithm;
    }

    public static boolean isSupport(byte algorithm) {
        MacAlgorithm[] algorithms = MacAlgorithm.values();
        for (MacAlgorithm _algorithm : algorithms) {
            if (_algorithm.getAlgorithm() == algorithm) {
                return true;
            }
        }
        return false;
    }

    public static MacAlgorithm getMatchedAlgorithm(byte algorithm) {
        MacAlgorithm[] algorithms = MacAlgorithm.values();
        for (MacAlgorithm _algorithm : algorithms) {
            if (_algorithm.getAlgorithm() == algorithm) {
                return _algorithm;
            }
        }
        return null;
    }

    public byte getAlgorithm() {
        return algorithm;
    }
}
