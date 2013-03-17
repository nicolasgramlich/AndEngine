package org.andengine.util.adt.bit;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:14:12 - 02.03.2013
 * @author ngramlich
 */
public abstract class BitVector implements IBitVector {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int TRUE = 1;
	public static final int FALSE = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static IBitVector wrap(final byte[] pData) {
		return new ByteBackedBitVector(pData);
	}

	public static IBitVector wrap(final int pSize, final byte[] pData) {
		return new ByteBackedBitVector(pData);
	}

	public static IBitVector wrap(final long[] pData) {
		return new LongBackedBitVector(pData);
	}

	public static IBitVector wrap(final int pSize, final long[] pData) {
		return new LongBackedBitVector(pSize, pData);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static int getByteIndex(final int pIndex) {
		return pIndex / Byte.SIZE;
	}

	public static int getLongIndex(final int pIndex) {
		return pIndex / Long.SIZE;
	}

	public static int getIndexInByte(final int pIndex) {
		return pIndex % Byte.SIZE;
	}

	public static int getIndexInLong(final int pIndex) {
		return pIndex % Long.SIZE;
	}

	public static int getBitInByte(final byte pByte, final int pIndex) throws IllegalArgumentException {
		return BitVector.getBitsInByte(pByte, pIndex, 1);
	}

	public static int getBitsInByte(final byte pByte, final int pIndex, final int pCount) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + pCount) > Byte.SIZE)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		if ((pCount < 0) || (pCount > Byte.SIZE)) {
			throw new IllegalArgumentException("pBitCount out of bounds: " + pCount);
		}

		final int shift = Byte.SIZE - pIndex - pCount;
		final int shiftedByte = ((pByte & 0xFF) >> shift);

		final int mask = (0x01 << (pCount)) - 1;

		final int result = shiftedByte & mask;

		return result;
	}

	public static byte setBitInByte(final byte pByte, final int pIndex, final boolean pTrue) throws IllegalArgumentException {
		if ((pIndex < 0) || (pIndex >= Byte.SIZE)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		if (pTrue) {
			final int mask = (0x80 >> pIndex);

			final byte result = (byte)(pByte | mask);

			return result;
		} else {
			final int mask = 0xFF ^ (0x80 >> pIndex);

			final byte result = (byte)(pByte & mask);

			return result;
		}
	}

	public static byte setBitsInByte(final byte pByte, final int pIndex, final byte pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException {
		if ((pIndex < 0) || ((pIndex + pBitCount) > Byte.SIZE)) {
			throw new IllegalArgumentException("pIndex out of bounds: " + pIndex);
		}

		if ((pBitIndex + pBitCount) > Byte.SIZE) {
			throw new IllegalArgumentException("pBitIndex out of bounds: " + pIndex);
		}

		if ((pBitCount < 0) || (pBitCount > Byte.SIZE)) {
			throw new IllegalArgumentException("pBitCount out of bounds: " + pIndex);
		}

		final int sizeMask = (0x01 << (pBitCount)) - 1;

		final int byteMask = (sizeMask << (Byte.SIZE - (pBitCount + pIndex))) ^ 0xFF;
		final int maskedByte = (pByte & byteMask) & 0xFF;

		final int bitMask = sizeMask << (Byte.SIZE - (pBitCount + pBitIndex));
		final int maskedBits = (pBits & bitMask) & 0xFF;

		final int shift = (pBitIndex - pIndex);
		final int shiftedBits;
		if (shift < 0) {
			shiftedBits = maskedBits >> -shift;
		} else {
			shiftedBits = maskedBits << shift;
		}

		final byte result = (byte) (maskedByte | shiftedBits);

		return result;
	}

	public static int calculateByteSize(final int pBitSize) {
		if (pBitSize < 0) {
			throw new IllegalArgumentException("pBitSize out of bounds: " + pBitSize);
		}

		if (BitVector.getIndexInByte(pBitSize) == 0) {
			return pBitSize / Byte.SIZE;
		} else {
			return 1 + (pBitSize / Byte.SIZE);
		}
	}

	public static int calculateLongSize(final int pBitSize) {
		if (pBitSize < 0) {
			throw new IllegalArgumentException("pBitSize out of bounds: " + pBitSize);
		}

		if (BitVector.getIndexInLong(pBitSize) == 0) {
			return pBitSize / Byte.SIZE;
		} else {
			return 1 + (pBitSize / Byte.SIZE);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
