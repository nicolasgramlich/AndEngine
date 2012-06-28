package org.andengine.opengl.texture.region;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.Texture;

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

	protected int mCurrentTileIndex;
	protected final int mTileCount;
	protected final ITextureRegion[] mTextureRegions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegion(final ITexture pTexture, final ITextureRegion ... pTextureRegions) {
		this(pTexture, true, pTextureRegions);
	}

	/**
	 * @param pTexture
	 * @param pPerformSameTextureSanityCheck checks whether all supplied {@link ITextureRegion} are on the same {@link Texture}
	 * @param pTextureRegions
	 */
	public TiledTextureRegion(final ITexture pTexture, final boolean pPerformSameTextureSanityCheck, final ITextureRegion ... pTextureRegions) {
		super(pTexture);

		this.mTextureRegions = pTextureRegions;
		this.mTileCount = this.mTextureRegions.length;

		if(pPerformSameTextureSanityCheck) {
			for(int i = this.mTileCount - 1; i >= 0; i--) {
				if(pTextureRegions[i].getTexture() != pTexture) {
					throw new IllegalArgumentException("The " + ITextureRegion.class.getSimpleName() + ": '" + pTextureRegions[i].toString() + "' at index: '" + i + "' is not on the same " + ITexture.class.getSimpleName() + ": '" + pTextureRegions[i].getTexture().toString() + "' as the supplied " + ITexture.class.getSimpleName() + ": '" + pTexture.toString() + "'.");
				}
			}
		}
	}

	public static TiledTextureRegion create(final ITexture pTexture, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final int pTileColumns, final int pTileRows) {
		return TiledTextureRegion.create(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, pTileColumns, pTileRows, false);
	}

	public static TiledTextureRegion create(final ITexture pTexture, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final int pTileColumns, final int pTileRows, final boolean pRotated) {
		final ITextureRegion[] textureRegions = new ITextureRegion[pTileColumns * pTileRows];

		final int tileWidth = pTextureWidth / pTileColumns;
		final int tileHeight = pTextureHeight / pTileRows;

		for(int tileColumn = 0; tileColumn < pTileColumns; tileColumn++) {
			for(int tileRow = 0; tileRow < pTileRows; tileRow++) {
				final int tileIndex = tileRow * pTileColumns + tileColumn;

				final int x = pTextureX + tileColumn * tileWidth;
				final int y = pTextureY + tileRow * tileHeight;
				textureRegions[tileIndex] = new TextureRegion(pTexture, x, y, tileWidth, tileHeight, pRotated);
			}
		}

		return new TiledTextureRegion(pTexture, false, textureRegions);
	}

	@Override
	public TiledTextureRegion deepCopy() {
		final int tileCount = this.mTileCount;

		final ITextureRegion[] textureRegions = new ITextureRegion[tileCount];

		for(int i = 0; i < tileCount; i++) {
			textureRegions[i] = this.mTextureRegions[i].deepCopy();
		}

		return new TiledTextureRegion(this.mTexture, false, textureRegions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getCurrentTileIndex() {
		return this.mCurrentTileIndex;
	}

	@Override
	public void setCurrentTileIndex(final int pCurrentTileIndex) {
		this.mCurrentTileIndex = pCurrentTileIndex;
	}

	@Override
	public void nextTile() {
		this.mCurrentTileIndex++;
		if(this.mCurrentTileIndex >= this.mTileCount) {
			this.mCurrentTileIndex = this.mCurrentTileIndex % this.mTileCount;
		}
	}

	@Override
	public ITextureRegion getTextureRegion(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex];
	}

	@Override
	public int getTileCount() {
		return this.mTileCount;
	}

	@Override
	public float getTextureX() {
		return this.mTextureRegions[this.mCurrentTileIndex].getTextureX();
	}

	@Override
	public float getTextureX(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getTextureX();
	}

	@Override
	public float getTextureY() {
		return this.mTextureRegions[this.mCurrentTileIndex].getTextureY();
	}

	@Override
	public float getTextureY(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getTextureY();
	}

	@Override
	public void setTextureX(final float pTextureX) {
		this.mTextureRegions[this.mCurrentTileIndex].setTextureX(pTextureX);
	}

	@Override
	public void setTextureX(final int pTileIndex, final float pTextureX) {
		this.mTextureRegions[pTileIndex].setTextureY(pTextureX);
	}

	@Override
	public void setTextureY(final float pTextureY) {
		this.mTextureRegions[this.mCurrentTileIndex].setTextureY(pTextureY);
	}

	@Override
	public void setTextureY(final int pTileIndex, final float pTextureY) {
		this.mTextureRegions[pTileIndex].setTextureY(pTextureY);
	}

	@Override
	public void setTexturePosition(final float pTextureX, final float pTextureY) {
		this.mTextureRegions[this.mCurrentTileIndex].setTexturePosition(pTextureX, pTextureY);
	}

	@Override
	public void setTexturePosition(final int pTileIndex, final float pTextureX, final float pTextureY) {
		this.mTextureRegions[pTileIndex].setTexturePosition(pTextureX, pTextureY);
	}

	@Override
	public float getWidth() {
		return this.mTextureRegions[this.mCurrentTileIndex].getWidth();
	}

	@Override
	public float getWidth(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getWidth();
	}

	@Override
	public float getHeight() {
		return this.mTextureRegions[this.mCurrentTileIndex].getHeight();
	}

	@Override
	public float getHeight(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getHeight();
	}

	@Override
	public void setTextureWidth(final float pTextureWidth) {
		this.mTextureRegions[this.mCurrentTileIndex].setTextureWidth(pTextureWidth);
	}

	@Override
	public void setTextureWidth(final int pTileIndex, final float pTextureWidth) {
		this.mTextureRegions[pTileIndex].setTextureWidth(pTextureWidth);
	}

	@Override
	public void setTextureHeight(final float pTextureHeight) {
		this.mTextureRegions[this.mCurrentTileIndex].setTextureHeight(pTextureHeight);
	}

	@Override
	public void setTextureHeight(final int pTileIndex, final float pTextureHeight) {
		this.mTextureRegions[pTileIndex].setTextureHeight(pTextureHeight);
	}

	@Override
	public void setTextureSize(final float pTextureWidth, final float pTextureHeight) {
		this.mTextureRegions[this.mCurrentTileIndex].setTextureSize(pTextureWidth, pTextureHeight);
	}

	@Override
	public void setTextureSize(final int pTileIndex, final float pTextureWidth, final float pTextureHeight) {
		this.mTextureRegions[pTileIndex].setTextureSize(pTextureWidth, pTextureHeight);
	}

	@Override
	public void set(final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
		this.mTextureRegions[this.mCurrentTileIndex].set(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
	}

	@Override
	public void set(final int pTileIndex, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
		this.mTextureRegions[pTileIndex].set(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
	}

	@Override
	public float getU() {
		return this.mTextureRegions[this.mCurrentTileIndex].getU();
	}

	@Override
	public float getU(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getU();
	}

	@Override
	public float getV() {
		return this.mTextureRegions[this.mCurrentTileIndex].getV();
	}

	@Override
	public float getV(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getV();
	}

	@Override
	public float getU2() {
		return this.mTextureRegions[this.mCurrentTileIndex].getU2();
	}

	@Override
	public float getU2(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getU2();
	}

	@Override
	public float getV2() {
		return this.mTextureRegions[this.mCurrentTileIndex].getV2();
	}

	@Override
	public float getV2(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getV2();
	}

	@Override
	public boolean isScaled() {
		return this.mTextureRegions[this.mCurrentTileIndex].isScaled();
	}

	@Override
	public boolean isScaled(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].isScaled();
	}

	@Override
	public float getScale() {
		return this.mTextureRegions[this.mCurrentTileIndex].getScale();
	}

	@Override
	public float getScale(final int pTileIndex) {
		return this.mTextureRegions[pTileIndex].getScale();
	}

	@Override
	public boolean isRotated() {
		return this.mTextureRegions[this.mCurrentTileIndex].isRotated();
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
