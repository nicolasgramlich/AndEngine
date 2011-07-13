package org.anddev.andengine.opengl.texture.bitmap.source;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:07:23 - 09.03.2010
 */
public class ResourceBitmapTextureSource extends BaseBitmapTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private final int mDrawableResourceID;
	private final Context mContext;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourceBitmapTextureSource(final Context pContext, final int pDrawableResourceID) {
		this(pContext, 0, 0, pDrawableResourceID);
	}

	public ResourceBitmapTextureSource(final Context pContext, final int pTexturePositionX, final int pTexturePositionY, final int pDrawableResourceID) {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mDrawableResourceID = pDrawableResourceID;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders

		BitmapFactory.decodeResource(pContext.getResources(), pDrawableResourceID, decodeOptions);

		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
	}

	protected ResourceBitmapTextureSource(final Context pContext, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final int pDrawableResourceID) {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mDrawableResourceID = pDrawableResourceID;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public ResourceBitmapTextureSource clone() {
		return new ResourceBitmapTextureSource(this.mContext, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mDrawableResourceID);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = pBitmapConfig;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders
		return BitmapFactory.decodeResource(this.mContext.getResources(), this.mDrawableResourceID, decodeOptions);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mDrawableResourceID + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}