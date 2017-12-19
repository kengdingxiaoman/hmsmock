package cn.hydralisk.hmsmock.constants.enums;

import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 命令枚举
 */
public enum CommandType {

    DECRYPT_KEY_UNDER_ZMK(new byte[] { (byte) 0x41 }),

    CONVERT_TO_UNDER_ANOTHER_KEY(new byte[] { (byte) 0xd1, (byte) 0x02 }),
    GENERATE_KEY(new byte[] { (byte) 0xd1, (byte) 0x07 }),
    ENCRYPT_PLAINTEXT(new byte[] { (byte) 0xd1, (byte) 0x12 }),
    DECRYPT_CIPHERTEXT(new byte[] { (byte) 0xd1, (byte) 0x14 }),
    GENERATE_PIN_BLOCK(new byte[] { (byte) 0xd1, (byte) 0x22 }),
    SWITCH_PIN_BLOCK(new byte[] { (byte) 0xd1, (byte) 0x24 }),
    DECRYPT_PINBLOCK(new byte[]{(byte)0xd1,(byte)0x26}),
    CALCULATE_MAC(new byte[] { (byte) 0xd1, (byte) 0x32 });
    private byte[] command;

    CommandType(byte[] command) {
        this.command = command;
    }

    public static boolean hasMathchedCommand(byte[] b) {
        CommandType[] commandTypes = CommandType.values();
        for (CommandType commandType : commandTypes) {
            if (CommonUtils.isEquals(commandType.getCommand(), b)) {
                return true;
            }
        }
        return false;
    }

    public byte[] getCommand() {
        return this.command;
    }
}
