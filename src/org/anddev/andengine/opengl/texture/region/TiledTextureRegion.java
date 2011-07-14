package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:14:42 - 09.03.2010
 */
public class TiledTextureRegion extends BaseTextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mTileColumns;
	private final int mTileRows;
	private int mCurrentTileColumn;
	private int mCurrentTileRow;
	private final int mTileCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegion(final ITexture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		super(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mTileColumns = pTileColumns;
		this.mTileRows = pTileRows;
		this.mTileCount = this.mTileColumns * this.mTileRows;
		this.mCurrentTileColumn = 0;
		this.mCurrentTileRow = 0;

		this.initTextureBuffer();
	}

	@Override
	protected void initTextureBuffer() {
		if(this.mTileRows != 0 && this.mTileColumns != 0) {
			super.initTextureBuffer();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getTileCount() {
		return this.mTileCount;
	}

	public int getTileWidth() {
		return super.getWidth() / this.mTileColumns;
	}

	public int getTileHeight() {
		return super.getHeight() / this.mTileRows;
	}

	public int getCurrentTileColumn() {
		return this.mCurrentTileColumn;
	}

	public int getCurrentTileRow() {
		return this.mCurrentTileRow;
	}

	public int getCurrentTileIndex() {
		return this.mCurrentTileRow * this.mTileColumns + this.mCurrentTileColumn;
	}

	public void setCurrentTileIndex(final int pTileColumn, final int pTileRow) {
		if(pTileColumn != this.mCurrentTileColumn || pTileRow != this.mCurrentTileRow) {
			this.mCurrentTileColumn = pTileColumn;
			this.mCurrentTileRow = pTileRow;
			super.updateTextureRegionBuffer();
		}
	}

	public void setCurrentTileIndex(final int pTileIndex) {
		if(pTileIndex < this.mTileCount) {
			final int tileColumns = this.mTileColumns;
			this.setCurrentTileIndex(pTileIndex % tileColumns, pTileIndex / tileColumns);
		}
	}

	public int getTexturePositionOfCurrentTileX() {
		return super.getTexturePositionX() + this.mCurrentTileColumn * this.getTileWidth();
	}

	public int getTexturePositionOfCurrentTileY() {
		return super.getTexturePositionY() + this.mCurrentTileRow * this.getTileHeight();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public TiledTextureRegion clone() {
		final TiledTextureRegion clone = new TiledTextureRegion(this.mTexture, this.getTexturePositionX(), this.getTexturePositionY(), this.getWidth(), this.getHeight(), this.mTileColumns, this.mTileRows);
		clone.setCurrentTileIndex(this.mCurrentTileColumn, this.mCurrentTileRow);
		return clone;
	}

	@Override
	public float getTextureCoordinateX1() {
		return (float)this.getTexturePositionOfCurrentTileX() / this.mTexture.getWidth();
	}

	@Override
	public float getTextureCoordinateY1() {
		return (float)this.getTexturePositionOfCurrentTileY() / this.mTexture.getHeight();
	}

	@Override
	public float getTextureCoordinateX2() {
		return (float)(this.getTexturePositionOfCurrentTileX() + this.getTileWidth()) / this.mTexture.getWidth();
	}

	@Override
	public float getTextureCoordinateY2() {
		return (float)(this.getTexturePositionOfCurrentTileY() + this.getTileHeight()) / this.mTexture.getHeight();
	}
	
	@Override
	public int getTextureCropLeft() {
		return this.getTexturePositionOfCurrentTileX();
	}

	@Override
	public int getTextureCropTop() {
		return this.getTexturePositionOfCurrentTileY();
	}

	@Override
	public int getTextureCropWidth() {
		return this.getTileWidth();
	}

	@Override
	public int getTextureCropHeight() {
		return this.getTileHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void nextTile() {
		final int tileIndex = (this.getCurrentTileIndex() + 1) % this.getTileCount();
		this.setCurrentTileIndex(tileIndex);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
