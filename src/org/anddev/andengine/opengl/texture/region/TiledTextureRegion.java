package org.anddev.andengine.opengl.texture.region;

import java.util.Arrays;

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
	protected int mTileCount;

	protected int[] mXs;
	protected int[] mYs;
	protected int[] mWidths;
	protected int[] mHeights;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegion(final ITexture pTexture, final int[] pXs, final int[] pYs, final int[] pWidths, final int[] pHeights) {
		super(pTexture);
		this.mTileCount = pWidths.length;

		this.mXs = pXs;
		this.mYs = pYs;

		this.mWidths = pWidths;
		this.mHeights = pHeights;

		this.updateUV();
	}

	public static TiledTextureRegion create(final ITexture pTexture, final int pX, final int pY, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final int tileCount = pTileColumns * pTileRows;

		final int[] xs = new int[tileCount];
		final int[] ys = new int[tileCount];
		final int[] widths = new int[tileCount];
		final int[] heights = new int[tileCount];

		final int tileWidth = pWidth / pTileColumns;
		final int tileHeight = pHeight / pTileRows;

		Arrays.fill(widths, tileWidth);
		Arrays.fill(heights, tileHeight);

		for(int i = 0; i < tileCount; i++) {
			final int tileColumn = i % pTileColumns;
			final int tileRow = i / pTileColumns;

			xs[i] = pX + tileColumn * tileWidth;
			ys[i] = pY + tileRow * tileHeight;
		}

		return new TiledTextureRegion(pTexture, xs, ys, widths, heights);
	}

	@Override
	public TiledTextureRegion clone() {
		final int tileCount = this.mTileCount;

		final int[] xs = new int[tileCount];
		final int[] ys = new int[tileCount];
		final int[] widths = new int[tileCount];
		final int[] heights = new int[tileCount];

		System.arraycopy(this.mXs, 0, xs, 0, tileCount);
		System.arraycopy(this.mYs, 0, ys, 0, tileCount);
		System.arraycopy(this.mWidths, 0, widths, 0, tileCount);
		System.arraycopy(this.mHeights, 0, heights, 0, tileCount);

		return new TiledTextureRegion(this.mTexture, xs, ys, widths, heights);
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
	
			this.updateUV();
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

		this.updateUV();
	}

	@Override
	public int getTileCount() {
		return this.mTileCount;
	}

	@Override
	public int getX() {
		return this.mXs[this.mTileIndex];
	}

	@Override
	public int getY() {
		return this.mYs[this.mTileIndex];
	}

	@Override
	public void setX(final int pX) {
		this.mXs[this.mTileIndex] = pX;

		this.updateUV();
	}

	@Override
	public void setY(final int pY) {
		this.mYs[this.mTileIndex] = pY;

		this.updateUV();
	}

	@Override
	public void setPosition(final int pX, final int pY) {
		final int tileIndex = this.mTileIndex;

		this.mXs[tileIndex] = pX;
		this.mYs[tileIndex] = pY;

		this.updateUV();
	}

	@Override
	public int getWidth() {
		return this.mWidths[this.mTileIndex];
	}

	@Override
	public int getHeight() {
		return this.mHeights[this.mTileIndex];
	}

	@Override
	public void setWidth(final int pWidth) {
		this.mWidths[this.mTileIndex] = pWidth;

		this.updateUV();
	}

	@Override
	public void setHeight(final int pHeight) {
		this.mHeights[this.mTileIndex] = pHeight;

		this.updateUV();
	}

	@Override
	public void setSize(final int pWidth, final int pHeight) {
		final int tileIndex = this.mTileIndex;

		this.mWidths[tileIndex] = pWidth;
		this.mHeights[tileIndex] = pHeight;

		this.updateUV();
	}

	@Override
	public void set(final int pX, final int pY, final int pWidth, final int pHeight) {
		final int tileIndex = this.mTileIndex;

		this.mXs[tileIndex] = pX;
		this.mYs[tileIndex] = pY;
		this.mWidths[tileIndex] = pWidth;
		this.mHeights[tileIndex] = pHeight;

		this.updateUV();
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
