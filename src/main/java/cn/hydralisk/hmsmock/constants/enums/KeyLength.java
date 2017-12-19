package cn.hydralisk.hmsmock.constants.enums;

public enum KeyLength {

	Double((byte) 0x10, 16), Single((byte) 0x08, 8), Triple((byte) 0x18, 24);

	private byte lengthByteStyle;
	private int length;

	KeyLength(byte lengthByteStyle, int length) {
		this.lengthByteStyle = lengthByteStyle;
		this.length = length;
	}

	public static boolean isSupportKeyLength(byte keyLength) {
		KeyLength[] keyLengths = KeyLength.values();
		for (KeyLength _keyLength : keyLengths) {
			if (_keyLength.getLengthByteStyle() == keyLength) {
				return true;
			}
		}
		return false;
	}

	public static KeyLength getMatchedKeyLength(byte lengthByteStyle) {
		KeyLength[] keyLengths = KeyLength.values();
		for (KeyLength keyLength : keyLengths) {
			if (keyLength.getLengthByteStyle() == lengthByteStyle) {
				return keyLength;
			}
		}
		return null;
	}

	public int getLength() {
		return this.length;
	}

	public byte getLengthByteStyle() {
		return this.lengthByteStyle;
	}
}
