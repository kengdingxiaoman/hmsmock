package cn.hydralisk.hmsmock.util;

import cn.hydralisk.hmsmock.constants.CommonConstants;

/**
 * 仿照 org.apache.http.util.ByteArrayBuffer
 * 
 * @author master.yang
 * @version $Id: ByteArrayBuffer.java, v 0.1 2013-1-24 下午4:35:44 jason.yang Exp
 *          $
 */
public class ByteArrayBuffer {

	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	private byte[] buffer;

	private int len;

	public ByteArrayBuffer() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	public ByteArrayBuffer(int capacity) {
		super();
		if (capacity < 0) {
			throw new IllegalArgumentException(
					"Buffer capacity may not be negative");
		}
		this.buffer = new byte[capacity];
	}

	private void expand(int newlen) {
		byte newbuffer[] = new byte[Math.max(this.buffer.length << 1, newlen)];
		System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
		this.buffer = newbuffer;
	}

	public void append(final byte[] b) {
		this.append(b, 0, b.length);
	}

	public void append(final byte[] b, int off, int len) {
		if (b == null) {
			return;
		}
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) < 0)
				|| ((off + len) > b.length)) {
			throw new IndexOutOfBoundsException();
		}
		if (len == 0) {
			return;
		}
		int newlen = this.len + len;
		if (newlen > this.buffer.length) {
			expand(newlen);
		}
		System.arraycopy(b, off, this.buffer, this.len, len);
		this.len = newlen;
	}

	public void append(int b) {
		int newlen = this.len + 1;
		if (newlen > this.buffer.length) {
			expand(newlen);
		}
		this.buffer[this.len] = (byte) b;
		this.len = newlen;
	}

	public void append(byte b) {
		int newlen = this.len + 1;
		if (newlen > this.buffer.length) {
			expand(newlen);
		}
		this.buffer[this.len] = b;
		this.len = newlen;
	}

	public byte subArray(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index is " + index);
		}
		if (index >= this.len) {
			throw new IndexOutOfBoundsException("index is " + index);
		}
		return this.buffer[index];
	}

	public byte[] subArray(int beginIndex, int endIndex) {
		if (beginIndex < 0 || endIndex < 0) {
			throw new IllegalArgumentException("beginIndex is " + beginIndex
					+ CommonConstants.COMMA + "endIndex is " + endIndex);
		}
		if (endIndex >= this.len) {
			throw new IndexOutOfBoundsException("endIndex is " + endIndex);
		}
		if (beginIndex > endIndex) {
			throw new IllegalArgumentException("beginIndex is " + beginIndex
					+ CommonConstants.COMMA + "endIndex is " + endIndex);
		}

		int subArrayLength = endIndex - beginIndex + 1;
		byte[] b = new byte[subArrayLength];
		System.arraycopy(this.buffer, beginIndex, b, 0, subArrayLength);
		return b;
	}

	public void clear() {
		this.len = 0;
	}

	public byte[] toByteArray() {
		byte[] b = new byte[this.len];
		if (this.len > 0) {
			System.arraycopy(this.buffer, 0, b, 0, this.len);
		}
		return b;
	}

	public void setLength(int len) {
		if (len < 0 || len > this.buffer.length) {
			throw new IndexOutOfBoundsException();
		}
		this.len = len;
	}

	public int byteAt(int i) {
		return this.buffer[i];
	}

	public boolean isEmpty() {
		return this.len == 0;
	}

	public boolean isFull() {
		return this.len == this.buffer.length;
	}

	public int capacity() {
		return this.buffer.length;
	}

	public int length() {
		return this.len;
	}

	public byte[] buffer() {
		return this.buffer;
	}
}
