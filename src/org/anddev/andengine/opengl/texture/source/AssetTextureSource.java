/**
 * 
 */
package org.anddev.andengine.opengl.texture.source;

import java.io.IOException;

import org.anddev.andengine.util.Debug;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;

/**
 * @author Nicolas Gramlich
 * @since 12:07:52 - 09.03.2010
 */
public class AssetTextureSource implements ITextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private final String mAssetPath;
	private final Context mContext;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AssetTextureSource(final Context pContext, final String pAssetPath) {
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(pContext.getAssets().open(pAssetPath), null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Texture asset not found, " + pAssetPath, e);
		}
		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
	}

	AssetTextureSource(final Context pContext, final String pAssetPath, final int pWidth, final int pHeight) {
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public AssetTextureSource clone() {
		return new AssetTextureSource(this.mContext, this.mAssetPath, this.mWidth, this.mHeight);
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
	public Bitmap loadBitmap() {
		try {
			final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
			decodeOptions.inPreferredConfig = Config.ARGB_8888;

			return BitmapFactory.decodeStream(this.mContext.getAssets().open(this.mAssetPath), null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in AssetTextureSource. AssetPath: " + this.mAssetPath, e);
			return null;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mAssetPath + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}