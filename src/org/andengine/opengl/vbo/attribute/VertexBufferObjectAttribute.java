package org.andengine.opengl.vbo.attribute;

import android.opengl.GLES20;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:57:13 - 15.08.2011
 */
public class VertexBufferObjectAttribute {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	/* package */ final int mLocation;
	/* package */ final String mName;
	/* package */ final int mSize;
	/* package */ final int mType;
	/* package */ final boolean mNormalized;
	/* package */ final int mOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VertexBufferObjectAttribute(final int pLocation, final String pName, final int pSize, final int pType, final boolean pNormalized, final int pOffset) {
		this.mLocation = pLocation;
		this.mName = pName;
		this.mSize = pSize;
		this.mType = pType;
		this.mNormalized = pNormalized;
		this.mOffset = pOffset;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getLocation() {
		return this.mLocation;
	}

	public String getName() {
		return this.mName;
	}

	public int getSize() {
		return this.mSize;
	}

	public int getType() {
		return this.mType;
	}

	public boolean isNormalized() {
		return this.mNormalized;
	}

	public int getOffset() {
		return this.mOffset;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void glVertexAttribPointer(final int pStride) {
		GLES20.glVertexAttribPointer(this.mLocation, this.mSize, this.mType, this.mNormalized, pStride, this.mOffset);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}