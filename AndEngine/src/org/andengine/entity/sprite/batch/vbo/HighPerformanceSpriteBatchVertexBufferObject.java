package org.andengine.entity.sprite.batch.vbo;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:35:28 - 29.03.2012
 */
public class HighPerformanceSpriteBatchVertexBufferObject extends HighPerformanceVertexBufferObject implements ISpriteBatchVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mBufferDataOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceSpriteBatchVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getBufferDataOffset() {
		return this.mBufferDataOffset;
	}

	@Override
	public void setBufferDataOffset(final int pBufferDataOffset) {
		this.mBufferDataOffset = pBufferDataOffset;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * 1-3
	 * |X|
	 * 2-4
	 */
	@Override
	public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pColorABGRPackedInt) {
		final float[] bufferData = this.getBufferData();
		final int bufferDataOffset = this.mBufferDataOffset;

		final float x1 = pX1;
		final float y1 = pY1;
		final float x2 = pX2;
		final float y2 = pY2;
		final float x3 = pX3;
		final float y3 = pY3;
		final float x4 = pX4;
		final float y4 = pY4;
		final float u = pTextureRegion.getU();
		final float v = pTextureRegion.getV();
		final float u2 = pTextureRegion.getU2();
		final float v2 = pTextureRegion.getV2();

		if(pTextureRegion.isRotated()) {
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
		} else {
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x3;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y3;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x4;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y4;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
		}

		this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
	}


	/**
	 * 1-+
	 * |X|
	 * +-2
	 */
	@Override
	public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pColorABGRPackedInt) {
		final float[] bufferData = this.getBufferData();
		final int bufferDataOffset = this.mBufferDataOffset;

		final float x1 = pX1;
		final float y1 = pY1;
		final float x2 = pX2;
		final float y2 = pY2;
		final float u = pTextureRegion.getU();
		final float v = pTextureRegion.getV();
		final float u2 = pTextureRegion.getU2();
		final float v2 = pTextureRegion.getV2();

		if(pTextureRegion.isRotated()) {
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
		} else {
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 0 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX] = pColorABGRPackedInt;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V] = v2;
		}

		this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}