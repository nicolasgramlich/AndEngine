package org.anddev.andengine.opengl.texture.source.decorator.shape;

import org.anddev.andengine.opengl.texture.source.decorator.BaseTextureSourceDecorator.TextureSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author Nicolas Gramlich
 * @since 12:50:09 - 04.01.2011
 */
public class RoundedRectangleTextureSourceDecoratorShape implements ITextureSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float CORNER_RADIUS_DEFAULT = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final RectF mRectF = new RectF();

	private final float mCornerRadiusX;
	private final float mCornerRadiusY;

	private static RoundedRectangleTextureSourceDecoratorShape sDefaultInstance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RoundedRectangleTextureSourceDecoratorShape() {
		this(CORNER_RADIUS_DEFAULT, CORNER_RADIUS_DEFAULT);
	}

	public RoundedRectangleTextureSourceDecoratorShape(final float pCornerRadiusX, final float pCornerRadiusY) {
		this.mCornerRadiusX = pCornerRadiusX;
		this.mCornerRadiusY = pCornerRadiusY;
	}

	public static RoundedRectangleTextureSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new RoundedRectangleTextureSourceDecoratorShape();
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

		pCanvas.drawRoundRect(this.mRectF, this.mCornerRadiusX, this.mCornerRadiusY, pPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}