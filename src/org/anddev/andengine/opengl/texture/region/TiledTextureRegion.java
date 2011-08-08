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
public class TiledTextureRegion extends BaseTextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mTileIndex;

	protected int[] mXs;
	protected int[] mYs;
	protected int[] mWidths;
	protected int[] mHeights;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledTextureRegion(final ITexture pTexture, final int[] pX, final int[] pY, final int[] pWidth, final int[] pHeight) {
		super(pTexture);

		this.mXs = pX;
		this.mYs = pY;

		this.mWidths = pWidth;
		this.mHeights = pHeight;

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
			final int tileRow = i % pTileColumns;
			final int tileColumn = i / pTileColumns;

			xs[i] = pX + tileColumn * tileWidth;
			ys[i] = pY + tileRow * tileHeight;
		}

		return new TiledTextureRegion(pTexture, xs, ys, widths, heights);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
