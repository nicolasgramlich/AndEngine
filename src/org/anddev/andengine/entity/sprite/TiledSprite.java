package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public TiledSprite(final float pX, final float pY, final float pTileWidth, final float pTileHeight, final TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
	}

	public TiledSprite(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pTiledTextureRegion.getTileWidth(), pTiledTextureRegion.getTileHeight(), pTiledTextureRegion, pRectangleVertexBuffer);
	}

	public TiledSprite(final float pX, final float pY, final float pTileWidth, final float pTileHeight, final TiledTextureRegion pTiledTextureRegion, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion, pRectangleVertexBuffer);
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

	public int getCurrentTileIndex() {
		return this.getTextureRegion().getCurrentTileIndex();
	}

	public void setCurrentTileIndex(final int pTileIndex) {
		this.getTextureRegion().setCurrentTileIndex(pTileIndex);
	}

	public void setCurrentTileIndex(final int pTileColumn, final int pTileRow) {
		this.getTextureRegion().setCurrentTileIndex(pTileColumn, pTileRow);
	}

	public void nextTile() {
		this.getTextureRegion().nextTile();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
