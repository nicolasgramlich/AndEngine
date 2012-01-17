package org.andengine.opengl.vbo;

import java.nio.FloatBuffer;

import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/**
 * Compared to a {@link HighPerformanceVertexBufferObject}, the {@link LowMemoryVertexBufferObject} uses <b><u>50%</u> less memory</b>, 
 * at the cost of significantly slower data buffering (<b>up to <u>5x</u> slower!</b>).
 * 
 * Usually a {@link LowMemoryVertexBufferObject} is preferred to a {@link HighPerformanceVertexBufferObject} when the following conditions are met:
 * <ol>
 * 	<li>The applications is close to run out of memory.</li>
 * 	<li>You have very big {@link HighPerformanceVertexBufferObject} or a shitload of small {@link HighPerformanceVertexBufferObject}s, where a 50% memory reduction would actually be significant.</li>
 * 	<li>The content (color, vertices, texturecoordinates) of the {@link LowMemoryVertexBufferObject} is changed not often, or even better: never.</li> 
 * </ol>
 *
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

	public LowMemoryVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);

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
