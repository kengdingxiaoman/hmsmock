package cn.hydralisk.hmsmock.constants.enums;

public enum PinFormat {

	FIRST((byte) 0x01);

	private byte format;

	PinFormat(byte format) {
		this.format = format;
	}

	public static boolean isSupportivePinFormat(byte format) {
		PinFormat[] pinFormats = PinFormat.values();
		for (PinFormat pinFormat : pinFormats) {
			if (pinFormat.getFormat() == format) {
				return true;
			}
		}
		return false;
	}

	public byte getFormat() {
		return this.format;
	}
}
