package org.andengine.opengl.util;

import java.nio.ByteBuffer;

import org.andengine.util.adt.DataConstants;
import org.andengine.util.adt.map.Library;
import org.andengine.util.debug.Debug;
import org.andengine.util.system.SystemUtils;

import android.os.Build;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:06:51 - 11.08.2011
 * @see http://code.google.com/p/android/issues/detail?id=11078
 * @see http://code.google.com/p/android/issues/detail?id=16941
 */
public class BufferUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final boolean NATIVE_LIB_LOADED;

	/** Android issue 16941. */
	private static final boolean WORKAROUND_BYTEBUFFER_PUT_FLOATARRAY;
	/** Android issue 16941. */
	private static final boolean WORKAROUND_BYTEBUFFER_ALLOCATE_DIRECT;

	static {
		boolean loadLibrarySuccess;
		try {
			System.loadLibrary("andengine");
			loadLibrarySuccess = true;
		} catch (final UnsatisfiedLinkError pUnsatisfiedLinkError) {
			loadLibrarySuccess = false;
		}
		NATIVE_LIB_LOADED = loadLibrarySuccess;

		if(NATIVE_LIB_LOADED) {
			if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
				WORKAROUND_BYTEBUFFER_ALLOCATE_DIRECT = true;
			} else {
				WORKAROUND_BYTEBUFFER_ALLOCATE_DIRECT = false;
			}

			if(SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO)) {
				WORKAROUND_BYTEBUFFER_PUT_FLOATARRAY = true;
			} else {
				WORKAROUND_BYTEBUFFER_PUT_FLOATARRAY = false;
			}
		} else {
			WORKAROUND_BYTEBUFFER_ALLOCATE_DIRECT = false;
			if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
				Debug.w("Creating a " + ByteBuffer.class.getSimpleName() + " will actually allocate 4x the memory than requested!");
			}

			WORKAROUND_BYTEBUFFER_PUT_FLOATARRAY = false;
		}
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

	public static ByteBuffer allocateDirectByteBuffer(final int pCapacity) {
		if(BufferUtils.WORKAROUND_BYTEBUFFER_ALLOCATE_DIRECT) {
			return BufferUtils.allocateDirect(pCapacity * DataConstants.BYTES_PER_FLOAT);
		} else {
			return ByteBuffer.allocateDirect(pCapacity * DataConstants.BYTES_PER_FLOAT);
		}
	}

	public static ByteBuffer allocateDirect(final int pCapacity) {
		return BufferUtils.jniAllocateDirect(pCapacity);
	}

	public static void freeDirectByteBuffer(final ByteBuffer pByteBuffer) {
		if(BufferUtils.WORKAROUND_BYTEBUFFER_ALLOCATE_DIRECT) {
			BufferUtils.freeDirect(pByteBuffer);
		}
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
		if(BufferUtils.WORKAROUND_BYTEBUFFER_PUT_FLOATARRAY) {
			BufferUtils.jniPut(pByteBuffer, pSource, pLength, pOffset);
		} else {
			pByteBuffer.put(pSource, );
		}
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
