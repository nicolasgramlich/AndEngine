package org.anddev.andengine.opengl.texture.bitmap.source;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * @author Nicolas Gramlich
 * @since 20:20:36 - 08.08.2010
 */
public class EmptyBitmapTextureSource extends BaseBitmapTextureSource {
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
	
	public EmptyBitmapTextureSource(final int pWidth, final int pHeight) {
		this(0, 0, pWidth, pHeight);
	}

	public EmptyBitmapTextureSource(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexturePositionX, pTexturePositionY);
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public EmptyBitmapTextureSource clone() {
		return new EmptyBitmapTextureSource(this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
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