package cn.hydralisk.hmsmock.constants;

import cn.hydralisk.hmsmock.util.CommonUtils;

public class HmsConstants {

    public static final int SERVER_PORT = 59009;

    public static final int THREAD_POOL_SIZE = 4;

    public static final int MAX_TRANSFER_DATA_LENGTH = 1024 * 4;

    public static final int SUCCESS_RESP_BYTE = (byte) 0x41;

    public static final int FAILER_RESP_BYTE = (byte) 0x45;

    // 命令类型至少占1位
    public static final int COMMAND_TYPE_AT_LEAST_LENGTH = 1;

    // 命令类型可能出现的最大长度占2位
    public static final int COMMAND_TYPE_MAX_LENGTH = 2;

    public static final String LOCAL_MASTER_KEY_KEY = "E08EC774E3B63E7C0C7EF991A48CE7F5";

    public static final String LOCAL_MASTER_KEY = CommonUtils.decryptLocalMasterKey();

    public static final byte[] NOT_USE_ZMK_MARK = new byte[] { (byte) 0xff, (byte) 0xff };

    public static final int PIN_BLOCK_LENGTH = 8;

    public static final byte ACCOUNT_NO_END_SYMBOL = (byte) 0x3b;

    public static final byte[] MAC_INITIAL_VECTOR = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00,
                                                                0x00, 0x00, 0x00 };
}
