package org.anddev.andengine.opengl.vbo;

import java.nio.FloatBuffer;

import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:42:18 - 15.11.2011
 */
public class LowMemoryVertexBufferObject extends VertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final FloatBuffer mFloatBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LowMemoryVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);

		this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
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

	@Override
	protected void onBufferData() {
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.limit(), this.mByteBuffer, this.mUsage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
