package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.TextureRegion;
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

	public TiledSprite(final int pX, final int pY, final TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return getTextureRegion().getTileWidth();
	}
	
	@Override
	public int getHeight() {
		return getTextureRegion().getTileHeight();
	}
	
	@Override
	public TiledTextureRegion getTextureRegion() {
		return (TiledTextureRegion)super.getTextureRegion();
	}
	
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
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
