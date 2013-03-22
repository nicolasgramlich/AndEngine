package org.andengine.util.adt.bit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.andengine.util.adt.data.constants.DataConstants;
import org.andengine.util.exception.MethodNotYetImplementedException;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since Nov 20, 2012
 */
public class ByteBackedBitVector extends BitVector {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mSize;
	private final byte[] mData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ByteBackedBitVector(final int pSize) throws IllegalArgumentException {
		this(pSize, new byte[BitVector.calculateByteSize(pSize)]);
	}

	public ByteBackedBitVector(final byte[] pData) throws IllegalArgumentException, NullPointerException {
		this(pData.length * Byte.SIZE, pData);
	}

	public ByteBackedBitVector(final int pSize, final byte[] pData) throws IllegalArgumentException, NullPointerException {
		if (pData == null) {
			throw new IllegalArgumentException("pData must not be null");
		}

		if (pSize > (pData.length * Byte.SIZE)) {
			throw new IllegalArgumentException("pData is too short.");
		}

		if (BitVector.calculateByteSize(pSize) < pData.length) {
			throw new IllegalArgumentException("pData is too long.");
		}

		this.mSize = pSize;

		this.mData = pData;
	}

	public ByteBackedBitVector(final DataInputStream pDataInputStream) throws IOException {
		this.mSize = pDataInputStream.readInt();
		this.mData = new byte[BitVector.calculateByteSize(this.mSize)];

		pDataInputStream.readFully(this.mData);
	}

	public static ByteBackedBitVector load(final DataInputStream pDataInputStream) throws IOException {
		return new ByteBackedBitVector(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getSize() {
		return this.mSize;
	}

	public int getByteSize() {
		return this.mData.length;
	}

	@Override
	public int getBit(final int pIndex) throws IllegalArgumentException {
		if ((pIndex < 0) || (pIndex >= this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int byteIndex = BitVector.getByteIndex(pIndex);
		final int indexInByte = BitVector.getIndexInByte(pIndex);

		return BitVector.getBitInByte(this.mData[byteIndex], indexInByte);
	}

	@Override
	public boolean getBitAsBoolean(final int pIndex) throws IllegalArgumentException {
		return this.getBit(pIndex) == BitVector.TRUE;
	}

	@Override
	public void setBit(final int pIndex) throws IllegalArgumentException {
		this.setBit(pIndex, true);
	}

	@Override
	public void clearBit(final int pIndex) throws IllegalArgumentException {
		this.setBit(pIndex, false);
	}

	@Override
	public void setBit(final int pIndex, final boolean pTrue) throws IllegalArgumentException {
		if ((pIndex < 0) || (pIndex >= this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int byteIndex = BitVector.getByteIndex(pIndex);
		final int indexInByte = BitVector.getIndexInByte(pIndex);
		final byte oldByte = this.mData[byteIndex];

		final byte newByte = BitVector.setBitInByte(oldByte, indexInByte, pTrue);
		this.mData[byteIndex] = newByte;
	}

	@Override
	public int getBits(final int pIndex, final int pCount) throws IllegalArgumentException {
		if ((pIndex < 0) || (pIndex + pCount > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		int bits = 0;

		int bitsLeft = pCount;
		int index = pIndex;
		while (bitsLeft >= Byte.SIZE) {
			bits = bits << Byte.SIZE;
			bits |= (this.getByte(index)) & 0xFF;
			index += Byte.SIZE;
			bitsLeft -= Byte.SIZE;
		}

		for (int i = 0; i < bitsLeft; i++) {
			bits = bits << 1;
			if(this.getBit(index) == BitVector.TRUE) {
				bits |= 0x01;
			}
			index++;
		}

		return bits;
	}

	@Override
	public long getLongBits(final int pIndex, final int pLength) throws IllegalArgumentException {
		throw new MethodNotYetImplementedException();
	}

	@Override
	public void setBits(final int pIndex, final byte pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + pBitCount) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		if ((pBitIndex < 0) || ((pBitIndex + pBitCount) > Byte.SIZE)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pBitIndex);
		}

		final int indexInByte = BitVector.getIndexInByte(pIndex);
		final int byteIndex = BitVector.getByteIndex(pIndex);
		if (indexInByte == 0) {
			/* Perfect match, easy get. */
			final byte newByte = BitVector.setBitsInByte(this.mData[byteIndex], 0, pBits, pBitIndex, pBitCount);
			this.mData[byteIndex] = newByte;
		} else {
			final byte highByte = this.mData[byteIndex];
			final int highByteBitCount = Math.min(pBitCount, Byte.SIZE - indexInByte);

			final byte newHighByte = BitVector.setBitsInByte(highByte, indexInByte, pBits, pBitIndex, highByteBitCount);
			this.mData[byteIndex] = newHighByte;

			if (highByteBitCount < pBitCount) {
				final byte lowByte = this.mData[byteIndex + 1];
				final int lowByteBitIndex = pBitIndex + highByteBitCount;
				final int lowByteBitCount = pBitCount - highByteBitCount;

				final byte newLowByte = BitVector.setBitsInByte(lowByte, 0, pBits, lowByteBitIndex, lowByteBitCount);
				this.mData[byteIndex + 1] = newLowByte;
			}
		}
	}

	@Override
	public void setBits(final int pIndex, final short pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + pBitCount) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		if ((pBitIndex < 0) || ((pBitIndex + pBitCount) > Short.SIZE)) {
			throw new IllegalArgumentException("pBitIndex out of bounds: " + pBitIndex);
		}

		final int highByteBitCount = Math.min(pBitCount, Math.max(0, Byte.SIZE - pBitIndex));
		if (highByteBitCount != 0) {
			final byte highByte = (byte)((pBits >> (1 * Byte.SIZE)) & 0xFF);
			this.setBits(pIndex, highByte, pBitIndex, highByteBitCount);
		}

		if (pBitCount > highByteBitCount) {
			final byte lowByte = (byte)((pBits >> (0 * Byte.SIZE)) & 0xFF);
			final int lowByteBitCount = pBitCount - highByteBitCount;

			if(highByteBitCount == 0) {
				this.setBits(pIndex, lowByte, (pBitIndex - Byte.SIZE) % Byte.SIZE, lowByteBitCount);
			} else {
				this.setBits(pIndex + highByteBitCount, lowByte, 0, lowByteBitCount);
			}
		}
	}

	@Override
	public void setBits(final int pIndex, final int pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + pBitCount) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		if ((pBitIndex < 0) || ((pBitIndex + pBitCount) > Integer.SIZE)) {
			throw new IllegalArgumentException("pBitIndex out of bounds: " + pBitIndex);
		}

		final int highShortBitCount = Math.min(pBitCount, Math.max(0, Short.SIZE - pBitIndex));
		if (highShortBitCount != 0) {
			final short highShort = (short)((pBits >> (1 * Short.SIZE)) & 0xFFFF);
			this.setBits(pIndex, highShort, pBitIndex, highShortBitCount);
		}

		if (pBitCount > highShortBitCount) {
			final short lowShort = (short)((pBits >> (0 * Short.SIZE)) & 0xFFFF);
			final int lowShortBitCount = pBitCount - highShortBitCount;

			if(highShortBitCount == 0) {
				this.setBits(pIndex, lowShort, (pBitIndex - Short.SIZE) % Short.SIZE, lowShortBitCount);
			} else {
				this.setBits(pIndex + highShortBitCount, lowShort, 0, lowShortBitCount);
			}
		}
	}

	@Override
	public byte getByte(final int pIndex) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + Byte.SIZE) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int indexInByte = BitVector.getIndexInByte(pIndex);
		final int byteIndex = BitVector.getByteIndex(pIndex);
		if (indexInByte == 0) {
			/* Perfect match, easy get. */
			return this.mData[byteIndex];
		} else {
			final byte highByte = this.mData[byteIndex];
			final byte lowByte = this.mData[byteIndex + 1];

			final int highBits = BitVector.getBitsInByte(highByte, indexInByte, Byte.SIZE - indexInByte);
			final int lowBits = BitVector.getBitsInByte(lowByte, 0, indexInByte);

			final int result = (highBits << indexInByte) + lowBits;

			return (byte)result;
		}
	}

	@Override
	public final void setByte(final int pIndex, final byte pByte) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + Byte.SIZE) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int indexInByte = BitVector.getIndexInByte(pIndex);
		final int byteIndex = BitVector.getByteIndex(pIndex);
		if (indexInByte == 0) {
			/* Perfect match, easy set. */
			this.mData[byteIndex] = pByte;
		} else {
			final byte highByte = this.mData[byteIndex];
			final byte lowByte = this.mData[byteIndex + 1];

			this.mData[byteIndex] = BitVector.setBitsInByte(highByte, indexInByte, pByte, 0, Byte.SIZE - indexInByte);
			this.mData[byteIndex + 1] = BitVector.setBitsInByte(lowByte, 0, pByte, Byte.SIZE - indexInByte, indexInByte);
		}
	}

	@Override
	public short getShort(final int pIndex) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + Short.SIZE) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int highByte = this.getByte(pIndex) & 0xFF;
		final int lowByte = this.getByte(pIndex + Byte.SIZE) & 0xFF;

		final short result = (short)((highByte << Byte.SIZE) | lowByte);

		return result;
	}

	@Override
	public final void setShort(final int pIndex, final short pShort) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + Short.SIZE) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final byte highByte = (byte)((pShort >> Byte.SIZE) & 0xFF);
		final byte lowByte = (byte)(pShort & 0xFF);

		this.setByte(pIndex, highByte);
		this.setByte(pIndex + Byte.SIZE, lowByte);
	}

	@Override
	public int getInt(final int pIndex) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + Integer.SIZE) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		final int highestByte = this.getByte(pIndex + (0 * Byte.SIZE)) & 0xFF;
		final int highByte = this.getByte(pIndex + (1 * Byte.SIZE)) & 0xFF;
		final int lowByte = this.getByte(pIndex + (2 * Byte.SIZE)) & 0xFF;
		final int lowestByte = this.getByte(pIndex + (3 * Byte.SIZE)) & 0xFF;

		final int result = (highestByte << (3 * Byte.SIZE)) | (highByte << (2 * Byte.SIZE)) | (lowByte << (1 * Byte.SIZE)) | lowestByte;

		return result;
	}

	@Override
	public void setInt(final int pIndex, final int pInt) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + Integer.SIZE) > this.mSize)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		this.setByte(pIndex + (0 * Byte.SIZE), (byte)((pInt >> (3 * Byte.SIZE)) & 0xFF));
		this.setByte(pIndex + (1 * Byte.SIZE), (byte)((pInt >> (2 * Byte.SIZE)) & 0xFF));
		this.setByte(pIndex + (2 * Byte.SIZE), (byte)((pInt >> (1 * Byte.SIZE)) & 0xFF));
		this.setByte(pIndex + (3 * Byte.SIZE), (byte)((pInt >> (0 * Byte.SIZE)) & 0xFF));
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
		this.fill((byte)0x00);
	}

	@Override
	public void fill(final byte pByte) {
		Arrays.fill(this.mData, pByte);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void save(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mSize);
		pDataOutputStream.write(this.mData);
	}

	@Override
	public byte[] toByteArray() {
		final byte[] bytes = new byte[this.mData.length];
		System.arraycopy(this.mData, 0, bytes, 0, this.mData.length);
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

			if (((i % Byte.SIZE) == (Byte.SIZE - 1)) && (i < (this.mSize - 1))) {
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
