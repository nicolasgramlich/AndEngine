package org.anddev.andengine.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.Build;

/**
 * @author Nicolas Gramlich
 * @since 18:54:39 - 09.03.2010
 */
public abstract class BaseBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ByteBuffer mByteBuffer;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseBuffer() {
		this.mByteBuffer = allocateByteBuffer();
		this.mByteBuffer.order(ByteOrder.nativeOrder());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public ByteBuffer getByteBuffer() {
		return this.mByteBuffer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private ByteBuffer allocateByteBuffer() {
		if(Build.VERSION.SDK_INT == 3) {
			return ByteBuffer.allocate(8*4);
		} else {
			return ByteBuffer.allocateDirect(8*4);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
