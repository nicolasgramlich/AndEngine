package org.anddev.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.IBitmapTextureAtlasSourceDecoratorShape;

import android.graphics.AvoidXfermode;
import android.graphics.AvoidXfermode.Mode;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:41:39 - 07.06.2011
 */
public class ColorSwapBitmapTextureAtlasSourceDecorator extends BaseShapeBitmapTextureAtlasSourceDecorator {
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

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColor, final int pColorSwapColor) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT, pColorSwapColor, null);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColor, final int pColorSwapColor, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT, pColorSwapColor, pTextureAtlasSourceDecoratorOptions);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final int pColorSwapColor) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor, pTolerance, pColorSwapColor, null);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final int pColorSwapColor, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		super(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pTextureAtlasSourceDecoratorOptions);
		this.mColorKeyColor = pColorKeyColor;
		this.mTolerance = pTolerance;
		this.mColorSwapColor = pColorSwapColor;
		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColor, pTolerance, Mode.TARGET));
		this.mPaint.setColor(pColorSwapColor);
	}

	@Override
	public ColorSwapBitmapTextureAtlasSourceDecorator clone() {
		return new ColorSwapBitmapTextureAtlasSourceDecorator(this.mBitmapTextureAtlasSource, this.mBitmapTextureAtlasSourceDecoratorShape, this.mColorKeyColor, this.mTolerance, this.mColorSwapColor, this.mTextureAtlasSourceDecoratorOptions);
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
