package org.andengine.util.adt.bit;

import junit.framework.TestCase;

import org.andengine.util.AssertUtils;

/**
 * @author Nicolas Gramlich
 * @since 22:31:38 - 16.09.2010
 */
public class BitVectorTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void testGetBit() {
		final BitVector bitVector = new BitVector(new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF});
		
		assertEquals(false, bitVector.getBit(0));
		assertEquals(false, bitVector.getBit(1));
		assertEquals(false, bitVector.getBit(2));
		assertEquals(false, bitVector.getBit(3));
		assertEquals(false, bitVector.getBit(4));
		assertEquals(false, bitVector.getBit(5));
		assertEquals(false, bitVector.getBit(6));
		assertEquals(true, bitVector.getBit(7));
		
		assertEquals(false, bitVector.getBit(8));
		assertEquals(false, bitVector.getBit(9));
		assertEquals(false, bitVector.getBit(10));
		assertEquals(false, bitVector.getBit(11));
		assertEquals(false, bitVector.getBit(12));
		assertEquals(false, bitVector.getBit(13));
		assertEquals(true, bitVector.getBit(14));
		assertEquals(false, bitVector.getBit(15));
		
		assertEquals(false, bitVector.getBit(16));
		assertEquals(false, bitVector.getBit(17));
		assertEquals(false, bitVector.getBit(18));
		assertEquals(false, bitVector.getBit(19));
		assertEquals(false, bitVector.getBit(20));
		assertEquals(true, bitVector.getBit(21));
		assertEquals(false, bitVector.getBit(22));
		assertEquals(false, bitVector.getBit(23));
		
		assertEquals(false, bitVector.getBit(24));
		assertEquals(false, bitVector.getBit(25));
		assertEquals(false, bitVector.getBit(26));
		assertEquals(false, bitVector.getBit(27));
		assertEquals(true, bitVector.getBit(28));
		assertEquals(false, bitVector.getBit(29));
		assertEquals(false, bitVector.getBit(30));
		assertEquals(false, bitVector.getBit(31));
		
		assertEquals(false, bitVector.getBit(32));
		assertEquals(false, bitVector.getBit(33));
		assertEquals(false, bitVector.getBit(34));
		assertEquals(true, bitVector.getBit(35));
		assertEquals(false, bitVector.getBit(36));
		assertEquals(false, bitVector.getBit(37));
		assertEquals(false, bitVector.getBit(38));
		assertEquals(false, bitVector.getBit(39));
		
		assertEquals(false, bitVector.getBit(40));
		assertEquals(false, bitVector.getBit(41));
		assertEquals(true, bitVector.getBit(42));
		assertEquals(false, bitVector.getBit(43));
		assertEquals(false, bitVector.getBit(44));
		assertEquals(false, bitVector.getBit(45));
		assertEquals(false, bitVector.getBit(46));
		assertEquals(false, bitVector.getBit(47));
		
		assertEquals(false, bitVector.getBit(48));
		assertEquals(true, bitVector.getBit(49));
		assertEquals(false, bitVector.getBit(50));
		assertEquals(false, bitVector.getBit(51));
		assertEquals(false, bitVector.getBit(52));
		assertEquals(false, bitVector.getBit(53));
		assertEquals(false, bitVector.getBit(54));
		assertEquals(false, bitVector.getBit(55));
		
		assertEquals(true, bitVector.getBit(56));
		assertEquals(false, bitVector.getBit(57));
		assertEquals(false, bitVector.getBit(58));
		assertEquals(false, bitVector.getBit(59));
		assertEquals(false, bitVector.getBit(60));
		assertEquals(false, bitVector.getBit(61));
		assertEquals(false, bitVector.getBit(62));
		assertEquals(false, bitVector.getBit(63));
		
		assertEquals(true, bitVector.getBit(64));
		assertEquals(true, bitVector.getBit(65));
		assertEquals(true, bitVector.getBit(66));
		assertEquals(true, bitVector.getBit(67));
		assertEquals(true, bitVector.getBit(68));
		assertEquals(true, bitVector.getBit(69));
		assertEquals(true, bitVector.getBit(70));
		assertEquals(true, bitVector.getBit(71));
	}

	public void testGetByte() {
		final BitVector bitVector = new BitVector(new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF});
		
		assertEquals((byte)0x01, bitVector.getByte(0));
		assertEquals((byte)0x02, bitVector.getByte(8));
		assertEquals((byte)0x04, bitVector.getByte(16));
		assertEquals((byte)0x08, bitVector.getByte(24));
		assertEquals((byte)0x10, bitVector.getByte(32));
		assertEquals((byte)0x20, bitVector.getByte(40));
		assertEquals((byte)0x40, bitVector.getByte(48));
		assertEquals((byte)0x80, bitVector.getByte(56));
		assertEquals((byte)0xFF, bitVector.getByte(64));
	}
	
	
	public void testGetByteUneven() {
		final BitVector bitVector = new BitVector(new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xF0, (byte)0x0F});
		
		assertEquals((byte)0xE0, bitVector.getByte(57));
		assertEquals((byte)0xC0, bitVector.getByte(58));
		assertEquals((byte)0x80, bitVector.getByte(59));
		assertEquals((byte)0x00, bitVector.getByte(60));
		assertEquals((byte)0x01, bitVector.getByte(61));
		assertEquals((byte)0x03, bitVector.getByte(62));
		assertEquals((byte)0x07, bitVector.getByte(63));
	}

	public void testGetShort() {
		final BitVector bitVector = new BitVector(new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF, (byte)0xFF});
		
		assertEquals((short)0x0102, bitVector.getShort(0));
		assertEquals((short)0x0408, bitVector.getShort(16));
		assertEquals((short)0x1020, bitVector.getShort(32));
		assertEquals((short)0x4080, bitVector.getShort(48));
		assertEquals((short)0xFFFF, bitVector.getShort(64));
	}

	public void testGetInt() {
		final BitVector bitVector = new BitVector(new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});
		
		assertEquals(0x01020408, bitVector.getInt(0));
		assertEquals(0x10204080, bitVector.getInt(32));
		assertEquals(0xFFFFFFFF, bitVector.getInt(64));
	}

	public void testGetLong() {
		final BitVector bitVector = new BitVector(new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});
		
		assertEquals(0x0102040810204080L, bitVector.getLong(0));
		assertEquals(0xFFFFFFFFFFFFFFFFL, bitVector.getLong(64));
	}
	
	public void testToByteArray() {
		final byte[] bytes = new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
		final BitVector bitVector = new BitVector(bytes);
		
		AssertUtils.assertArrayEquals(bytes, bitVector.toByteArray());
	}
	
	public void testToByteArrayUneven() {
		final byte[] bytes = new byte[]{(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFE, (byte)0xFF, (byte)0xFF, (byte)0x23, (byte)0xAC};
		final BitVector bitVector = new BitVector(bytes);
		
		AssertUtils.assertArrayEquals(bytes, bitVector.toByteArray());
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
