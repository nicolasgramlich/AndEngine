package org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape;

import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator.TextureAtlasSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:50:09 - 04.01.2011
 */
public class RoundedRectangleBitmapTextureAtlasSourceDecoratorShape implements IBitmapTextureAtlasSourceDecoratorShape {
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

	private static RoundedRectangleBitmapTextureAtlasSourceDecoratorShape sDefaultInstance;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RoundedRectangleBitmapTextureAtlasSourceDecoratorShape() {
		this(CORNER_RADIUS_DEFAULT, CORNER_RADIUS_DEFAULT);
	}

	public RoundedRectangleBitmapTextureAtlasSourceDecoratorShape(final float pCornerRadiusX, final float pCornerRadiusY) {
		this.mCornerRadiusX = pCornerRadiusX;
		this.mCornerRadiusY = pCornerRadiusY;
	}

	public static RoundedRectangleBitmapTextureAtlasSourceDecoratorShape getDefaultInstance() {
		if (sDefaultInstance == null) {
			sDefaultInstance = new RoundedRectangleBitmapTextureAtlasSourceDecoratorShape();
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
		final float left = pDecoratorOptions.getInsetLeft();
		final float top = pDecoratorOptions.getInsetTop();
		final float right = pCanvas.getWidth() - pDecoratorOptions.getInsetRight();
		final float bottom = pCanvas.getHeight() - pDecoratorOptions.getInsetBottom();

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