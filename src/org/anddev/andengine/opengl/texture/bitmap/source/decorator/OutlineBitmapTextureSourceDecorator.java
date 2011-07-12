package org.anddev.andengine.opengl.texture.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.IBitmapTextureSourceDecoratorShape;

import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 18:07:55 - 05.11.2010
 */
public class OutlineBitmapTextureSourceDecorator extends BaseShapeBitmapTextureSourceDecorator {
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

	public OutlineBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pOutlineColor) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pOutlineColor, null);
	}

	public OutlineBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pOutlineColor, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mOutlineColor = pOutlineColor;

		this.mPaint.setStyle(Style.STROKE);
		this.mPaint.setColor(pOutlineColor);
	}

	@Override
	public OutlineBitmapTextureSourceDecorator clone() {
		return new OutlineBitmapTextureSourceDecorator(this.mBitmapTextureSource, this.mBitmapTextureSourceDecoratorShape, this.mOutlineColor, this.mTextureSourceDecoratorOptions);
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
