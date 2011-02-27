package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

import android.graphics.Canvas;

/**
 * @author Nicolas Gramlich
 * @since 20:09:41 - 05.11.2010
 */
public abstract class BaseShapeTextureSourceDecorator extends BaseTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITextureSourceDecoratorShape mTextureSourceDecoratorShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseShapeTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorOptions);

		this.mTextureSourceDecoratorShape = pTextureSourceDecoratorShape;
	}

	@Override
	public abstract BaseShapeTextureSourceDecorator clone();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas){
		this.mTextureSourceDecoratorShape.onDecorateBitmap(pCanvas, this.mPaint, (this.mTextureSourceDecoratorOptions == null) ? TextureSourceDecoratorOptions.DEFAULT : this.mTextureSourceDecoratorOptions);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
