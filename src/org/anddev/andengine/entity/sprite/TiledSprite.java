package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.TiledTextureRegion;

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

	public TiledSprite(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion.getTileWidth(), pTiledTextureRegion.getTileHeight(), pTiledTextureRegion);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public TiledTextureRegion getTextureRegion() {
		return (TiledTextureRegion)super.getTextureRegion();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void setCurrentTileIndex(final int pTileIndex) {
		getTextureRegion().setCurrentTileIndex(pTileIndex);
	}
	
	public void setCurrentTileIndex(final int pTileColumn, final int pTileRow) {
		getTextureRegion().setCurrentTileIndex(pTileColumn, pTileRow);
	}
	
	public void nextTile() {
		getTextureRegion().nextTile();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
