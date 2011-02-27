package org.anddev.andengine.opengl.texture.source.decorator.shape;

import org.anddev.andengine.opengl.texture.source.decorator.BaseTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Nicolas Gramlich
 * @since 12:47:40 - 04.01.2011
 */
public interface ITextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final TextureSourceDecoratorOptions pDecoratorOptions);
}