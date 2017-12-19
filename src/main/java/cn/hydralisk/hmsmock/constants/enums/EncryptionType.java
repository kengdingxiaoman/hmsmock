package cn.hydralisk.hmsmock.constants.enums;

/**
 * 加密类型
 */
public enum EncryptionType {
	TripleDesECB12((byte) 0x12);

	private byte type;

	EncryptionType(byte type) {
		this.type = type;
	}

	public static boolean isSupportEncryptionType(byte type) {
		EncryptionType[] encryptionTypes = EncryptionType.values();
		for (EncryptionType encryptionType : encryptionTypes) {
			if (encryptionType.getType() == type) {
				return true;
			}
		}
		return false;
	}

	public byte getType() {
		return this.type;
	}
}
