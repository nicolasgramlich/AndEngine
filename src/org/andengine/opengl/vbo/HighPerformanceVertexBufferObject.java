package org.andengine.opengl.vbo;

import java.nio.FloatBuffer;

import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.system.SystemUtils;

import android.opengl.GLES20;

/**
 * Compared to a {@link LowMemoryVertexBufferObject}, the {@link HighPerformanceVertexBufferObject} uses <b><u>2x</u> the heap memory</b>,
 * at the benefit of significantly faster data buffering (<b>up to <u>5x</u> faster!</b>).
 *
 * @see {@link LowMemoryVertexBufferObject} when to prefer a {@link LowMemoryVertexBufferObject} instead of a {@link HighPerformanceVertexBufferObject}
 *
 * <p>(c) 2011 Zynga Inc.</p>
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
	protected final FloatBuffer mFloatBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);

		this.mBufferData = new float[pCapacity];
		if (SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
			this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
		} else {
			this.mFloatBuffer = null;
		}
	}

	public HighPerformanceVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final float[] pBufferData, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pBufferData.length, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
		this.mBufferData = pBufferData;

		if (SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
			this.mFloatBuffer = this.mByteBuffer.asFloatBuffer();
		} else {
			this.mFloatBuffer = null;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getBufferData() {
		return this.mBufferData;
	}

	@Override
	public int getHeapMemoryByteSize() {
		return this.getByteCapacity();
	}

	@Override
	public int getNativeHeapMemoryByteSize() {
		return this.getByteCapacity();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onBufferData() {
		// TODO Check if, and how mow this condition affects performance.
		if (SystemUtils.SDK_VERSION_HONEYCOMB_OR_LATER) {
			// TODO Check if this is similar fast or faster than the non Honeycomb codepath.
			this.mFloatBuffer.position(0);
			this.mFloatBuffer.put(this.mBufferData);

			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.capacity(), this.mByteBuffer, this.mUsage);
		} else {
			BufferUtils.put(this.mByteBuffer, this.mBufferData, this.mBufferData.length, 0);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, this.mByteBuffer.limit(), this.mByteBuffer, this.mUsage);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
