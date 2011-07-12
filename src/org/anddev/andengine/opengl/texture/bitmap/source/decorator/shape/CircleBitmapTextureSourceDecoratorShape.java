package org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape;

import org.anddev.andengine.opengl.texture.bitmap.source.decorator.BaseBitmapTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Nicolas Gramlich
 * @since 12:53:13 - 04.01.2011
 */
public class CircleBitmapTextureSourceDecoratorShape implements IBitmapTextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static CircleBitmapTextureSourceDecoratorShape sDefaultInstance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CircleBitmapTextureSourceDecoratorShape() {

	}

	public static CircleBitmapTextureSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new CircleBitmapTextureSourceDecoratorShape();
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
		final float width = pCanvas.getWidth() - pDecoratorOptions.getInsetLeft() - pDecoratorOptions.getInsetRight();
		final float height = pCanvas.getHeight() - pDecoratorOptions.getInsetTop() - pDecoratorOptions.getInsetBottom();

		final float centerX = (pCanvas.getWidth() + pDecoratorOptions.getInsetLeft() - pDecoratorOptions.getInsetRight()) * 0.5f;
		final float centerY = (pCanvas.getHeight() + pDecoratorOptions.getInsetTop() - pDecoratorOptions.getInsetBottom()) * 0.5f;

		final float radius = Math.min(width * 0.5f, height * 0.5f);

		pCanvas.drawCircle(centerX, centerY, radius, pPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

