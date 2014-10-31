package org.andengine.opengl.util;

import java.nio.ByteOrder;

import junit.framework.TestCase;

import org.andengine.util.AssertUtils;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:50:39 - 08.11.2011
 */
public class GLHelperTest extends TestCase {
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

	public void testConvertARGB_8888toRGBA_8888() {
		AssertUtils.assertArrayEquals(new int[] { 0xFE5487CB }, GLHelper.convertARGB_8888toRGBA_8888(new int[] { 0xFECB8754 }, ByteOrder.LITTLE_ENDIAN));
		AssertUtils.assertArrayEquals(new int[] { 0xCB8754FE }, GLHelper.convertARGB_8888toRGBA_8888(new int[] { 0xFECB8754 }, ByteOrder.BIG_ENDIAN));
	}

	public void testConvertARGB_8888toRGBA_4444() {
		AssertUtils.assertArrayEquals(new short[] { (short) 0x5FC8 }, GLHelper.convertARGB_8888toRGBA_4444(new int[] { 0xFECB8754 }, ByteOrder.LITTLE_ENDIAN));
		AssertUtils.assertArrayEquals(new short[] { (short) 0xC85F }, GLHelper.convertARGB_8888toRGBA_4444(new int[] { 0xFECB8754 }, ByteOrder.BIG_ENDIAN));
	}

	public void testConvertARGB_8888toRGBA_565() {
		AssertUtils.assertArrayEquals(new short[] { (short) 0x2ACC }, GLHelper.convertARGB_8888toRGB_565(new int[] { 0xFECB8754 }, ByteOrder.LITTLE_ENDIAN));
		AssertUtils.assertArrayEquals(new short[] { (short) 0xCC2A }, GLHelper.convertARGB_8888toRGB_565(new int[] { 0xFECB8754 }, ByteOrder.BIG_ENDIAN));
	}

	public void testConvertARGB_8888toA_8() {
		final byte[] actual = GLHelper.convertARGB_8888toA_8(new int[] { 0xFECB8754 });

		final byte[] expected = new byte[] { (byte) 0xFE };

		AssertUtils.assertArrayEquals(expected, actual);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
