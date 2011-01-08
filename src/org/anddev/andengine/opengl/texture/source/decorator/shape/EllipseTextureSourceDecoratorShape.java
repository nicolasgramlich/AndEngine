package org.anddev.andengine.opengl.texture.source.decorator.shape;

import org.anddev.andengine.opengl.texture.source.decorator.BaseTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author Nicolas Gramlich
 * @since 12:52:55 - 04.01.2011
 */
public class EllipseTextureSourceDecoratorShape implements ITextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EllipseTextureSourceDecoratorShape sDefaultInstance;

	private final RectF mRectF = new RectF();

	// ===========================================================
	// Constructors
	// ===========================================================

	public EllipseTextureSourceDecoratorShape() {

	}

	public static EllipseTextureSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new EllipseTextureSourceDecoratorShape();
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

