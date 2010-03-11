package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.buffer.TextureRegionBuffer;
import org.anddev.andengine.opengl.texture.buffer.TiledTextureRegionBuffer;

/**
 * @author Nicolas Gramlich
 * @since 18:14:42 - 09.03.2010
 */
public class TiledTextureRegion extends TextureRegion {
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

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegion(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		super(pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mTileColumns = pTileColumns;
		this.mTileRows = pTileRows;
		this.mCurrentTileColumn = 0;
		this.mCurrentTileRow = 0;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public int getTileCount() {
		return this.mTileColumns * this.mTileRows;
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
		this.mCurrentTileColumn = pTileColumn;
		this.mCurrentTileRow = pTileRow;
		super.updateTextureBuffer();
	}
	
	public void setCurrentTileIndex(final int pTileIndex) {
		if(pTileIndex < this.getTileCount()) {
			this.setCurrentTileIndex(pTileIndex % this.mTileColumns, pTileIndex / this.mTileColumns);
		}
	}

	public float getTexturePositionOfCurrentTileX() {
		return super.getTexturePositionX() + this.mCurrentTileColumn * this.getTileWidth();
	}
	
	public float getTexturePositionOfCurrentTileY() {
		return super.getTexturePositionY() + this.mCurrentTileRow * this.getTileHeight();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public TiledTextureRegion clone() {
		final TiledTextureRegion clone = new TiledTextureRegion(this.getTexturePositionX(), this.getTexturePositionY(), this.getWidth(), this.getHeight(), this.mTileColumns, this.mTileRows);
		clone.setTexture(this.getTexture());
		clone.setCurrentTileIndex(this.mCurrentTileColumn, this.mCurrentTileRow);
		return clone;
	}
	
	@Override
	protected TextureRegionBuffer onCreateTextureRegionBuffer() {
		return new TiledTextureRegionBuffer(this);
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
