package org.anddev.andengine.opengl.texture.source;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/**
 * @author Nicolas Gramlich
 * @since 15:07:23 - 09.03.2010
 */
public class ResourceTextureSource implements ITextureSource {
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

	public ResourceTextureSource(final Context pContext, final int pDrawableResourceID) {
		this.mContext = pContext;
		this.mDrawableResourceID = pDrawableResourceID;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders

		BitmapFactory.decodeResource(pContext.getResources(), pDrawableResourceID, decodeOptions);

		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
	}

	protected ResourceTextureSource(final Context pContext, final int pDrawableResourceID, final int pWidth, final int pHeight) {
		this.mContext = pContext;
		this.mDrawableResourceID = pDrawableResourceID;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public ResourceTextureSource clone() {
		return new ResourceTextureSource(this.mContext, this.mDrawableResourceID, this.mWidth, this.mHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public Bitmap onLoadBitmap() {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = Config.ARGB_8888;
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