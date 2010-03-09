package org.anddev.andengine.opengl.texture.buffer;

import org.anddev.andengine.opengl.texture.TiledTexture;

/**
 * @author Nicolas Gramlich
 * @since 19:01:11 - 09.03.2010
 */
public class TiledTextureBuffer extends TextureBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public TiledTextureBuffer(final TiledTexture pTexture) {
		super(pTexture);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	@Override
	public TiledTexture getTexture() {
		return (TiledTexture)super.getTexture();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected float getX1() {
		final TiledTexture texture = this.getTexture();
		return (float)texture.getAtlasPositionOfCurrentTileX() / texture.getTextureAtlas().getWidth();
	}
	
	@Override
	protected float getX2() {
		final TiledTexture texture = this.getTexture();
		return (float)(texture.getAtlasPositionOfCurrentTileX() + texture.getTileWidth()) / texture.getTextureAtlas().getWidth();
	}
	
	@Override
	protected float getY1() {
		final TiledTexture texture = this.getTexture();
		return (float)texture.getAtlasPositionOfCurrentTileY() / texture.getTextureAtlas().getHeight();
	}
	
	@Override
	protected float getY2() {
		final TiledTexture texture = this.getTexture();
		return (float)(texture.getAtlasPositionOfCurrentTileY() + texture.getTileHeight()) / texture.getTextureAtlas().getHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
