package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;
import org.anddev.andengine.util.ArrayUtils;

import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;

/**
 * @author Nicolas Gramlich
 * @since 19:21:24 - 05.11.2010
 */
public class RadialGradientFillTextureSourceDecorator extends BaseShapeTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final RadialGradientDirection mRadialGradientDirection;
	protected final int[] mColors;
	protected final float[] mPositions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RadialGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final RadialGradientDirection pRadialGradientDirection) {
		this(pTextureSource, pTextureSourceDecoratorShape, pFromColor, pToColor, pRadialGradientDirection, null);
	}

	public RadialGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final RadialGradientDirection pRadialGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this(pTextureSource, pTextureSourceDecoratorShape, new int[] { pFromColor, pToColor }, null, pRadialGradientDirection, pTextureSourceDecoratorOptions);
	}

	public RadialGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape,  final int[] pColors, final float[] pPositions, final RadialGradientDirection pRadialGradientDirection) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColors, pPositions, pRadialGradientDirection, null);
	}

	public RadialGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int[] pColors, final float[] pPositions, final RadialGradientDirection pRadialGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mColors = pColors;
		this.mPositions = pPositions;
		this.mRadialGradientDirection = pRadialGradientDirection;

		this.mPaint.setStyle(Style.FILL);

		final int width = pTextureSource.getWidth();
		final int height = pTextureSource.getHeight();

		final float centerX = width * 0.5f;
		final float centerY = height * 0.5f;

		final float radius = Math.max(centerX, centerY);

		switch(pRadialGradientDirection) {
			case INSIDE_OUT:
				this.mPaint.setShader(new RadialGradient(centerX, centerY, radius, pColors, pPositions, TileMode.CLAMP));
				break;
			case OUTSIDE_IN:
				ArrayUtils.reverse(pPositions);
				this.mPaint.setShader(new RadialGradient(centerX, centerY, radius, pColors, pPositions, TileMode.CLAMP));
				ArrayUtils.reverse(pPositions);
				break;
		}
	}

	@Override
	public RadialGradientFillTextureSourceDecorator clone() {
		return new RadialGradientFillTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mColors, this.mPositions, this.mRadialGradientDirection, this.mTextureSourceDecoratorOptions);
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
