package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:14:42 - 09.03.2010
 */
public class TiledTextureRegion extends BaseTextureRegion implements ITiledTextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mTileIndex;
	protected final int mTileCount;
	protected final ITextureRegion[] mTextureRegions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegion(final ITexture pTexture, final ITextureRegion ... pTextureRegions) {
		super(pTexture);

		this.mTextureRegions = pTextureRegions;
		this.mTileCount = this.mTextureRegions.length;
	}

	public static TiledTextureRegion create(final ITexture pTexture, final int pX, final int pY, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final int tileCount = pTileColumns * pTileRows;

		final ITextureRegion[] textureRegions = new ITextureRegion[tileCount];

		final int tileWidth = pWidth / pTileColumns;
		final int tileHeight = pHeight / pTileRows;

		for(int i = 0; i < tileCount; i++) {
			final int tileColumn = i % pTileColumns;
			final int tileRow = i / pTileColumns;

			final int x = pX + tileColumn * tileWidth;
			final int y = pY + tileRow * tileHeight;
			textureRegions[i] = new TextureRegion(pTexture, x, y, tileWidth, tileHeight, false);
		}

		return new TiledTextureRegion(pTexture, textureRegions);
	}

	@Override
	public TiledTextureRegion deepCopy() {
		final int tileCount = this.mTileCount;

		final ITextureRegion[] textureRegions = new ITextureRegion[tileCount];

		for(int i = 0; i < tileCount; i++) {
			if(this.mTextureRegions[i].getTexture() != this.mTexture) {
				throw new IllegalArgumentException("Illegal TextureRegion detected that does not match the Texture passed.");
			}

			textureRegions[i] = this.mTextureRegions[i].deepCopy();
		}

		return new TiledTextureRegion(this.mTexture, textureRegions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getTileIndex() {
		return this.mTileIndex;
	}

	@Override
	public boolean setTileIndex(final int pTileIndex) {
		if(this.mTileIndex != pTileIndex) {
			this.mTileIndex = pTileIndex;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void nextTile() {
		this.mTileIndex++;
		if(this.mTileIndex >= this.mTileCount) {
			this.mTileIndex = this.mTileIndex % this.mTileCount;
		}
	}

	@Override
	public int getTileCount() {
		return this.mTileCount;
	}

	@Override
	public int getX() {
		return this.mTextureRegions[this.mTileIndex].getX();
	}

	@Override
	public int getX(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getX();
	}

	@Override
	public int getY() {
		return this.mTextureRegions[this.mTileIndex].getY();
	}

	@Override
	public int getY(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getY();
	}

	@Override
	public void setX(final int pX) {
		this.mTextureRegions[this.mTileIndex].setX(pX);
	}

	@Override
	public void setX(final int pTileIndex, final int pX) {
		this.mTextureRegions[pTileIndex].setY(pX);
	}

	@Override
	public void setY(final int pY) {
		this.mTextureRegions[this.mTileIndex].setY(pY);
	}

	@Override
	public void setY(final int pTileIndex, final int pY) {
		this.mTextureRegions[pTileIndex].setY(pY);
	}

	@Override
	public void setPosition(final int pX, final int pY) {
		this.mTextureRegions[this.mTileIndex].setPosition(pX, pY);
	}

	@Override
	public void setPosition(final int pTileIndex, final int pX, final int pY) {
		this.mTextureRegions[pTileIndex].setPosition(pX, pY);
	}

	@Override
	public int getWidth() {
		return this.mTextureRegions[this.mTileIndex].getWidth();
	}

	@Override
	public int getWidth(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getWidth();
	}

	@Override
	public int getHeight() {
		return this.mTextureRegions[this.mTileIndex].getHeight();
	}

	@Override
	public int getHeight(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getHeight();
	}

	@Override
	public void setWidth(final int pWidth) {
		this.mTextureRegions[this.mTileIndex].setWidth(pWidth);
	}

	@Override
	public void setWidth(final int pTileIndex, final int pWidth) {
		this.mTextureRegions[pTileIndex].setWidth(pWidth);
	}

	@Override
	public void setHeight(final int pHeight) {
		this.mTextureRegions[this.mTileIndex].setHeight(pHeight);
	}

	@Override
	public void setHeight(final int pTileIndex, final int pHeight) {
		this.mTextureRegions[pTileIndex].setHeight(pHeight);
	}

	@Override
	public void setSize(final int pWidth, final int pHeight) {
		this.mTextureRegions[this.mTileIndex].setSize(pWidth, pHeight);
	}

	@Override
	public void setSize(final int pTileIndex, final int pWidth, final int pHeight) {
		this.mTextureRegions[pTileIndex].setSize(pWidth, pHeight);
	}

	@Override
	public void set(final int pX, final int pY, final int pWidth, final int pHeight) {
		this.mTextureRegions[this.mTileIndex].set(pX, pY, pWidth, pHeight);
	}

	@Override
	public void set(final int pTileIndex, final int pX, final int pY, final int pWidth, final int pHeight) {
		this.mTextureRegions[pTileIndex].set(pX, pY, pWidth, pHeight);
	}

	@Override
	public float getU() {
		return this.mTextureRegions[this.mTileIndex].getU();
	}

	@Override
	public float getU(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getU();
	}

	@Override
	public float getV() {
		return this.mTextureRegions[this.mTileIndex].getV();
	}

	@Override
	public float getV(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getV();
	}

	@Override
	public float getU2() {
		return this.mTextureRegions[this.mTileIndex].getU2();
	}

	@Override
	public float getU2(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getU2();
	}

	@Override
	public float getV2() {
		return this.mTextureRegions[this.mTileIndex].getV2();
	}

	@Override
	public float getV2(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getV2();
	}

	@Override
	public boolean isRotated() {
		return this.mTextureRegions[this.mTileIndex].isRotated();
	}

	@Override
	public boolean isRotated(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].isRotated();
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
