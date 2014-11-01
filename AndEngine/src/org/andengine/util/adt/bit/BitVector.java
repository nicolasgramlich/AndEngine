package org.andengine.util.adt.bit;

import org.andengine.util.adt.DataConstants;


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

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCapacity;
	private final long[] mData;

	// ===========================================================
	// Constructors
	// ===========================================================

	private BitVector(final int pCapacity) {
		if(pCapacity <= 0) {
			throw new IllegalArgumentException("pCapacity must be > 0.");
		}
		this.mCapacity = pCapacity;

		/* Check if bytes perfectly fit into the data fields or if there are some overflow bytes that need special treatment. */
		final boolean perfectDataFit = pCapacity % DataConstants.BITS_PER_LONG == 0;

		final int dataCapacity;
		if(perfectDataFit) {
			dataCapacity = pCapacity / DataConstants.BITS_PER_LONG;
		} else {
			/* Extra field for overflow bytes. */
			dataCapacity = (pCapacity / DataConstants.BITS_PER_LONG) + 1;
		}
		this.mData = new long[dataCapacity];
	}

	public BitVector(final byte[] pBytes) {
		this(pBytes.length * DataConstants.BITS_PER_BYTE);

		final long[] data = this.mData;

		/* Check if bytes perfectly fit into the data fields or if there are some overflow bytes that need special treatment. */
		final boolean perfectDataFit = pBytes.length % DataConstants.BYTES_PER_LONG == 0;

		final int dataCapacity = data.length;
		final int lastCompleteDataIndex = (perfectDataFit) ? dataCapacity - 1 : dataCapacity - 2;

		for(int i = lastCompleteDataIndex; i >= 0; i--) {
			final int bytesOffset = i * DataConstants.BYTES_PER_LONG;

			data[i] = ((((long)pBytes[bytesOffset + 0]) << 56) & 0xFF00000000000000L)
					| ((((long)pBytes[bytesOffset + 1]) << 48) & 0xFF000000000000L)
					| ((((long)pBytes[bytesOffset + 2]) << 40) & 0xFF0000000000L)
					| ((((long)pBytes[bytesOffset + 3]) << 32) & 0xFF00000000L)
					| ((((long)pBytes[bytesOffset + 4]) << 24) & 0xFF000000L)
					| ((((long)pBytes[bytesOffset + 5]) << 16) & 0xFF0000L)
					| ((((long)pBytes[bytesOffset + 6]) <<  8) & 0xFF00L)
					| ((((long)pBytes[bytesOffset + 7]) <<  0) & 0xFFL);
		}

		/* Put overflow bytes into last data field. */
		if(!perfectDataFit) {
			long overflowData = 0;

			final int overflowDataIndex = dataCapacity - 1;
			final int overflowBytesOffset = overflowDataIndex * DataConstants.BYTES_PER_LONG;

			final int overflowByteCount = pBytes.length - overflowBytesOffset;
			switch(overflowByteCount) {
				case 7:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 6]) <<  8) & 0xFF00L);
				case 6:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 5]) << 16) & 0xFF0000L);
				case 5:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 4]) << 24) & 0xFF000000L);
				case 4:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 3]) << 32) & 0xFF00000000L);
				case 3:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 2]) << 40) & 0xFF0000000000L);
				case 2:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 1]) << 48) & 0xFF000000000000L);
				case 1:
					overflowData = overflowData | ((((long)pBytes[overflowBytesOffset + 0]) << 56) & 0xFF00000000000000L);
			}

			data[overflowDataIndex] = overflowData;
		}
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
		final int dataOffset = pPosition % DataConstants.BITS_PER_LONG;

		final long dataField = this.mData[dataIndex];

		final int rightShift = DataConstants.BITS_PER_LONG - dataOffset - 1;
		final long bit = (dataField >> rightShift) & 0x01;

		return bit != 0;
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
		final int offset = pPosition % DataConstants.BITS_PER_LONG;

		final long data;
		if(offset == 0) {
			data = this.mData[dataIndex];
		} else if(offset + pLength <= DataConstants.BITS_PER_LONG) {
			data = this.mData[dataIndex] << offset;
		} else {
			/* Join bits from adjacent data fields. */
			data = (this.mData[dataIndex] << offset) | (this.mData[dataIndex + 1] >>> (DataConstants.BITS_PER_LONG - offset));
		}

		if(pLength == DataConstants.BITS_PER_LONG) {
			return data;
		} else {
			final int rightShift = DataConstants.BITS_PER_LONG - pLength;
			final long mask = 0xFFFFFFFFFFFFFFFFL >>> rightShift;
			final long unmaskedBits = data >> rightShift;
			return unmaskedBits & mask;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = 0; i < this.mCapacity; i++) {
			sb.append(this.getBit(i) ? '1' : '0');
			if(i % 8 == 7 && i < this.mCapacity -1) {
				sb.append(' ');
			}
		}
		sb.append(']');
		return sb.toString();
	}

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
		/* Check if bytes perfectly fit into the data fields or if there are some overflow bytes that need special treatment. */
		final boolean perfectDataFit = this.mCapacity % DataConstants.BITS_PER_LONG == 0;

		final long[] data = this.mData;
		final int dataCapacity = data.length;
		final int lastCompleteDataIndex = (perfectDataFit) ? dataCapacity - 1 : dataCapacity - 2;

		int bytesOffset = lastCompleteDataIndex * DataConstants.BYTES_PER_LONG + (DataConstants.BYTES_PER_LONG - 1);
		for(int i = lastCompleteDataIndex; i >= 0; i--) {
			final long dataField = data[i];

			bytes[bytesOffset--] = (byte) ((dataField >> 0) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 8) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 16) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 24) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 32) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 40) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 48) & 0xFF);
			bytes[bytesOffset--] = (byte) ((dataField >> 56) & 0xFF);
		}

		/* Put overflow bytes into last data field. */
		if(!perfectDataFit) {
			final int overflowDataIndex = dataCapacity - 1;
			final long overflowDataField = data[overflowDataIndex];

			final int overflowBytesOffset = overflowDataIndex * DataConstants.BYTES_PER_LONG;

			final int overflowByteCount = bytes.length % DataConstants.BYTES_PER_LONG;
			switch(overflowByteCount) {
				case 7:
					bytes[overflowBytesOffset + 6] = (byte) ((overflowDataField >> 8) & 0xFF);
				case 6:
					bytes[overflowBytesOffset + 5] = (byte) ((overflowDataField >> 16) & 0xFF);
				case 5:
					bytes[overflowBytesOffset + 4] = (byte) ((overflowDataField >> 24) & 0xFF);
				case 4:
					bytes[overflowBytesOffset + 3] = (byte) ((overflowDataField >> 32) & 0xFF);
				case 3:
					bytes[overflowBytesOffset + 2] = (byte) ((overflowDataField >> 40) & 0xFF);
				case 2:
					bytes[overflowBytesOffset + 1] = (byte) ((overflowDataField >> 48) & 0xFF);
				case 1:
					bytes[overflowBytesOffset + 0] = (byte) ((overflowDataField >> 56) & 0xFF);
			}
		}

		return bytes;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}