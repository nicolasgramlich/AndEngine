package org.andengine.opengl.util;

import java.nio.ByteBuffer;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:06:51 - 11.08.2011
 */
public class BufferUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	static {
		System.loadLibrary("andengine");
	}

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

	public static ByteBuffer allocateDirect(final int pCapacity) {
		return BufferUtils.jniAllocateDirect(pCapacity);
	}

	public static void freeDirect(final ByteBuffer pByteBuffer) {
		BufferUtils.jniFreeDirect(pByteBuffer);
	}

	private native static ByteBuffer jniAllocateDirect(final int pCapacity);
	private native static void jniFreeDirect(final ByteBuffer pByteBuffer);

	/**
	 * @param pByteBuffer must be a direct Buffer.
	 * @param pSource
	 * @param pLength to copy in pSource.
	 * @param pOffset in pSource.
	 */
	public static void put(final ByteBuffer pByteBuffer, final float[] pSource, final int pLength, final int pOffset) {
		BufferUtils.jniPut(pByteBuffer, pSource, pLength, pOffset);
		pByteBuffer.position(0);
		pByteBuffer.limit(pLength << 2);
	}

	private native static void jniPut(final ByteBuffer pByteBuffer, final float[] pSource, final int pLength, final int pOffset);

	public static short getUnsignedByte(final ByteBuffer pByteBuffer) {
		return (short) (pByteBuffer.get() & 0xFF);
	}

	public static void putUnsignedByte(final ByteBuffer pByteBuffer, final int pValue) {
		pByteBuffer.put((byte) (pValue & 0xFF));
	}

	public static short getUnsignedByte(final ByteBuffer pByteBuffer, final int pPosition) {
		return (short) (pByteBuffer.get(pPosition) & (short) 0xFF);
	}

	public static void putUnsignedByte(final ByteBuffer pByteBuffer, final int pPosition, final int pValue) {
		pByteBuffer.put(pPosition, (byte) (pValue & 0xFF));
	}

	public static int getUnsignedShort(final ByteBuffer pByteBuffer) {
		return pByteBuffer.getShort() & 0xFFFF;
	}

	public static void putUnsignedShort(final ByteBuffer pByteBuffer, final int pValue) {
		pByteBuffer.putShort((short) (pValue & 0xFFFF));
	}

	public static int getUnsignedShort(final ByteBuffer pByteBuffer, final int pPosition) {
		return pByteBuffer.getShort(pPosition) & 0xFFFF;
	}

	public static void putUnsignedShort(final ByteBuffer pByteBuffer, final int pPosition, final int pValue) {
		pByteBuffer.putShort(pPosition, (short) (pValue & 0xFFFF));
	}

	public static long getUnsignedInt(final ByteBuffer pByteBuffer) {
		return pByteBuffer.getInt() & 0xFFFFFFFFL;
	}

	public static void putUnsignedInt(final ByteBuffer pByteBuffer, final long pValue) {
		pByteBuffer.putInt((int) (pValue & 0xFFFFFFFFL));
	}

	public static long getUnsignedInt(final ByteBuffer pByteBuffer, final int pPosition) {
		return pByteBuffer.getInt(pPosition) & 0xFFFFFFFFL;
	}

	public static void putUnsignedInt(final ByteBuffer pByteBuffer, final int pPosition, final long pValue) {
		pByteBuffer.putInt(pPosition, (short) (pValue & 0xFFFFFFFFL));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
