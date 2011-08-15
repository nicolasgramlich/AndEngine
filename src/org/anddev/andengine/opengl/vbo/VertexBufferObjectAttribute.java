package org.anddev.andengine.opengl.vbo;

import org.anddev.andengine.opengl.GLES20Fix;
import org.anddev.andengine.opengl.shader.ShaderProgram;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:57:13 - 15.08.2011
 */
public class VertexBufferObjectAttribute {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LOCATION_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private int mLocation = VertexBufferObjectAttribute.LOCATION_INVALID;

	private final String mName;
	private final int mSize;
	private final int mType;
	private final boolean mNormalized;
	private final int mOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VertexBufferObjectAttribute(final String pName, final int pSize, final int pType, final boolean pNormalized, final int pOffset) {
		this.mName = pName;
		this.mSize = pSize;
		this.mType = pType;
		this.mNormalized = pNormalized;
		this.mOffset = pOffset;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void enable(final ShaderProgram pShaderProgram, final int pStride) {
		if(this.mLocation == VertexBufferObjectAttribute.LOCATION_INVALID) {
			this.mLocation = pShaderProgram.getAttributeLocation(this.mName);
		}

		GLES20.glEnableVertexAttribArray(this.mLocation);
		GLES20Fix.glVertexAttribPointer(this.mLocation, this.mSize, this.mType, this.mNormalized, pStride, this.mOffset);
	}

	public void disable(final ShaderProgram pShaderProgram) {
		GLES20.glDisableVertexAttribArray(this.mLocation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}