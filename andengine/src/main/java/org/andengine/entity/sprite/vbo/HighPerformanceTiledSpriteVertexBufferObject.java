package org.andengine.entity.sprite.vbo;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:39:06 - 28.03.2012
 */
public class HighPerformanceTiledSpriteVertexBufferObject extends HighPerformanceSpriteVertexBufferObject implements ITiledSpriteVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceTiledSpriteVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(final TiledSprite pTiledSprite) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pTiledSprite.getColor().getABGRPackedFloat();

		final int tileCount = pTiledSprite.getTileCount();
		int bufferDataOffset = 0;
		for(int i = 0; i < tileCount; i++) {
			bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final TiledSprite pTiledSprite) {
		final float[] bufferData = this.mBufferData;

		final float x = 0;
		final float y = 0;
		final float x2 = pTiledSprite.getWidth(); // TODO Optimize with field access?
		final float y2 = pTiledSprite.getHeight(); // TODO Optimize with field access?

		final int tileCount = pTiledSprite.getTileCount();
		int bufferDataOffset = 0;
		for(int i = 0; i < tileCount; i++) {
			bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
			bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

			bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
			bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

			bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

			bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

			bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
			bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

			bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite) {
		final float[] bufferData = this.mBufferData;

		final ITiledTextureRegion tiledTextureRegion = pTiledSprite.getTiledTextureRegion();

		final int tileCount = pTiledSprite.getTileCount();
		int bufferDataOffset = 0;
		for(int i = 0; i < tileCount; i++) {
			final ITextureRegion textureRegion = tiledTextureRegion.getTextureRegion(i);

			final float u;
			final float v;
			final float u2;
			final float v2;

			if(pTiledSprite.isFlippedVertical()) { // TODO Optimize with field access?
				if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
					u = textureRegion.getU2();
					u2 = textureRegion.getU();
					v = textureRegion.getV2();
					v2 = textureRegion.getV();
				} else {
					u = textureRegion.getU();
					u2 = textureRegion.getU2();
					v = textureRegion.getV2();
					v2 = textureRegion.getV();
				}
			} else {
				if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
					u = textureRegion.getU2();
					u2 = textureRegion.getU();
					v = textureRegion.getV();
					v2 = textureRegion.getV2();
				} else {
					u = textureRegion.getU();
					u2 = textureRegion.getU2();
					v = textureRegion.getV();
					v2 = textureRegion.getV2();
				}
			}

			if(textureRegion.isRotated()) {
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}