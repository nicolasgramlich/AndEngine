package org.anddev.andengine.opengl.texture.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.IBitmapTextureSourceDecoratorShape;

import android.graphics.Canvas;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:09:41 - 05.11.2010
 */
public abstract class BaseShapeBitmapTextureSourceDecorator extends BaseBitmapTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IBitmapTextureSourceDecoratorShape mBitmapTextureSourceDecoratorShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseShapeBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pTextureSourceDecoratorOptions);

		this.mBitmapTextureSourceDecoratorShape = pBitmapTextureSourceDecoratorShape;
	}

	@Override
	public abstract BaseShapeBitmapTextureSourceDecorator clone();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas){
		this.mBitmapTextureSourceDecoratorShape.onDecorateBitmap(pCanvas, this.mPaint, (this.mTextureSourceDecoratorOptions == null) ? TextureSourceDecoratorOptions.DEFAULT : this.mTextureSourceDecoratorOptions);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
