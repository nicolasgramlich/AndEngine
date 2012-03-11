package org.andengine.opengl.vbo.attribute;

import org.andengine.opengl.GLES20Fix;

/**
 * The {@link VertexBufferObjectAttributeFix} is a special 
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:52:33 - 08.03.2012
 */
public class VertexBufferObjectAttributeFix extends VertexBufferObjectAttribute {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public VertexBufferObjectAttributeFix(final int pLocation, final String pName, final int pSize, final int pType, final boolean pNormalized, final int pOffset) {
		super(pLocation, pName, pSize, pType, pNormalized, pOffset);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
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