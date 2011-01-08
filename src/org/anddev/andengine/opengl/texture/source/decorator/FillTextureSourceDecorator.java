package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 18:08:00 - 05.11.2010
 */
public class FillTextureSourceDecorator extends BaseShapeTextureSourceDecorator {
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

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFillColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pFillColor, null);
	}

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFillColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mFillColor = pFillColor;

		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(pFillColor);
	}

	@Override
	public FillTextureSourceDecorator clone() {
		return new FillTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mFillColor, this.mTextureSourceDecoratorOptions);
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
