package org.andengine.opengl.texture.atlas.bitmap.source;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;
import org.andengine.util.StreamUtils;
import org.andengine.util.debug.Debug;

import android.content.res.AssetManager;
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
public class AssetBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final AssetManager mAssetManager;
	private final String mAssetPath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static AssetBitmapTextureAtlasSource create(final AssetManager pAssetManager, final String pAssetPath) {
		return AssetBitmapTextureAtlasSource.create(pAssetManager, pAssetPath, 0, 0);
	}

	public static AssetBitmapTextureAtlasSource create(final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		InputStream in = null;
		try {
			in = pAssetManager.open(pAssetPath);
			BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in AssetBitmapTextureAtlasSource. AssetPath: " + pAssetPath, e);
		} finally {
			StreamUtils.close(in);
		}

		return new AssetBitmapTextureAtlasSource(pAssetManager, pAssetPath, pTextureX, pTextureY, decodeOptions.outWidth, decodeOptions.outHeight);
	}
	
	public static AssetBitmapTextureAtlasSource createSampled(final AssetManager pAssetManager, final String pAssetPath, final int pReqWidth, final int pReqHeight) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		InputStream in = null;
		try {
			in = pAssetManager.open(pAssetPath);
			BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in AssetBitmapTextureAtlasSource. AssetPath: " + pAssetPath, e);
		} finally {
			StreamUtils.close(in);
		}
		
		// Calculate inSampleSize
		final int lInSampleSize = calculateInSampleSize(decodeOptions, pReqWidth, pReqHeight);
		final int lTextureWidth = decodeOptions.outWidth / lInSampleSize;
		final int lTextureHeight = decodeOptions.outHeight / lInSampleSize;
		Debug.i("createSampled(pAssetManager, pAssetPath=" + pAssetPath + ", pReqWidth=" + pReqWidth + ", pReqHeight=" + pReqHeight + ") original image of " + decodeOptions.outWidth + "x" + decodeOptions.outHeight + " lInSampleSize=" + lInSampleSize + " -> resampled to size " + lTextureWidth + "x" + lTextureHeight);
		return new AssetBitmapTextureAtlasSource(pAssetManager, pAssetPath, 0, 0, lTextureWidth, lTextureHeight, lInSampleSize);
	}

	AssetBitmapTextureAtlasSource(final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		this(pAssetManager, pAssetPath, pTextureX, pTextureY, pTextureWidth, pTextureHeight, 1);
	}
	
	AssetBitmapTextureAtlasSource(final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final int pSampleSize) {
		super(pTextureX, pTextureY, pTextureWidth, pTextureHeight, pSampleSize);

		this.mAssetManager = pAssetManager;
		this.mAssetPath = pAssetPath;
	}

	@Override
	public AssetBitmapTextureAtlasSource deepCopy() {
		return new AssetBitmapTextureAtlasSource(this.mAssetManager, this.mAssetPath, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		InputStream in = null;
		try {
			final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
			decodeOptions.inPreferredConfig = pBitmapConfig;
			if (mSampleSize != 1) {
				decodeOptions.inSampleSize = mSampleSize;
			}
			in = this.mAssetManager.open(this.mAssetPath);
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
	
	public static int calculateInSampleSize(final BitmapFactory.Options pOptions, final int pReqWidth, final int pReqHeight) {
		// Raw height and width of image
		final int lHeight = pOptions.outHeight;
		final int lWidth = pOptions.outWidth;
		int lInSampleSize = 1;

		if (lHeight > pReqHeight || lWidth > pReqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int lHeightRatio = Math.round((float) lHeight / (float) pReqHeight);
			final int lWidthRatio = Math.round((float) lWidth / (float) pReqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			lInSampleSize = lHeightRatio < lWidthRatio ? lHeightRatio : lWidthRatio;
		}

		return lInSampleSize;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}