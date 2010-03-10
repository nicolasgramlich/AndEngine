package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.buffer.TextureBuffer;
import org.anddev.andengine.opengl.texture.buffer.TiledTextureBuffer;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 18:14:42 - 09.03.2010
 */
public class TiledTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mTileColumns;
	private final int mTileRows;
	private int mCurrentTileColumn; // TODO Think if it is smarter to have the Sprites handle the index, so multiple Sprites can have the same texture but different tile-indexes. 
	private int mCurrentTileRow;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTexture(final ITextureSource pTextureSource, final int pTileColumns, final int pTileRows) {
		super(pTextureSource);
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
			this.setCurrentTileIndex(pTileIndex / this.mTileColumns, pTileIndex % this.mTileColumns);
		}
	}

	public float getAtlasPositionOfCurrentTileX() {
		return super.getAtlasPositionX() + this.mCurrentTileColumn * this.getTileWidth();
	}
	
	public float getAtlasPositionOfCurrentTileY() {
		return super.getAtlasPositionY() + this.mCurrentTileRow * this.getTileHeight();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected TextureBuffer onCreateTextureBuffer() {
		return new TiledTextureBuffer(this);
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
