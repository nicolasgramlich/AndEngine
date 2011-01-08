package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

import android.graphics.AvoidXfermode;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;

/**
 * @author Nicolas Gramlich
 * @since 22:16:41 - 06.08.2010
 */
public class ColorKeyTextureSourceDecorator extends BaseShapeTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int TOLERANCE_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mColorKeyColor;
	private final int mTolerance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, ColorKeyTextureSourceDecorator.TOLERANCE_DEFAULT, null);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, ColorKeyTextureSourceDecorator.TOLERANCE_DEFAULT, pTextureSourceDecoratorOptions);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, pTolerance, null);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mColorKeyColor = pColorKeyColor;
		this.mTolerance = pTolerance;
		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColor, pTolerance, Mode.TARGET));
		this.mPaint.setColor(Color.TRANSPARENT);
	}

	@Override
	public ColorKeyTextureSourceDecorator clone() {
		return new ColorKeyTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mColorKeyColor, this.mTolerance, this.mTextureSourceDecoratorOptions);
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
