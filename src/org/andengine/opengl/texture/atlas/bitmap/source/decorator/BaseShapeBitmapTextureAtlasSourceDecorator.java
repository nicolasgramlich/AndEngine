package org.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.IBitmapTextureAtlasSourceDecoratorShape;

import android.graphics.Canvas;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:09:41 - 05.11.2010
 */
public abstract class BaseShapeBitmapTextureAtlasSourceDecorator extends BaseBitmapTextureAtlasSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IBitmapTextureAtlasSourceDecoratorShape mBitmapTextureAtlasSourceDecoratorShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseShapeBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		super(pBitmapTextureAtlasSource, pTextureAtlasSourceDecoratorOptions);

		this.mBitmapTextureAtlasSourceDecoratorShape = pBitmapTextureAtlasSourceDecoratorShape;
	}

	@Override
	public abstract BaseShapeBitmapTextureAtlasSourceDecorator deepCopy();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas){
		this.mBitmapTextureAtlasSourceDecoratorShape.onDecorateBitmap(pCanvas, this.mPaint, (this.mTextureAtlasSourceDecoratorOptions == null) ? TextureAtlasSourceDecoratorOptions.DEFAULT : this.mTextureAtlasSourceDecoratorOptions);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
