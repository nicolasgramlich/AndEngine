package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

import android.graphics.Color;

/**
 * @author Nicolas Gramlich
 * @since 22:16:41 - 06.08.2010
 */
public class ColorKeyTextureSourceDecorator extends ColorSwapTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor) {
		super(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, Color.TRANSPARENT);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, Color.TRANSPARENT, pTextureSourceDecoratorOptions);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance) {
		super(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, Color.TRANSPARENT, pTolerance);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pColorKeyColor, final int pTolerance, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pColorKeyColor, pTolerance, Color.TRANSPARENT, pTextureSourceDecoratorOptions);
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
