package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Nicolas Gramlich
 * @since 16:43:29 - 06.08.2010
 */
public abstract class BaseTextureSourceDecorator implements ITextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITextureSource mTextureSource;
	protected final Paint mPaint = new Paint();
	protected final DecoratorOptions mDecoratorOptions = new DecoratorOptions();
	protected final boolean mAntiAliasing;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureSourceDecorator(final ITextureSource pTextureSource) {
		this(pTextureSource, false);
	}

	public BaseTextureSourceDecorator(final ITextureSource pTextureSource, final boolean pAntiAliasing) {
		this.mTextureSource = pTextureSource;
		this.mAntiAliasing = pAntiAliasing;
		this.mPaint.setAntiAlias(pAntiAliasing);
	}

	@Override
	public abstract BaseTextureSourceDecorator clone();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Paint getPaint() {
		return this.mPaint;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onDecorateBitmap(final Canvas pCanvas);

	@Override
	public int getWidth() {
		return this.mTextureSource.getWidth();
	}

	@Override
	public int getHeight() {
		return this.mTextureSource.getHeight();
	}

	@Override
	public Bitmap onLoadBitmap() {
		final Bitmap bitmap = this.ensureLoadedBitmapIsMutable(this.mTextureSource.onLoadBitmap());

		final Canvas canvas = new Canvas(bitmap);
		this.onDecorateBitmap(canvas);
		return bitmap;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private Bitmap ensureLoadedBitmapIsMutable(final Bitmap pBitmap) {
		if(pBitmap.isMutable()) {
			return pBitmap;
		} else {
			final Bitmap mutableBitmap = pBitmap.copy(pBitmap.getConfig(), true);
			pBitmap.recycle();
			return mutableBitmap;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class DecoratorOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private float mInsetLeft = 0.0f;
		private float mInsetRight = 0.0f;
		private float mInsetTop = 0.0f;
		private float mInsetBottom = 0.0f;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public float getInsetLeft() {
			return this.mInsetLeft;
		}
		
		public void setInsetLeft(final float pInsetLeft) {
			this.mInsetLeft = pInsetLeft;
		}
		
		public float getInsetRight() {
			return this.mInsetRight;
		}
		
		public void setInsetRight(final float pInsetRight) {
			this.mInsetRight = pInsetRight;
		}
		
		public float getInsetTop() {
			return this.mInsetTop;
		}
		
		public void setInsetTop(final float pInsetTop) {
			this.mInsetTop = pInsetTop;
		}
		
		public float getInsetBottom() {
			return this.mInsetBottom;
		}
		
		public void setInsetBottom(final float pInsetBottom) {
			this.mInsetBottom = pInsetBottom;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
