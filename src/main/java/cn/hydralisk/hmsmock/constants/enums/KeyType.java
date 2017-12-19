package cn.hydralisk.hmsmock.constants.enums;

/**
 * 密钥枚举
 */
public enum KeyType {

	TMK((byte) 0x01, (byte) 0x6a),
	PIK((byte) 0x11, (byte) 0x6b),
	MAC((byte) 0x12, (byte) 0x6c),
	DEK((byte) 0x13, (byte) 0x6d);

	private byte keyType;
	private byte xorByte; //变种密钥第一个字节需要异或的值

	KeyType(byte keyType, byte xorByte) {
		this.keyType = keyType;
		this.xorByte = xorByte;
	}

	public static boolean isSupportKeyType(byte keyType) {
		KeyType[] keyTypes = KeyType.values();
		for (KeyType _keyType : keyTypes) {
			if (_keyType.getType() == keyType) {
				return true;
			}
		}
		return false;
	}

	public byte getType() {
		return this.keyType;
	}

	public byte getXorByte(){
		return xorByte;
	}
}