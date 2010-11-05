package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.graphics.AvoidXfermode.Mode;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final int pColorKeyColor) {
		this(pTextureSource, TextureSourceDecoratorShape.RECTANGLE, pColorKeyColor);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, TOLERANCE_DEFAULT);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final int pColorKeyColor, final int pTolerance) {
		this(pTextureSource, TextureSourceDecoratorShape.RECTANGLE, pColorKeyColor, pTolerance);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance) {
		super(pTextureSource, pTextureSourceDecoratorShape);
		this.mColorKeyColor = pColorKeyColor;
		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColor, pTolerance, Mode.TARGET));
		this.mPaint.setColor(Color.TRANSPARENT);
	}

	@Override
	public ColorKeyTextureSourceDecorator clone() {
		return new ColorKeyTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mColorKeyColor);
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
