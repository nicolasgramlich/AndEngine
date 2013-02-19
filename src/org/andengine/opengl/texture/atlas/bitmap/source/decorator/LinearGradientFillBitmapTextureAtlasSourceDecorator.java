package org.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.IBitmapTextureAtlasSourceDecoratorShape;

import android.graphics.LinearGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:21:24 - 05.11.2010
 */
public class LinearGradientFillBitmapTextureAtlasSourceDecorator extends BaseShapeBitmapTextureAtlasSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final LinearGradientDirection mLinearGradientDirection;
	protected final int[] mColors;
	protected final float[] mPositions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LinearGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pFromColor, pToColor, pLinearGradientDirection, null);
	}

	public LinearGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, new int[] { pFromColor, pToColor }, null, pLinearGradientDirection, pTextureAtlasSourceDecoratorOptions);
	}

	public LinearGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int[] pColors, final float[] pPositions, final LinearGradientDirection pLinearGradientDirection) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColors, pPositions, pLinearGradientDirection, null);
	}

	public LinearGradientFillBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int[] pColors, final float[] pPositions, final LinearGradientDirection pLinearGradientDirection, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		super(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pTextureAtlasSourceDecoratorOptions);
		this.mColors = pColors;
		this.mPositions = pPositions;
		this.mLinearGradientDirection = pLinearGradientDirection;

		this.mPaint.setStyle(Style.FILL);

		final int right = pBitmapTextureAtlasSource.getTextureWidth() - 1;
		final int bottom = pBitmapTextureAtlasSource.getTextureHeight() - 1;

		final float fromX = pLinearGradientDirection.getFromX(right);
		final float fromY = pLinearGradientDirection.getFromY(bottom);
		final float toX = pLinearGradientDirection.getToX(right);
		final float toY = pLinearGradientDirection.getToY(bottom);

		this.mPaint.setShader(new LinearGradient(fromX, fromY, toX, toY, pColors, pPositions, TileMode.CLAMP));
	}

	@Override
	public LinearGradientFillBitmapTextureAtlasSourceDecorator deepCopy() {
		return new LinearGradientFillBitmapTextureAtlasSourceDecorator(this.mBitmapTextureAtlasSource, this.mBitmapTextureAtlasSourceDecoratorShape, this.mColors, this.mPositions, this.mLinearGradientDirection, this.mTextureAtlasSourceDecoratorOptions);
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

	public static enum LinearGradientDirection {
		// ===========================================================
		// Elements
		// ===========================================================

		LEFT_TO_RIGHT(1, 0, 0, 0),
		RIGHT_TO_LEFT(0, 0, 1, 0),
		BOTTOM_TO_TOP(0, 1, 0, 0),
		TOP_TO_BOTTOM(0, 0, 0, 1),
		TOPLEFT_TO_BOTTOMRIGHT(0, 0, 1, 1),
		BOTTOMRIGHT_TO_TOPLEFT(1, 1, 0, 0),
		TOPRIGHT_TO_BOTTOMLEFT(1, 0, 0, 1),
		BOTTOMLEFT_TO_TOPRIGHT(0, 1, 1, 0);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mFromX;
		private final int mFromY;
		private final int mToX;
		private final int mToY;

		// ===========================================================
		// Constructors
		// ===========================================================

		private LinearGradientDirection(final int pFromX, final int pFromY, final int pToX, final int pToY) {
			this.mFromX = pFromX;
			this.mFromY = pFromY;
			this.mToX = pToX;
			this.mToY = pToY;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		final int getFromX(int pRight) {
			return this.mFromX * pRight;
		}

		final int getFromY(int pBottom) {
			return this.mFromY * pBottom;
		}

		final int getToX(int pRight) {
			return this.mToX * pRight;
		}

		final int getToY(int pBottom) {
			return this.mToY * pBottom;
		}

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
