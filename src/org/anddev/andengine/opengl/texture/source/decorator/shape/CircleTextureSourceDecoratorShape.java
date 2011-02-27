package org.anddev.andengine.opengl.texture.source.decorator.shape;

import org.anddev.andengine.opengl.texture.source.decorator.BaseTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Nicolas Gramlich
 * @since 12:53:13 - 04.01.2011
 */
public class CircleTextureSourceDecoratorShape implements ITextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static CircleTextureSourceDecoratorShape sDefaultInstance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CircleTextureSourceDecoratorShape() {

	}

	public static CircleTextureSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new CircleTextureSourceDecoratorShape();
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

		final float centerX = (pCanvas.getWidth() + pDecoratorOptions.getInsetLeft() - pDecoratorOptions.getInsetRight()) / 2;
		final float centerY = (pCanvas.getHeight() + pDecoratorOptions.getInsetTop() - pDecoratorOptions.getInsetBottom()) / 2;

		final float radius = Math.min(width / 2, height / 2);

		pCanvas.drawCircle(centerX, centerY, radius, pPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

