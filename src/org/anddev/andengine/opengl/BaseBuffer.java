package org.anddev.andengine.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.os.Build;

/**
 * @author Nicolas Gramlich
 * @since 18:54:39 - 09.03.2010
 */
public abstract class BaseBuffer {
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final int BYTES_PER_FLOAT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private final FloatBuffer mFloatBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseBuffer(final int pByteCount) {
		final ByteBuffer byteBuffer = this.allocateByteBuffer(pByteCount);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.mFloatBuffer = byteBuffer.asFloatBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public FloatBuffer getFloatBuffer() {
		return this.mFloatBuffer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private ByteBuffer allocateByteBuffer(final int pByteCount) {
		if(Build.VERSION.SDK_INT == 3) {
			return ByteBuffer.allocate(pByteCount);
		} else {
			return ByteBuffer.allocateDirect(pByteCount);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
