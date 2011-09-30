package org.anddev.andengine.util.data;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:56:32 - 30.10.2011
 */
public final class BitVector {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int DATA_MASK = DataConstants.BITS_PER_LONG - 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCapacity;
	private final long[] mData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BitVector(final int pCapacity) {
		if(pCapacity <= 0) {
			throw new IllegalArgumentException("pCapacity must be > 0.");
		}
		this.mCapacity = pCapacity;

		final int dataCapacity;
		if(pCapacity % DataConstants.BITS_PER_LONG == 0) {
			dataCapacity = pCapacity / DataConstants.BITS_PER_LONG;
		} else {
			dataCapacity = (pCapacity / DataConstants.BITS_PER_LONG) + 1;
		}
		this.mData = new long[dataCapacity];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCapacity() {
		return this.mCapacity;
	}

	public boolean getBit(final int pPosition) {
		if(pPosition < 0) {
			throw new IllegalArgumentException("pPosition must be >= 0.");
		}
		if(pPosition >= this.mCapacity) {
			throw new IllegalArgumentException("pPosition must be < capacity.");
		}

		final int dataIndex = pPosition / DataConstants.BITS_PER_LONG;
		final long m = 1L << (pPosition & BitVector.DATA_MASK);

		return (this.mData[dataIndex] & m) != 0;
	}

	public byte getByte(final int pPosition) {
		return (byte) this.getBits(pPosition, DataConstants.BITS_PER_BYTE);
	}

	public short getShort(final int pPosition) {
		return (short) this.getBits(pPosition, DataConstants.BITS_PER_SHORT);
	}

	public int getInt(final int pPosition) {
		return (int) this.getBits(pPosition, DataConstants.BITS_PER_INT);
	}

	public long getLong(final int pPosition) {
		return this.getBits(pPosition, DataConstants.BITS_PER_LONG);
	}

	public long getBits(final int pPosition, final int pLength) {
		/* Sanity checks. */
		if(pPosition < 0) {
			throw new IllegalArgumentException("pPosition must be >= 0.");
		}
		if(pLength < 0) {
			throw new IllegalArgumentException("pLength must be >= 0.");
		}
		if(pPosition + pLength > this.mCapacity) {
			throw new IllegalArgumentException("pPosition + pLength must be <= capacity.");
		}

		/* Early exit. */
		if(pLength == 0) {
			return 0L;
		}

		final int dataIndex = pPosition / DataConstants.BITS_PER_LONG;
		final int shift = pPosition & BitVector.DATA_MASK;

		final long data;
		if(shift == 0) {
			data = this.mData[dataIndex];
		} else if(shift + pLength <= DataConstants.BITS_PER_LONG) {
			data = this.mData[dataIndex] >>> shift;
		} else {
			data = (this.mData[dataIndex] >>> shift) | (this.mData[dataIndex + 1] << (DataConstants.BITS_PER_LONG - shift));
		}
		return pLength == DataConstants.BITS_PER_LONG ? data : data & ((1L << pLength) - 1);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public byte[] toByteArray() {
		final int byteArrayLength;
		if(this.mCapacity % DataConstants.BITS_PER_BYTE == 0) {
			byteArrayLength = this.mCapacity / DataConstants.BITS_PER_BYTE;
		} else {
			byteArrayLength = (this.mCapacity / DataConstants.BITS_PER_BYTE) + 1;
		}
		
		final byte[] bytes = new byte[byteArrayLength];
		int i = 0;
		int j = byteArrayLength; // how many bytes we have left to process
		for(; j > 8; i++) {
			final long l = this.mData[i];
			bytes[--j] = (byte) (l & 0xff);
			bytes[--j] = (byte) ((l >> 8) & 0xff);
			bytes[--j] = (byte) ((l >> 16) & 0xff);
			bytes[--j] = (byte) ((l >> 24) & 0xff);
			bytes[--j] = (byte) ((l >> 32) & 0xff);
			bytes[--j] = (byte) ((l >> 40) & 0xff);
			bytes[--j] = (byte) ((l >> 48) & 0xff);
			bytes[--j] = (byte) ((l >> 56) & 0xff);
		}
		if(j > 0) {
			final long m = -1L >>> (DataConstants.BITS_PER_LONG - this.mCapacity & BitVector.DATA_MASK);
			final long l = this.mData[i] & m;
			for(int k = 0; j > 0; k++) {
				bytes[--j] = (byte) ((l >> (k * 8)) & 0xff);
			}
		}
		return bytes;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}