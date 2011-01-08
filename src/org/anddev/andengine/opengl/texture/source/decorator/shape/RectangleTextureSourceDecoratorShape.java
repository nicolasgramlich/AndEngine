package org.anddev.andengine.opengl.texture.source.decorator.shape;

import org.anddev.andengine.opengl.texture.source.decorator.BaseTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectangleTextureSourceDecoratorShape implements ITextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static RectangleTextureSourceDecoratorShape sDefaultInstance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangleTextureSourceDecoratorShape() {

	}

	public static RectangleTextureSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new RectangleTextureSourceDecoratorShape();
		}
		return sDefaultInstance;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final TextureSourceDecoratorOptions pDecoratorOptions) {
		final float left = pDecoratorOptions.getInsetLeft();
		final float top = pDecoratorOptions.getInsetTop();
		final float right = pCanvas.getWidth() - 1 - pDecoratorOptions.getInsetRight();
		final float bottom = pCanvas.getHeight() - 1 - pDecoratorOptions.getInsetBottom();
		pCanvas.drawRect(left, top, right, bottom, pPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
};