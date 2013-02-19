package org.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.andengine.opengl.texture.atlas.bitmap.source.BaseBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:43:29 - 06.08.2010
 */
public abstract class BaseBitmapTextureAtlasSourceDecorator extends BaseBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected TextureAtlasSourceDecoratorOptions mTextureAtlasSourceDecoratorOptions;
	protected Paint mPaint = new Paint();

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource) {
		this(pBitmapTextureAtlasSource, new TextureAtlasSourceDecoratorOptions());
	}

	public BaseBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		super(pBitmapTextureAtlasSource);

		this.mTextureAtlasSourceDecoratorOptions = (pTextureAtlasSourceDecoratorOptions == null) ? new TextureAtlasSourceDecoratorOptions() : pTextureAtlasSourceDecoratorOptions;
		this.mPaint.setAntiAlias(this.mTextureAtlasSourceDecoratorOptions.getAntiAliasing());
	}

	@Override
	public abstract BaseBitmapTextureAtlasSourceDecorator deepCopy();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Paint getPaint() {
		return this.mPaint;
	}

	public void setPaint(final Paint pPaint) {
		this.mPaint = pPaint;
	}

	public TextureAtlasSourceDecoratorOptions getTextureAtlasSourceDecoratorOptions() {
		return this.mTextureAtlasSourceDecoratorOptions;
	}

	public void setTextureAtlasSourceDecoratorOptions(final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this.mTextureAtlasSourceDecoratorOptions = pTextureAtlasSourceDecoratorOptions;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onDecorateBitmap(final Canvas pCanvas) throws Exception;

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		final Bitmap bitmap = this.onLoadBitmap(pBitmapConfig, true);

		final Canvas canvas = new Canvas(bitmap);
		try {
			this.onDecorateBitmap(canvas);
		} catch (final Exception e) {
			Debug.e(e);
		}
		return bitmap;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureAtlasSourceDecoratorOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final TextureAtlasSourceDecoratorOptions DEFAULT = new TextureAtlasSourceDecoratorOptions();

		// ===========================================================
		// Fields
		// ===========================================================

		private float mInsetLeft = 0.25f;
		private float mInsetRight = 0.25f;
		private float mInsetTop = 0.25f;
		private float mInsetBottom = 0.25f;

		private boolean mAntiAliasing;

		// ===========================================================
		// Constructors
		// ===========================================================

		protected TextureAtlasSourceDecoratorOptions deepCopy() {
			final TextureAtlasSourceDecoratorOptions textureSourceDecoratorOptions = new TextureAtlasSourceDecoratorOptions();
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

		public TextureAtlasSourceDecoratorOptions setAntiAliasing(final boolean pAntiAliasing) {
			this.mAntiAliasing = pAntiAliasing;
			return this;
		}

		public TextureAtlasSourceDecoratorOptions setInsetLeft(final float pInsetLeft) {
			this.mInsetLeft = pInsetLeft;
			return this;
		}

		public TextureAtlasSourceDecoratorOptions setInsetRight(final float pInsetRight) {
			this.mInsetRight = pInsetRight;
			return this;
		}

		public TextureAtlasSourceDecoratorOptions setInsetTop(final float pInsetTop) {
			this.mInsetTop = pInsetTop;
			return this;
		}

		public TextureAtlasSourceDecoratorOptions setInsetBottom(final float pInsetBottom) {
			this.mInsetBottom = pInsetBottom;
			return this;
		}

		public TextureAtlasSourceDecoratorOptions setInsets(final float pInsets) {
			return this.setInsets(pInsets, pInsets, pInsets, pInsets);
		}

		public TextureAtlasSourceDecoratorOptions setInsets(final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom) {
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
