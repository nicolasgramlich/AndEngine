package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

import android.graphics.AvoidXfermode;
import android.graphics.AvoidXfermode.Mode;

/**
 * @author Nicolas Gramlich
 * @since 19:41:39 - 07.06.2011
 */
public class ColorSwapTextureSourceDecorator extends BaseShapeTextureSourceDecorator {
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

	public ColorSwapTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pColorSwapColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT, pColorSwapColor, null);
	}

	public ColorSwapTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pColorSwapColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT, pColorSwapColor, pTextureSourceDecoratorOptions);
	}

	public ColorSwapTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final int pColorSwapColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, pTolerance, pColorSwapColor, null);
	}

	public ColorSwapTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final int pColorSwapColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mColorKeyColor = pColorKeyColor;
		this.mTolerance = pTolerance;
		this.mColorSwapColor = pColorSwapColor;
		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColor, pTolerance, Mode.TARGET));
		this.mPaint.setColor(pColorSwapColor);
	}

	@Override
	public ColorSwapTextureSourceDecorator clone() {
		return new ColorSwapTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mColorKeyColor, this.mTolerance, this.mColorSwapColor, this.mTextureSourceDecoratorOptions);
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
