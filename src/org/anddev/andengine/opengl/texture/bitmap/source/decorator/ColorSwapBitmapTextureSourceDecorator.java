package org.anddev.andengine.opengl.texture.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.IBitmapTextureSourceDecoratorShape;

import android.graphics.AvoidXfermode;
import android.graphics.AvoidXfermode.Mode;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:41:39 - 07.06.2011
 */
public class ColorSwapBitmapTextureSourceDecorator extends BaseShapeBitmapTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int TOLERANCE_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mColorKeyColor;
	protected final int mTolerance;
	protected final int mColorSwapColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorSwapBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final int pColorSwapColor) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT, pColorSwapColor, null);
	}

	public ColorSwapBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final int pColorSwapColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT, pColorSwapColor, pTextureSourceDecoratorOptions);
	}

	public ColorSwapBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final int pColorSwapColor) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColorKeyColor, pTolerance, pColorSwapColor, null);
	}

	public ColorSwapBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final int pColorSwapColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mColorKeyColor = pColorKeyColor;
		this.mTolerance = pTolerance;
		this.mColorSwapColor = pColorSwapColor;
		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColor, pTolerance, Mode.TARGET));
		this.mPaint.setColor(pColorSwapColor);
	}

	@Override
	public ColorSwapBitmapTextureSourceDecorator clone() {
		return new ColorSwapBitmapTextureSourceDecorator(this.mBitmapTextureSource, this.mBitmapTextureSourceDecoratorShape, this.mColorKeyColor, this.mTolerance, this.mColorSwapColor, this.mTextureSourceDecoratorOptions);
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
