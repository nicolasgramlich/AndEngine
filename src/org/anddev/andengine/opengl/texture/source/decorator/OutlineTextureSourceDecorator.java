package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

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

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pOutlineColor) {
		this(pTextureSource, pTextureSourceDecoratorShape, pOutlineColor, null);
	}

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pOutlineColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mOutlineColor = pOutlineColor;

		this.mPaint.setStyle(Style.STROKE);
		this.mPaint.setColor(pOutlineColor);
	}

	@Override
	public OutlineTextureSourceDecorator clone() {
		return new OutlineTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mOutlineColor, this.mTextureSourceDecoratorOptions);
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
