package org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape;

import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator.TextureAtlasSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:53:13 - 04.01.2011
 */
public class CircleBitmapTextureAtlasSourceDecoratorShape implements IBitmapTextureAtlasSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static CircleBitmapTextureAtlasSourceDecoratorShape sDefaultInstance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CircleBitmapTextureAtlasSourceDecoratorShape() {

	}

	public static CircleBitmapTextureAtlasSourceDecoratorShape getDefaultInstance() {
		if (sDefaultInstance == null) {
			sDefaultInstance = new CircleBitmapTextureAtlasSourceDecoratorShape();
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
	public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final TextureAtlasSourceDecoratorOptions pDecoratorOptions) {
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

