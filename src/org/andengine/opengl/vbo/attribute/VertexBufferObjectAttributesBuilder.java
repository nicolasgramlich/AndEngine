package org.andengine.opengl.vbo.attribute;

import org.andengine.util.adt.DataConstants;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:58:05 - 15.08.2011
 */
public class VertexBufferObjectAttributesBuilder {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mIndex;
	private final VertexBufferObjectAttribute[] mVertexBufferObjectAttributes;

	private int mOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VertexBufferObjectAttributesBuilder(final int pCount) {
		this.mVertexBufferObjectAttributes = new VertexBufferObjectAttribute[pCount];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public VertexBufferObjectAttributesBuilder add(final int pLocation, final String pName, final int pSize, final int pType, final boolean pNormalized) {
		this.mVertexBufferObjectAttributes[this.mIndex] = new VertexBufferObjectAttribute(pLocation, pName, pSize, pType, pNormalized, this.mOffset);

		switch(pType) {
			case GLES20.GL_FLOAT:
				this.mOffset += pSize * DataConstants.BYTES_PER_FLOAT;
				break;
			case GLES20.GL_UNSIGNED_BYTE:
				this.mOffset += pSize * DataConstants.BYTES_PER_BYTE;
				break;
			default:
				throw new IllegalArgumentException("Unexpected pType: '" + pType + "'.");
		}

		this.mIndex++;

		return this;
	}

	public VertexBufferObjectAttributes build() {
		return new VertexBufferObjectAttributes(this.mOffset, this.mVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}