package org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape;

import org.anddev.andengine.opengl.texture.bitmap.source.decorator.BaseBitmapTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Nicolas Gramlich
 * @since 12:47:40 - 04.01.2011
 */
public interface IBitmapTextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final TextureSourceDecoratorOptions pDecoratorOptions);
}