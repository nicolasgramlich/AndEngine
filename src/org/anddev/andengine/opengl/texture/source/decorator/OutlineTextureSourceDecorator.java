package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 18:07:55 - 05.11.2010
 */
public class OutlineTextureSourceDecorator extends BaseShapeTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mOutlineColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final int pOutlineColor) {
		this(pTextureSource, TextureSourceDecoratorShape.RECTANGLE, pOutlineColor);
	}

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pOutlineColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pOutlineColor, false);
	}

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final int pOutlineColor, final boolean pAntiAliasing) {
		this(pTextureSource, TextureSourceDecoratorShape.RECTANGLE, pOutlineColor, pAntiAliasing);
	}

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pOutlineColor, final boolean pAntiAliasing) {
		super(pTextureSource, pTextureSourceDecoratorShape, pAntiAliasing);
		this.mOutlineColor = pOutlineColor;

		this.mPaint.setStyle(Style.STROKE);
		this.mPaint.setColor(pOutlineColor);
	}

	@Override
	public OutlineTextureSourceDecorator clone() {
		return new OutlineTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mOutlineColor, this.mAntiAliasing);
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
