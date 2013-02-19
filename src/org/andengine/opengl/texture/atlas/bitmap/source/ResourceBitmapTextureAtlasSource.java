package org.andengine.opengl.texture.atlas.bitmap.source;


import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;
import org.andengine.util.BitmapUtils;
import org.andengine.util.system.SystemUtils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:07:23 - 09.03.2010
 */
public class ResourceBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Resources mResources;
	private final int mDrawableResourceID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static ResourceBitmapTextureAtlasSource create(final Resources pResources, final int pDrawableResourceID) {
		return ResourceBitmapTextureAtlasSource.create(pResources, pDrawableResourceID, 0, 0);
	}

	public static ResourceBitmapTextureAtlasSource create(final Resources pResources, final int pDrawableResourceID, final int pTextureX, final int pTextureY) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders

		BitmapFactory.decodeResource(pResources, pDrawableResourceID, decodeOptions);

		return new ResourceBitmapTextureAtlasSource(pResources, pDrawableResourceID, pTextureX, pTextureY, decodeOptions.outWidth, decodeOptions.outHeight);
	}

	public ResourceBitmapTextureAtlasSource(final Resources pResources, final int pDrawableResourceID, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);

		this.mResources = pResources;
		this.mDrawableResourceID = pDrawableResourceID;
	}

	@Override
	public ResourceBitmapTextureAtlasSource deepCopy() {
		return new ResourceBitmapTextureAtlasSource(this.mResources, this.mDrawableResourceID, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		return this.onLoadBitmap(pBitmapConfig, false);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig, final boolean pMutable) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = pBitmapConfig;
		decodeOptions.inDither = false;
//		decodeOptions.inScaled = false; // TODO Check how this behaves with drawable-""/nodpi/ldpi/mdpi/hdpi folders

		if (pMutable && SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB)) {
			decodeOptions.inMutable = pMutable;
		}

		final Bitmap bitmap = BitmapFactory.decodeResource(this.mResources, this.mDrawableResourceID, decodeOptions);

		if (pMutable) {
			return BitmapUtils.ensureBitmapIsMutable(bitmap);
		} else {
			return bitmap;
		}
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