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
	protected TextureSourceDecoratorOptions mTextureSourceDecoratorOptions;
	protected Paint mPaint = new Paint();

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureSourceDecorator(final ITextureSource pTextureSource) {
		this(pTextureSource, new TextureSourceDecoratorOptions());
	}

	public BaseTextureSourceDecorator(final ITextureSource pTextureSource, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this.mTextureSource = pTextureSource;
		this.mTextureSourceDecoratorOptions = (pTextureSourceDecoratorOptions == null) ? new TextureSourceDecoratorOptions() : pTextureSourceDecoratorOptions;
		this.mPaint.setAntiAlias(this.mTextureSourceDecoratorOptions.getAntiAliasing());
	}

	@Override
	public abstract BaseTextureSourceDecorator clone();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Paint getPaint() {
		return this.mPaint;
	}

	public void setPaint(final Paint pPaint) {
		this.mPaint = pPaint;
	}

	public TextureSourceDecoratorOptions getTextureSourceDecoratorOptions() {
		return this.mTextureSourceDecoratorOptions;
	}

	public void setTextureSourceDecoratorOptions(final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this.mTextureSourceDecoratorOptions = pTextureSourceDecoratorOptions;
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

	public static class TextureSourceDecoratorOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final TextureSourceDecoratorOptions DEFAULT = new TextureSourceDecoratorOptions();

		// ===========================================================
		// Fields
		// ===========================================================

		private float mInsetLeft = 0.0f;
		private float mInsetRight = 0.0f;
		private float mInsetTop = 0.0f;
		private float mInsetBottom = 0.0f;

		private boolean mAntiAliasing;

		// ===========================================================
		// Constructors
		// ===========================================================

		@Override
		protected TextureSourceDecoratorOptions clone() {
			final TextureSourceDecoratorOptions textureSourceDecoratorOptions = new TextureSourceDecoratorOptions();
			textureSourceDecoratorOptions.setInsets(this.mInsetLeft, this.mInsetTop, this.mInsetRight, this.mInsetBottom);
			textureSourceDecoratorOptions.setAntiAliasing(this.mAntiAliasing);
			return textureSourceDecoratorOptions;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public boolean getAntiAliasing() {
			return this.mAntiAliasing;
		}

		public float getInsetLeft() {
			return this.mInsetLeft;
		}

		public float getInsetRight() {
			return this.mInsetRight;
		}

		public float getInsetTop() {
			return this.mInsetTop;
		}

		public float getInsetBottom() {
			return this.mInsetBottom;
		}

		public TextureSourceDecoratorOptions setAntiAliasing(final boolean pAntiAliasing) {
			this.mAntiAliasing = pAntiAliasing;
			return this;
		}

		public TextureSourceDecoratorOptions setInsetLeft(final float pInsetLeft) {
			this.mInsetLeft = pInsetLeft;
			return this;
		}

		public TextureSourceDecoratorOptions setInsetRight(final float pInsetRight) {
			this.mInsetRight = pInsetRight;
			return this;
		}

		public TextureSourceDecoratorOptions setInsetTop(final float pInsetTop) {
			this.mInsetTop = pInsetTop;
			return this;
		}

		public TextureSourceDecoratorOptions setInsetBottom(final float pInsetBottom) {
			this.mInsetBottom = pInsetBottom;
			return this;
		}

		public TextureSourceDecoratorOptions setInsets(final float pInsets) {
			return this.setInsets(pInsets, pInsets, pInsets, pInsets);
		}

		public TextureSourceDecoratorOptions setInsets(final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom) {
			this.mInsetLeft = pInsetLeft;
			this.mInsetTop = pInsetTop;
			this.mInsetRight = pInsetRight;
			this.mInsetBottom = pInsetBottom;
			return this;
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
