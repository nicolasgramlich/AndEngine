package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.vbo.HighPerformanceVertexBufferObject;

/**
 * Unlike {@link Sprite}, the {@link DiamondSprite} class doesn't render the rectangular outline of a {@link ITextureRegion}, but cuts out a diamond.
 * <pre>
 * +--------+
 * |   /\   |
 * |  /  \  |
 * | /    \ |
 * |/      \|
 * |\      /|
 * | \    / |
 * |  \  /  |
 * |   \/   |
 * +--------+
 * </pre>
 *
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:26:32 - 24.10.2011
 */
public class DiamondSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateVertices() {
		final HighPerformanceVertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float x = 0;
		final float y = 0;
		final float x2 = this.mWidth;
		final float y2 = this.mHeight;

		final float xCenter = (x + x2) * 0.5f;
		final float yCenter = (y + y2) * 0.5f;

		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
		bufferData[0 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = yCenter;

		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = xCenter;
		bufferData[1 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = xCenter;
		bufferData[2 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
		bufferData[3 * Sprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = yCenter;

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateTextureCoordinates() {
		final HighPerformanceVertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final ITextureRegion textureRegion = this.mTextureRegion;

		final float u;
		final float v;
		final float u2;
		final float v2;

		if(this.mFlippedVertical) {
			if(this.mFlippedHorizontal) {
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
			if(this.mFlippedHorizontal) {
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

		final float uCenter = (u + u2) * 0.5f;
		final float vCenter = (v + v2) * 0.5f;

		if(textureRegion.isRotated()) {
			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = vCenter;

			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = vCenter;

			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
		} else {
			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = vCenter;

			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
			bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = uCenter;
			bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = vCenter;
		}

		vertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
