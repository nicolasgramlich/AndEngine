package org.anddev.andengine.opengl.texture.bitmap.source;

import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.opengl.texture.source.BaseTextureSource;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:07:52 - 09.03.2010
 */
public class AssetBitmapTextureSource extends BaseTextureSource implements IBitmapTextureSource {
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

	public AssetBitmapTextureSource(final Context pContext, final String pAssetPath) {
		this(pContext, 0, 0, pAssetPath);
	}

	public AssetBitmapTextureSource(final Context pContext, final int pTexturePositionX, final int pTexturePositionY, final String pAssetPath) {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		InputStream in = null;
		try {
			in = pContext.getAssets().open(pAssetPath);
			BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in AssetBitmapTextureSource. AssetPath: " + pAssetPath, e);
		} finally {
			StreamUtils.close(in);
		}

		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
	}

	AssetBitmapTextureSource(final Context pContext, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final String pAssetPath) {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public AssetBitmapTextureSource clone() {
		return new AssetBitmapTextureSource(this.mContext, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mAssetPath);
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
		InputStream in = null;
		try {
			final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
			decodeOptions.inPreferredConfig = pBitmapConfig;

			in = this.mContext.getAssets().open(this.mAssetPath);
			return BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in " + this.getClass().getSimpleName() + ". AssetPath: " + this.mAssetPath, e);
			return null;
		} finally {
			StreamUtils.close(in);
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