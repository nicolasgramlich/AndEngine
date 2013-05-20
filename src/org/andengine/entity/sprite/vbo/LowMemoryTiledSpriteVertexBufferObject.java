package org.andengine.entity.sprite.vbo;

import java.nio.FloatBuffer;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:39:06 - 28.03.2012
 */
public class LowMemoryTiledSpriteVertexBufferObject extends LowMemorySpriteVertexBufferObject implements ITiledSpriteVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LowMemoryTiledSpriteVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
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
		final FloatBuffer bufferData = this.mFloatBuffer;

		final float packedColor = pTiledSprite.getColor().getABGRPackedFloat();

		final int tileCount = pTiledSprite.getTileCount();
		int bufferDataOffset = 0;
		for (int i = 0; i < tileCount; i++) {
			bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
			bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final TiledSprite pTiledSprite) {
		final FloatBuffer bufferData = this.mFloatBuffer;

		final float width = pTiledSprite.getWidth(); // TODO Optimize with field access?
		final float height = pTiledSprite.getHeight(); // TODO Optimize with field access?

		final int tileCount = pTiledSprite.getTileCount();
		int bufferDataOffset = 0;
		for (int i = 0; i < tileCount; i++) {
			bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, 0);
			bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, 0);

			bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, 0);
			bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, height);

			bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, width);
			bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, 0);

			bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, width);
			bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, 0);

			bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, 0);
			bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, height);

			bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, width);
			bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, height);

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite) {
		final FloatBuffer bufferData = this.mFloatBuffer;

		final ITiledTextureRegion tiledTextureRegion = pTiledSprite.getTiledTextureRegion();

		final int tileCount = pTiledSprite.getTileCount();
		int bufferDataOffset = 0;
		for (int i = 0; i < tileCount; i++) {
			final ITextureRegion textureRegion = tiledTextureRegion.getTextureRegion(i);

			final float u;
			final float v;
			final float u2;
			final float v2;

			if (pTiledSprite.isFlippedVertical()) { // TODO Optimize with field access?
				if (pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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
				if (pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
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

			if (textureRegion.isRotated()) {
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);
			} else {
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);
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