package cn.hydralisk.hmsmock.exception;

/**
 * 错误原因
 */
public enum RespErrorByte {

    NO_REQUEST_DATA((byte) 0x01, "未收到请求数据"),
    ABSENCE_OF_COMMAND((byte) 0x02, "未指定命令参数"),
    UNSUPPORT_COMMAND((byte) 0x03, "不支持的命令"),
    ABSENCE_OF_KEY_TYPE((byte) 0x04, "未指定密钥类型"),
    UNSUPPORT_KEY_TYPE((byte) 0x05, "不支持的密钥类型"),
    ABSENCE_OF_KEY_LENGTH((byte) 0x06, "未指定密钥长度"),
    UNSUPPORT_KEY_LENGTH((byte) 0x07, "不支持的密钥长度"),
    ABSENCE_OF_ZMK_INDEX((byte) 0x08, "未指定区域密钥索引"),
    UNSUPPORT_ZMK_INDEX((byte) 0x09, "不支持的区域密钥索引"),
    ABSENCE_OF_CIPHERTEXT((byte) 0x10, "缺少密文"),
    NOT_USE_NO_NEED_ZMK_MARK((byte) 0x11, "未使用不需要ZMK的索引表示：0xFF 0xFF"),
    ABSENCE_OF_TMK_LENGTH((byte) 0x12, "未指定TMK长度"),
    TMK_LENGTH_NOT_MATCHED_WORK_LENGTH((byte) 0x13, "TMK长度与要求生成的工作密钥长度不符"),
    ABSENCE_OF_TMK_UNDER_LMK((byte) 0x14, "TmkUnderLmk位数不足"),
    ABSENCE_OF_ENCRYPTION_TYPE((byte) 0x15, "未指定算法标识"),
    UNSUPPORT_ENCRYPTION_TYPE((byte) 0x16, "不支持的加密算法"),
    ABSENCE_OF_PIN_KEY_LENGTH_SRC((byte) 0x17, "缺少PinKeySrc的长度"),
    ABSENCE_OF_PIN_KEY_SRC((byte) 0x18, "缺少PinKeySrc"),
    ABSENCE_OF_PIN_KEY_LENGTH_DEST((byte) 0x19, "缺少PinKeyDest的长度"),
    ABSENCE_OF_PIN_KEY_DEST((byte) 0x20, "缺少PinKeyDest"),
    ABSENCE_OF_PIN_FORMAT_SRC((byte) 0x21, "缺少PinFormatSrc"),
    ABSENCE_OF_PIN_FORMAT_DEST((byte) 0x22, "缺少PinFormatDest"),
    ABSENCE_OF_PIN_BLOCK((byte) 0x23, "缺少PinBlock"),
    ABSENCE_OF_ACCOUNT_NO_SRC((byte) 0x25, "缺少主账号Src"),
    ABSENCE_OF_ACCOUNT_NO_DEST((byte) 0x26, "缺少主账号Dest"),
    HAS_USELESS_BYTE_AT_TAIL((byte) 0x27, "在结尾有无用的字节"),
    NOT_SUPPORTIVE_PIN_FORMAT((byte) 0x28, "不支持的PinFormat"),
    ABSENCE_OF_ACCOUNT_NO_END_SYMBOL((byte) 0x29, "缺少主账号结束符标记"),
    NOT_FOUND_ACCOUNT_NO_END_SYMBOL((byte) 0x30, "未找到主账号结束符标记"),
    ABSENCE_OF_MAC_CALCULATE_TYPE((byte) 0x31, "未指定MAC算法标志"),
    UNSUPPORT_MAC_CALCULATE_ALGORITHM((byte) 0x32, "不支持的MAC计算算法"),
    ABSENCE_OF_MAC_KEY((byte) 0x33, "缺少MacKey"),
    ABSENCE_OF_MAC_INITIAL_VECTOR((byte) 0x34, "缺少MAC初始向量"),
    ABSENCE_OF_DATA_LENGTH((byte) 0x35, "缺少数据长度"),
    ABSENCE_OF_DATA((byte) 0x36, "缺少数据"),
    KCV_NOT_MATCHED_REQUIRED((byte) 0x37, "KCV与给定的不匹配"),
    ABSENCE_OF_ORIGINAL_KEY((byte) 0x38, "缺少原始密钥"),
    ABSENCE_OF_KCV_LENGTH((byte) 0x39, "缺少KCV长度"),
    ABSENCE_OF_KCV((byte) 0x40, "缺少KCV"),
    ABSENCE_OF_WORKKEY((byte) 0x41, "缺少工作密钥"),
    KEY_LENGTH_NOT_EQUAL_REQUIRED((byte) 0x42, "密钥长度不正确"),
    ABSENCE_OF_PIN_KEY((byte) 0x43, "缺少PinKey"),
    ABSENCE_OF_PIN_FORMAT((byte) 0x44, "缺少PinFormat"),
    ABSENCE_OF_PIN_LENGTH((byte) 0x45, "缺少Pin长度"),
    ABSENCE_OF_PIN((byte) 0x46, "缺少Pin"),
    
    KEY_LENGTH_EXCEED_LIMIT((byte) 0x97, "密钥长度超出限制"),
    REQUEST_IP_NOT_IN_WHITE_LIST((byte) 0x98, "请求IP不在白名单中"),
    UNKNOWN_ERROR((byte) 0x99, "系统未知错误");

    private byte errorByte;

    private String errorDesc;

    private RespErrorByte(byte errorByte, String errorDesc) {
        this.errorByte = errorByte;
        this.errorDesc = errorDesc;
    }

    public byte getErrorByte() {
        return errorByte;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
