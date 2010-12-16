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
		this.mTextureSourceDecoratorShape.onDecorateBitmap(pCanvas, this.mPaint, null);
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
			public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final DecoratorOptions pDecoratorOptions) {
				final float widthHalf = pCanvas.getWidth() / 2;
				final float heightHalf = pCanvas.getHeight() / 2;
				final float radius = Math.min(widthHalf, heightHalf);
				// TODO Use DecoratorOptions
				pCanvas.drawCircle(widthHalf - 0.5f, heightHalf - 0.5f, radius, pPaint);
			}
		},
		ELLIPSE() {
			private final RectF mRectF = new RectF();

			@Override
			public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final DecoratorOptions pDecoratorOptions) {
				this.mRectF.set(0, 0, pCanvas.getWidth() - 1, pCanvas.getWidth() - 1);
				// TODO Use DecoratorOptions
				pCanvas.drawOval(this.mRectF, pPaint);
			}
		},
		RECTANGLE() {
			@Override
			public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final DecoratorOptions pDecoratorOptions) {
				final float left = pDecoratorOptions.getInsetLeft();
				final float top = pDecoratorOptions.getInsetTop();
				final float right = pCanvas.getWidth() - 1 - pDecoratorOptions.getInsetRight();
				final float bottom = pCanvas.getHeight() - 1 - pDecoratorOptions.getInsetBottom();
				pCanvas.drawRect(left, top, right, bottom, pPaint);
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

		public abstract void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final DecoratorOptions pDecoratorOptions);

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
