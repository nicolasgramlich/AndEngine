package org.anddev.andengine.opengl.util;

import java.nio.Buffer;
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

	private native static void jniPut(final Buffer pBuffer, final float[] pSource, final int pLength, final int pOffset);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
