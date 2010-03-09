package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TiledTexture;

/**
 * @author Nicolas Gramlich
 * @since 19:30:13 - 09.03.2010
 */
public class TiledSprite extends BaseSprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledSprite(final int pX, final int pY, final Texture pTexture) {
		super(pX, pY, pTexture);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return getTexture().getTileWidth();
	}
	
	@Override
	public int getHeight() {
		return getTexture().getTileHeight();
	}
	
	@Override
	public TiledTexture getTexture() {
		return (TiledTexture)super.getTexture();
	}
	
	public void setCurrentTile(final int pTileIndex) {
		getTexture().setCurrentTileIndex(pTileIndex);
	}
	
	public void setCurrentTile(final int pTileColumn, final int pTileRow) {
		getTexture().setCurrentTileIndex(pTileColumn, pTileRow);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
