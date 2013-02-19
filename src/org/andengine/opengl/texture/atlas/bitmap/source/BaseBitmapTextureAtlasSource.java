package org.andengine.opengl.texture.atlas.bitmap.source;

import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:43:29 - 06.08.2010
 */
public abstract class BaseBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IBitmapTextureAtlasSource mBitmapTextureAtlasSource;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource) {
		super(pBitmapTextureAtlasSource.getTextureX(), pBitmapTextureAtlasSource.getTextureY(), pBitmapTextureAtlasSource.getTextureWidth(), pBitmapTextureAtlasSource.getTextureHeight());

		this.mBitmapTextureAtlasSource = pBitmapTextureAtlasSource;
	}

	@Override
	public abstract BaseBitmapTextureAtlasSource deepCopy();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getTextureWidth() {
		return this.mBitmapTextureAtlasSource.getTextureWidth();
	}

	@Override
	public int getTextureHeight() {
		return this.mBitmapTextureAtlasSource.getTextureHeight();
	}

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		return this.mBitmapTextureAtlasSource.onLoadBitmap(pBitmapConfig);
	}

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig, final boolean pMutable) {
		return this.mBitmapTextureAtlasSource.onLoadBitmap(pBitmapConfig, pMutable);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
