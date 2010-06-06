package org.anddev.andengine.opengl.texture.region.buffer;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 19:01:11 - 09.03.2010
 */
public class TiledTextureRegionBuffer extends TextureRegionBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegionBuffer(final TiledTextureRegion pTextureRegion, final int pDrawType) {
		super(pTextureRegion, pDrawType);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public TiledTextureRegion getTextureRegion() {
		return (TiledTextureRegion)super.getTextureRegion();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected float getX1() {
		final TiledTextureRegion textureRegion = this.getTextureRegion();
		return textureRegion.getTexturePositionOfCurrentTileX() / textureRegion.getTexture().getWidth();
	}

	@Override
	protected float getX2() {
		final TiledTextureRegion textureRegion = this.getTextureRegion();
		return (textureRegion.getTexturePositionOfCurrentTileX() + textureRegion.getTileWidth()) / textureRegion.getTexture().getWidth();
	}

	@Override
	protected float getY1() {
		final TiledTextureRegion textureRegion = this.getTextureRegion();
		return textureRegion.getTexturePositionOfCurrentTileY() / textureRegion.getTexture().getHeight();
	}

	@Override
	protected float getY2() {
		final TiledTextureRegion textureRegion = this.getTextureRegion();
		return (textureRegion.getTexturePositionOfCurrentTileY() + textureRegion.getTileHeight()) / textureRegion.getTexture().getHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
