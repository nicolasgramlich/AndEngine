package org.andengine.opengl.vbo.attribute;

import org.andengine.opengl.GLES20Fix;

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

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mLocation;
	private final String mName;
	private final int mSize;
	private final int mType;
	private final boolean mNormalized;
	private final int mOffset;

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
		GLES20Fix.glVertexAttribPointer(this.mLocation, this.mSize, this.mType, this.mNormalized, pStride, this.mOffset);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}