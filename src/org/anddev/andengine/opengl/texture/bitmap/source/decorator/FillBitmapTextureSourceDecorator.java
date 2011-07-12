package org.anddev.andengine.opengl.texture.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.IBitmapTextureSourceDecoratorShape;

import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 18:08:00 - 05.11.2010
 */
public class FillBitmapTextureSourceDecorator extends BaseShapeBitmapTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mFillColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FillBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pFillColor) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pFillColor, null);
	}

	public FillBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pFillColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mFillColor = pFillColor;

		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(pFillColor);
	}

	@Override
	public FillBitmapTextureSourceDecorator clone() {
		return new FillBitmapTextureSourceDecorator(this.mBitmapTextureSource, this.mBitmapTextureSourceDecoratorShape, this.mFillColor, this.mTextureSourceDecoratorOptions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
