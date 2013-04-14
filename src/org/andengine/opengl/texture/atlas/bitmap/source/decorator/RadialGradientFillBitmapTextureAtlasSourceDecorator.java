package org.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.IBitmapTextureAtlasSourceDecoratorShape;
import org.andengine.util.adt.array.ArrayUtils;

import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:21:24 - 05.11.2010
 */
public class RadialGradientFillBitmapTextureAtlasSourceDecorator extends BaseShapeBitmapTextureAtlasSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float[] POSITIONS_DEFAULT = new float[] { 0.0f, 1.0f };

	// ===========================================================
	// Fields
	// ===========================================================

	protected final RadialGradientDirection mRadialGradientDirection;
	protected final int[] mColors;
	protected final float[] mPositions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RadialGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pFromColor, final int pToColor, final RadialGradientDirection pRadialGradientDirection) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pFromColor, pToColor, pRadialGradientDirection, null);
	}

	public RadialGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pFromColor, final int pToColor, final RadialGradientDirection pRadialGradientDirection, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, new int[] { pFromColor, pToColor }, POSITIONS_DEFAULT, pRadialGradientDirection, pTextureAtlasSourceDecoratorOptions);
	}

	public RadialGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int[] pColors, final float[] pPositions, final RadialGradientDirection pRadialGradientDirection) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColors, pPositions, pRadialGradientDirection, null);
	}

	public RadialGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int[] pColors, final float[] pPositions, final RadialGradientDirection pRadialGradientDirection, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		super(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pTextureAtlasSourceDecoratorOptions);
		this.mColors = pColors;
		this.mPositions = pPositions;
		this.mRadialGradientDirection = pRadialGradientDirection;

		this.mPaint.setStyle(Style.FILL);

		final int width = pBitmapTextureAtlasSource.getTextureWidth();
		final int height = pBitmapTextureAtlasSource.getTextureHeight();

		final float centerX = width * 0.5f;
		final float centerY = height * 0.5f;

		final float radius = Math.max(centerX, centerY);

		switch (pRadialGradientDirection) {
			case INSIDE_OUT:
				this.mPaint.setShader(new RadialGradient(centerX, centerY, radius, pColors, pPositions, TileMode.CLAMP));
				break;
			case OUTSIDE_IN:
				ArrayUtils.reverse(pColors);
				this.mPaint.setShader(new RadialGradient(centerX, centerY, radius, pColors, pPositions, TileMode.CLAMP));
				break;
		}
	}

	@Override
	public RadialGradientFillBitmapTextureAtlasSourceDecorator deepCopy() {
		return new RadialGradientFillBitmapTextureAtlasSourceDecorator(this.mBitmapTextureAtlasSource, this.mBitmapTextureAtlasSourceDecoratorShape, this.mColors, this.mPositions, this.mRadialGradientDirection, this.mTextureAtlasSourceDecoratorOptions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum RadialGradientDirection {
		// ===========================================================
		// Elements
		// ===========================================================

		INSIDE_OUT,
		OUTSIDE_IN;

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

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
