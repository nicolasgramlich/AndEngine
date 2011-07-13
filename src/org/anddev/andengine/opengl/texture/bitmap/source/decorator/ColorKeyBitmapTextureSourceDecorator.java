package org.anddev.andengine.opengl.texture.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.IBitmapTextureSourceDecoratorShape;

import android.graphics.Color;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:16:41 - 06.08.2010
 */
public class ColorKeyBitmapTextureSourceDecorator extends ColorSwapBitmapTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, Color.TRANSPARENT);
	}

	public ColorKeyBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, Color.TRANSPARENT, pTextureSourceDecoratorOptions);
	}

	public ColorKeyBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, Color.TRANSPARENT, pTolerance);
	}

	public ColorKeyBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, pTolerance, Color.TRANSPARENT, pTextureSourceDecoratorOptions);
	}

	@Override
	public ColorKeyBitmapTextureSourceDecorator clone() {
		return new ColorKeyBitmapTextureSourceDecorator(this.mBitmapTextureSource, this.mBitmapTextureSourceDecoratorShape, this.mColorKeyColor, this.mTolerance, this.mTextureSourceDecoratorOptions);
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
