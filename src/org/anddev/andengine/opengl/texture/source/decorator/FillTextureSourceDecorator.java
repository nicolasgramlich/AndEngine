package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

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

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final int pFillColor) {
		this(pTextureSource, TextureSourceDecoratorShape.RECTANGLE, pFillColor);
	}

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFillColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pFillColor, false);
	}

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final int pFillColor, final boolean pAntiAliasing) {
		this(pTextureSource, TextureSourceDecoratorShape.RECTANGLE, pFillColor, pAntiAliasing);
	}

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFillColor, final boolean pAntiAliasing) {
		super(pTextureSource, pTextureSourceDecoratorShape, pAntiAliasing);
		this.mFillColor = pFillColor;

		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(pFillColor);
	}

	@Override
	public FillTextureSourceDecorator clone() {
		return new FillTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mFillColor, this.mAntiAliasing);
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
