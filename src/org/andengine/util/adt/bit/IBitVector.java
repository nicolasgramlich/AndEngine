package org.andengine.util.adt.bit;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * (c) 2013 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:14:12 - 02.03.2013
 */
public interface IBitVector {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getSize();
	public int getBit(final int pIndex) throws IllegalArgumentException;
	public void setBit(final int pIndex) throws IllegalArgumentException;
	public void clearBit(final int pIndex) throws IllegalArgumentException;
	public void setBit(final int pIndex, final boolean pTrue) throws IllegalArgumentException;
	boolean getBitAsBoolean(int pIndex) throws IllegalArgumentException;

	public byte getByte(final int pIndex) throws IllegalArgumentException;
	public void setByte(final int pIndex, final byte pByte) throws IllegalArgumentException;

	public short getShort(final int pIndex) throws IllegalArgumentException;
	public void setShort(final int pIndex, final short pShort) throws IllegalArgumentException;

	public int getInt(final int pIndex) throws IllegalArgumentException;
	public void setInt(final int pIndex, final int pInt) throws IllegalArgumentException;

	public long getLong(final int pIndex) throws IllegalArgumentException;
	public void setLong(final int pIndex, final long pLong) throws IllegalArgumentException;

	public int getBits(final int pIndex, final int pLength) throws IllegalArgumentException;
	public void setBits(final int pIndex, final byte pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException;
	public void setBits(final int pIndex, final short pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException;
	public void setBits(final int pIndex, final int pBits, final int pBitIndex, final int pBitCount) throws IllegalArgumentException;

	public long getLongBits(int pIndex, int pLength) throws IllegalArgumentException;

	public byte[] toByteArray();

	public void save(final DataOutputStream pDataOutputStream) throws IOException;

	public void clear();
	public void fill(final byte pByte);
}