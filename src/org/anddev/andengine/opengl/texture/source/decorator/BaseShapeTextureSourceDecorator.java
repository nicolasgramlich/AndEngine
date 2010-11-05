package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author Nicolas Gramlich
 * @since 20:09:41 - 05.11.2010
 */
public abstract class BaseShapeTextureSourceDecorator extends BaseTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final TextureSourceDecoratorShape mTextureSourceDecoratorShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseShapeTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape) {
		super(pTextureSource);

		this.mTextureSourceDecoratorShape = pTextureSourceDecoratorShape;
	}

	public BaseShapeTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorShape pTextureSourceDecoratorShape, final boolean pAntiAliasing) {
		super(pTextureSource, pAntiAliasing);

		this.mTextureSourceDecoratorShape = pTextureSourceDecoratorShape;
	}

	@Override
	public abstract BaseShapeTextureSourceDecorator clone();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas){
		this.mTextureSourceDecoratorShape.onDecorateBitmap(pCanvas, this.mPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum TextureSourceDecoratorShape {
		// ===========================================================
		// Elements
		// ===========================================================

		CIRCLE() {
			@Override
			public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint) {
				final float widthHalf = pCanvas.getWidth() / 2;
				final float heightHalf = pCanvas.getHeight() / 2;
				final float radius = Math.min(widthHalf, heightHalf);
				pCanvas.drawCircle(widthHalf - 0.5f, heightHalf - 0.5f, radius, pPaint);
			}
		},
		ELLIPSE() {
			private final RectF mRectF = new RectF();

			@Override
			public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint) {
				this.mRectF.set(0, 0, pCanvas.getWidth() - 1, pCanvas.getWidth() - 1);
				pCanvas.drawOval(this.mRectF, pPaint);
			}
		},
		RECTANGLE() {
			@Override
			public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint) {
				pCanvas.drawRect(0, 0, pCanvas.getWidth() - 1, pCanvas.getHeight() - 1, pPaint);
			}
		};

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods from SuperClass/Interfaces
		// ===========================================================

		public abstract void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint);

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
