package org.andengine.opengl.texture.atlas.bitmap.source;


import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.NullBitmapException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Michal Stawinski
 * @since 02:20:33 - 03.05.2012
 */
public class ExtrudingBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final ExtrusionDirection mExtrusionDirection;
	private final ITextureAtlasSource mExtrusionSource;
	private final int mExtrusionSize;
	private Bitmap mExtrusionBitmap;

	// ===========================================================
	// Constructors
	// ===========================================================


	public ExtrudingBitmapTextureAtlasSource(final int pTextureWidth, final int pTextureHeight,
			final ITextureAtlasSource pExtrusionSource, final ExtrusionDirection pExtrusionDirection, final int pExtrusionSize) {
		this(0, 0, pTextureWidth, pTextureHeight, pExtrusionSource, pExtrusionDirection, pExtrusionSize);
	}

	public ExtrudingBitmapTextureAtlasSource(final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight,
			ITextureAtlasSource pExtrusionSource, ExtrusionDirection pExtrusionDirection, final int pExtrusionSize) {
		super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
		mExtrusionDirection = pExtrusionDirection;
		mExtrusionSource = pExtrusionSource;
		mExtrusionSize = pExtrusionSize;
	}

	@Override
	public ExtrudingBitmapTextureAtlasSource deepCopy() {
		return new ExtrudingBitmapTextureAtlasSource(this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight, mExtrusionSource, mExtrusionDirection, mExtrusionSize);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ITextureAtlasSource getExtrusionSource() {
		return mExtrusionSource;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		if (mExtrusionBitmap != null && !mExtrusionBitmap.isRecycled()) {
			return mExtrusionBitmap;
		}

		return Bitmap.createBitmap(this.mTextureWidth, this.mTextureHeight, pBitmapConfig);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mTextureWidth + " x " + this.mTextureHeight + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void createExtrusionBitmap(final Bitmap pBitmapToExtrude) {
		if (mExtrusionBitmap != null && !mExtrusionBitmap.isRecycled()) {
			Debug.w("Someone did not clean up mExtrusionBitmap! Doing it now.");
			mExtrusionBitmap.recycle();
		}

		if (getTextureHeight() == pBitmapToExtrude.getHeight() && pBitmapToExtrude.getWidth() < mExtrusionSize) {
			throw new NullBitmapException("BitmapToExtrude is only " + pBitmapToExtrude.getWidth() + " pixels wide, but ExtrusionSize was set to " + mExtrusionSize);
		} else if (getTextureWidth() == pBitmapToExtrude.getWidth() && pBitmapToExtrude.getHeight() < mExtrusionSize) {
			throw new NullBitmapException("BitmapToExtrude is only " + pBitmapToExtrude.getWidth() + " pixels high, but ExtrusionSize was set to " + mExtrusionSize);
		} else if (getTextureWidth() != pBitmapToExtrude.getWidth() && getTextureHeight() != pBitmapToExtrude.getHeight()) {
			throw new NullBitmapException("BitmapToExtrude does not match" + getClass().getSimpleName());
		}

		final Matrix flippingMatrix = new Matrix();

		switch (mExtrusionDirection) {
		case LEFT:
			flippingMatrix.preScale(-1, 1);
			mExtrusionBitmap = Bitmap.createBitmap(pBitmapToExtrude, 0, 0, mExtrusionSize, pBitmapToExtrude.getHeight(), flippingMatrix, false);
			break;
		case TOP:
			flippingMatrix.preScale(1, -1);
			mExtrusionBitmap = Bitmap.createBitmap(pBitmapToExtrude, 0, 0, pBitmapToExtrude.getWidth(), mExtrusionSize, flippingMatrix, false);
			break;
		case RIGHT:
			flippingMatrix.preScale(-1, 1);
			mExtrusionBitmap = Bitmap.createBitmap(pBitmapToExtrude, pBitmapToExtrude.getWidth() - mExtrusionSize - 1, 0, mExtrusionSize, pBitmapToExtrude.getHeight(), flippingMatrix, false);
			break;
		case BOTTOM:
			flippingMatrix.preScale(1, -1);
			mExtrusionBitmap = Bitmap.createBitmap(pBitmapToExtrude, 0, pBitmapToExtrude.getHeight() - mExtrusionSize - 1, pBitmapToExtrude.getWidth(), mExtrusionSize, flippingMatrix, false);
			break;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public static enum ExtrusionDirection {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	}
}