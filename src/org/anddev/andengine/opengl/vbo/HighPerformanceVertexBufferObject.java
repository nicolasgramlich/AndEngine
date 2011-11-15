package org.anddev.andengine.opengl.vbo;

import org.anddev.andengine.opengl.util.BufferUtils;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:42:18 - 15.11.2011
 */
public class HighPerformanceVertexBufferObject extends VertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float[] mBufferData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);

		this.mBufferData = new float[pCapacity];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getBufferData() {
		return this.mBufferData;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onBufferData() {
		// TODO On honeycomb the nio buffers are significantly faster, and below native call might not be needed!
//		this.mFloatBuffer.position(0);
//		this.mFloatBuffer.put(this.mBufferData);
//		this.mFloatBuffer.position(0);
		BufferUtils.put(this.mByteBuffer, this.mBufferData, this.mBufferData.length, 0);

		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.limit(), this.mByteBuffer, this.mUsage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
