package org.andengine.util.adt.bit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.andengine.util.adt.data.constants.DataConstants;
import org.andengine.util.exception.MethodNotYetImplementedException;


/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:56:32 - 30.10.2011
 */
public class LongBackedBitVector extends BitVector {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mSize;
	private final long[] mData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LongBackedBitVector(final int pSize) throws IllegalArgumentException {
		if (pSize <= 0) {
			throw new IllegalArgumentException("pSize must be > 0.");
		}
		this.mSize = pSize;

		/* Check if bytes perfectly fit into the data fields or if there are some overflow bytes that need special treatment. */
		final boolean perfectDataFit = (pSize % DataConstants.BITS_PER_LONG) == 0;

		final int dataLength;
		if (perfectDataFit) {
			dataLength = pSize / DataConstants.BITS_PER_LONG;
		} else {
			/* Extra field for overflow bytes. */
			dataLength = (pSize / DataConstants.BITS_PER_LONG) + 1;
		}
		this.mData = new long[dataLength];
	}

	public LongBackedBitVector(final long[] pData) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	public LongBackedBitVector(final int pSize, final long[] pData) throws IllegalArgumentException {
		if (pData == null) {
			throw new IllegalArgumentException("pData must not be null");
		}

		if (pSize > (pData.length * Long.SIZE)) {
			throw new IllegalArgumentException("pData is too short.");
		}

		if (BitVector.calculateLongSize(pSize) < pData.length) {
			throw new IllegalArgumentException("pData is too long.");
		}

		throw new MethodNotYetImplementedException();
	}

	public LongBackedBitVector(final byte[] pData) throws IllegalArgumentException, NullPointerException {
		this(pData.length * DataConstants.BITS_PER_BYTE);

		final long[] data = this.mData;

		/* Check if bytes perfectly fit into the data fields or if there are some overflow bytes that need special treatment. */
		final boolean perfectDataFit = (pData.length % DataConstants.BYTES_PER_LONG) == 0;

		final int dataLength = data.length;
		final int lastCompleteDataIndex = (perfectDataFit) ? dataLength - 1 : dataLength - 2;

		for (int i = lastCompleteDataIndex; i >= 0; i--) {
			final int bytesOffset = i * DataConstants.BYTES_PER_LONG;

			data[i] = ((((long)pData[bytesOffset + 0]) << 56) & 0xFF00000000000000L)
					| ((((long)pData[bytesOffset + 1]) << 48) & 0xFF000000000000L)
					| ((((long)pData[bytesOffset + 2]) << 40) & 0xFF0000000000L)
					| ((((long)pData[bytesOffset + 3]) << 32) & 0xFF00000000L)
					| ((((long)pData[bytesOffset + 4]) << 24) & 0xFF000000L)
					| ((((long)pData[bytesOffset + 5]) << 16) & 0xFF0000L)
					| ((((long)pData[bytesOffset + 6]) <<  8) & 0xFF00L)
					| ((((long)pData[bytesOffset + 7]) <<  0) & 0xFFL);
		}

		/* Put overflow bytes into last data field. */
		if (!perfectDataFit) {
			long overflowData = 0;

			final int overflowDataIndex = dataLength - 1;
			final int overflowBytesOffset = overflowDataIndex * DataConstants.BYTES_PER_LONG;

			final int overflowByteCount = pData.length - overflowBytesOffset;
			switch (overflowByteCount) {
				case 7:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 6]) <<  8) & 0xFF00L);
				case 6:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 5]) << 16) & 0xFF0000L);
				case 5:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 4]) << 24) & 0xFF000000L);
				case 4:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 3]) << 32) & 0xFF00000000L);
				case 3:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 2]) << 40) & 0xFF0000000000L);
				case 2:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 1]) << 48) & 0xFF000000000000L);
				case 1:
					overflowData = overflowData | ((((long)pData[overflowBytesOffset + 0]) << 56) & 0xFF00000000000000L);
			}

			data[overflowDataIndex] = overflowData;
		}
	}

	public LongBackedBitVector(final DataInputStream pDataInputStream) throws IOException {
		this.mSize = pDataInputStream.readInt();
		this.mData = new long[BitVector.calculateLongSize(this.mSize)];

		throw new MethodNotYetImplementedException();

//		pDataInputStream.readFully(this.mData);
	}

	public static LongBackedBitVector load(final DataInputStream pDataInputStream) throws IOException {
		return new LongBackedBitVector(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getSize() {
		return this.mSize;
	}

	@Override
	public int getBit(final int pIndex) throws IllegalArgumentException {
		if (pIndex < 0) {
			throw new IllegalArgumentException("pIndex must be >= 0.");
		}
		if (pIndex >= this.mSize) {
			throw new IllegalArgumentException("pIndex must be < size.");
		}

		final int dataIndex = pIndex / DataConstants.BITS_PER_LONG;
		final int dataOffset = pIndex % DataConstants.BITS_PER_LONG;

		final long dataField = this.mData[dataIndex];

		final int rightShift = DataConstants.BITS_PER_LONG - dataOffset - 1;
		final long dataBit = (dataField >> rightShift) & 0x01;
		final int bit = (int)dataBit;

		return bit;
	}

	@Override
	public boolean getBitAsBoolean(final int pIndex) throws IllegalArgumentException {
		return this.getBit(pIndex) == BitVector.TRUE;
	}

	@Override
	public void setBit(final int pIndex) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public void clearBit(final int pIndex) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public void setBit(final int pIndex, final boolean pTrue) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public int getBits(final int pIndex, final int pLength) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public long getLongBits(final int pIndex, final int pLength) throws IllegalArgumentException {
		/* Sanity checks. */
		if (pIndex < 0) {
			throw new IllegalArgumentException("pIndex must be >= 0.");
		}
		if (pLength < 0) {
			throw new IllegalArgumentException("pLength must be >= 0.");
		}
		if ((pIndex + pLength) > this.mSize) {
			throw new IllegalArgumentException("pIndex + pLength must be <= size.");
		}

		/* Early exit. */
		if (pLength == 0) {
			return 0L;
		}

		final int dataIndex = pIndex / DataConstants.BITS_PER_LONG;
		final int offset = pIndex % DataConstants.BITS_PER_LONG;

		final long data;
		if (offset == 0) {
			data = this.mData[dataIndex];
		} else if ((offset + pLength) <= DataConstants.BITS_PER_LONG) {
			data = this.mData[dataIndex] << offset;
		} else {
			/* Join bits from adjacent data fields. */
			data = (this.mData[dataIndex] << offset) | (this.mData[dataIndex + 1] >>> (DataConstants.BITS_PER_LONG - offset));
		}

		if (pLength == DataConstants.BITS_PER_LONG) {
			return data;
		} else {
			final int rightShift = DataConstants.BITS_PER_LONG - pLength;
			final long mask = 0xFFFFFFFFFFFFFFFFL >>> rightShift;
		final long unmaskedBits = data >> rightShift;
					return unmaskedBits & mask;
		}
	}


	@Override
	public void setBits(final int pIndex, final byte pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public void setBits(final int pIndex, final short pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public void setBits(final int pIndex, final int pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public byte getByte(final int pIndex) throws IllegalArgumentException {
		return (byte) this.getBits(pIndex, DataConstants.BITS_PER_BYTE);
	}

	@Override
	public void setByte(final int pIndex, final byte pByte) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public short getShort(final int pIndex) throws IllegalArgumentException {
		return (short) this.getBits(pIndex, DataConstants.BITS_PER_SHORT);
	}

	@Override
	public void setShort(final int pIndex, final short pShort) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public int getInt(final int pIndex) throws IllegalArgumentException {
		return this.getBits(pIndex, DataConstants.BITS_PER_INT);
	}

	@Override
	public void setInt(final int pIndex, final int pInt) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public long getLong(final int pIndex) throws IllegalArgumentException {
		return this.getLongBits(pIndex, DataConstants.BITS_PER_LONG);
	}

	@Override
	public void setLong(final int pIndex, final long pLong) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public void clear() {
		Arrays.fill(this.mData, 0x00);
	}

	@Override
	public void fill(final byte pByte) {
		throw new MethodNotYetImplementedException();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void save(final DataOutputStream pDataOutputStream) throws IOException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public byte[] toByteArray() {
		final int byteArrayLength;
		if ((this.mSize % DataConstants.BITS_PER_BYTE) == 0) {
			byteArrayLength = this.mSize / DataConstants.BITS_PER_BYTE;
		} else {
			byteArrayLength = (this.mSize / DataConstants.BITS_PER_BYTE) + 1;
		}

		final byte[] bytes = new byte[byteArrayLength];
		/* Check if bytes perfectly fit into the data fields or if there are some overflow bytes that need special treatment. */
		final boolean perfectDataFit = (this.mSize % DataConstants.BITS_PER_LONG) == 0;

		final long[] data = this.mData;
		final int dataLength = data.length;
		final int lastCompleteDataIndex = (perfectDataFit) ? dataLength - 1 : dataLength - 2;

		int bytesOffset = (lastCompleteDataIndex * DataConstants.BYTES_PER_LONG) + (DataConstants.BYTES_PER_LONG - 1);
		for (int i = lastCompleteDataIndex; i >= 0; i--) {
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
		if (!perfectDataFit) {
			final int overflowDataIndex = dataLength - 1;
			final long overflowDataField = data[overflowDataIndex];

			final int overflowBytesOffset = overflowDataIndex * DataConstants.BYTES_PER_LONG;

			final int overflowByteCount = bytes.length % DataConstants.BYTES_PER_LONG;
			switch (overflowByteCount) {
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

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('[');

		for (int i = 0; i < this.mSize; i++) {
			if (this.getBit(i) == BitVector.TRUE) {
				stringBuilder.append('1');
			} else {
				stringBuilder.append('0');
			}

			if (((i % Byte.SIZE) == (Long.SIZE - 1)) && (i < (this.mSize - 1))) {
				stringBuilder.append(',').append(' ');
			}
		}

		stringBuilder.append(']');
		return stringBuilder.toString();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}