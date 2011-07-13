package org.anddev.andengine.opengl.texture.bitmap.source.decorator;

import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.decorator.shape.IBitmapTextureSourceDecoratorShape;
import org.anddev.andengine.util.ArrayUtils;

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
public class RadialGradientFillBitmapTextureSourceDecorator extends BaseShapeBitmapTextureSourceDecorator {
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

	public RadialGradientFillBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final RadialGradientDirection pRadialGradientDirection) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pFromColor, pToColor, pRadialGradientDirection, null);
	}

	public RadialGradientFillBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final RadialGradientDirection pRadialGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, new int[] { pFromColor, pToColor }, POSITIONS_DEFAULT, pRadialGradientDirection, pTextureSourceDecoratorOptions);
	}

	public RadialGradientFillBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape,  final int[] pColors, final float[] pPositions, final RadialGradientDirection pRadialGradientDirection) {
		this(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pColors, pPositions, pRadialGradientDirection, null);
	}

	public RadialGradientFillBitmapTextureSourceDecorator(final IBitmapTextureSource pBitmapTextureSource, final IBitmapTextureSourceDecoratorShape pBitmapTextureSourceDecoratorShape, final int[] pColors, final float[] pPositions, final RadialGradientDirection pRadialGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pBitmapTextureSource, pBitmapTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mColors = pColors;
		this.mPositions = pPositions;
		this.mRadialGradientDirection = pRadialGradientDirection;

		this.mPaint.setStyle(Style.FILL);

		final int width = pBitmapTextureSource.getWidth();
		final int height = pBitmapTextureSource.getHeight();

		final float centerX = width * 0.5f;
		final float centerY = height * 0.5f;

		final float radius = Math.max(centerX, centerY);

		switch(pRadialGradientDirection) {
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
	public RadialGradientFillBitmapTextureSourceDecorator clone() {
		return new RadialGradientFillBitmapTextureSourceDecorator(this.mBitmapTextureSource, this.mBitmapTextureSourceDecoratorShape, this.mColors, this.mPositions, this.mRadialGradientDirection, this.mTextureSourceDecoratorOptions);
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
