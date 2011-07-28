package org.anddev.andengine.opengl.texture.region.buffer;

import org.anddev.andengine.opengl.buffer.BufferObject;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.opengl.vertex.SpriteBatchVertexBuffer;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:32:14 - 14.06.2011
 */
public class SpriteBatchTextureRegionBuffer extends BufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mIndex;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatchTextureRegionBuffer(final int pCapacity, final int pDrawType, final boolean pManaged) {
		super(pCapacity * 2 * SpriteBatchVertexBuffer.VERTICES_PER_RECTANGLE, pDrawType, pManaged);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getIndex() {
		return this.mIndex;
	}

	public void setIndex(final int pIndex) {
		this.mIndex = pIndex;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void add(final BaseTextureRegion pTextureRegion) {
		final ITexture texture = pTextureRegion.getTexture();

		if(texture == null) { // TODO Check really needed?
			return;
		}

		final int x1 = Float.floatToRawIntBits(pTextureRegion.getTextureCoordinateX1());
		final int y1 = Float.floatToRawIntBits(pTextureRegion.getTextureCoordinateY1());
		final int x2 = Float.floatToRawIntBits(pTextureRegion.getTextureCoordinateX2());
		final int y2 = Float.floatToRawIntBits(pTextureRegion.getTextureCoordinateY2());

		final int[] bufferData = this.mBufferData;

		int index = this.mIndex;
		bufferData[index++] = x1;
		bufferData[index++] = y1;

		bufferData[index++] = x1;
		bufferData[index++] = y2;

		bufferData[index++] = x2;
		bufferData[index++] = y1;

		bufferData[index++] = x2;
		bufferData[index++] = y1;

		bufferData[index++] = x1;
		bufferData[index++] = y2;

		bufferData[index++] = x2;
		bufferData[index++] = y2;
		this.mIndex = index;
	}

	public void submit() {
		final FastFloatBuffer buffer = this.mFloatBuffer;
		buffer.position(0);
		buffer.put(this.mBufferData);
		buffer.position(0);

		super.setHardwareBufferNeedsUpdate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
