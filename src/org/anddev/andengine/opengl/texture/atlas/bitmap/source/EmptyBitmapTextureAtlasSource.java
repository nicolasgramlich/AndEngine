package org.anddev.andengine.opengl.texture.atlas.bitmap.source;

import org.anddev.andengine.opengl.texture.source.BaseTextureAtlasSource;

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

	private final int mWidth;
	private final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public EmptyBitmapTextureAtlasSource(final int pWidth, final int pHeight) {
		this(0, 0, pWidth, pHeight);
	}

	public EmptyBitmapTextureAtlasSource(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexturePositionX, pTexturePositionY);
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public EmptyBitmapTextureAtlasSource clone() {
		return new EmptyBitmapTextureAtlasSource(this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
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
		return Bitmap.createBitmap(this.mWidth, this.mHeight, pBitmapConfig);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mWidth + " x " + this.mHeight + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}