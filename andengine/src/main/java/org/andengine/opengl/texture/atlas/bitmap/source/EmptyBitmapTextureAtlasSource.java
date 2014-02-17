package org.andengine.opengl.texture.atlas.bitmap.source;


import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:20:36 - 08.08.2010
 */
public class EmptyBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public EmptyBitmapTextureAtlasSource(final int pTextureWidth, final int pTextureHeight) {
		this(0, 0, pTextureWidth, pTextureHeight);
	}

	public EmptyBitmapTextureAtlasSource(final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);
	}

	@Override
	public EmptyBitmapTextureAtlasSource deepCopy() {
		return new EmptyBitmapTextureAtlasSource(this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		return Bitmap.createBitmap(this.mTextureWidth, this.mTextureHeight, pBitmapConfig);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mTextureWidth + " x " + this.mTextureHeight + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}