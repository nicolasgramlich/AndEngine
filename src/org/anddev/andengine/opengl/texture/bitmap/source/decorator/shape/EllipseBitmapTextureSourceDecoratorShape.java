package org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape;

import org.anddev.andengine.opengl.texture.bitmap.source.decorator.BaseBitmapTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author Nicolas Gramlich
 * @since 12:52:55 - 04.01.2011
 */
public class EllipseBitmapTextureSourceDecoratorShape implements IBitmapTextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EllipseBitmapTextureSourceDecoratorShape sDefaultInstance;

	private final RectF mRectF = new RectF();

	// ===========================================================
	// Constructors
	// ===========================================================

	public EllipseBitmapTextureSourceDecoratorShape() {

	}

	public static EllipseBitmapTextureSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new EllipseBitmapTextureSourceDecoratorShape();
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
		final float right = pCanvas.getWidth() - pDecoratorOptions.getInsetRight();
		final float bottom = pCanvas.getHeight() - pDecoratorOptions.getInsetBottom();

		this.mRectF.set(left, top, right, bottom);
		
		pCanvas.drawOval(this.mRectF, pPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

