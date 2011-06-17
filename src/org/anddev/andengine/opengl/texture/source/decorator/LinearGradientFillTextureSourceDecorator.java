package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.shape.ITextureSourceDecoratorShape;

import android.graphics.LinearGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;

/**
 * @author Nicolas Gramlich
 * @since 19:21:24 - 05.11.2010
 */
public class LinearGradientFillTextureSourceDecorator extends BaseShapeTextureSourceDecorator {
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

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection) {
		this(pTextureSource, pTextureSourceDecoratorShape, pFromColor, pToColor, pLinearGradientDirection, null);
	}

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		this(pTextureSource, pTextureSourceDecoratorShape, new int[] { pFromColor, pToColor }, null, pLinearGradientDirection, pTextureSourceDecoratorOptions);
	}

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int[] pColors, final float[] pPositions, final LinearGradientDirection pLinearGradientDirection) {
		this(pTextureSource, pTextureSourceDecoratorShape, pColors, pPositions, pLinearGradientDirection, null);
	}

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int[] pColors, final float[] pPositions, final LinearGradientDirection pLinearGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mColors = pColors;
		this.mPositions = pPositions;
		this.mLinearGradientDirection = pLinearGradientDirection;

		this.mPaint.setStyle(Style.FILL);

		final int right = pTextureSource.getWidth() - 1;
		final int bottom = pTextureSource.getHeight() - 1;

		final float fromX = pLinearGradientDirection.getFromX(right);
		final float fromY = pLinearGradientDirection.getFromY(bottom);
		final float toX = pLinearGradientDirection.getToX(right);
		final float toY = pLinearGradientDirection.getToY(bottom);

		this.mPaint.setShader(new LinearGradient(fromX, fromY, toX, toY, pColors, pPositions, TileMode.CLAMP));
	}

	@Override
	public LinearGradientFillTextureSourceDecorator clone() {
		return new LinearGradientFillTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mColors, this.mPositions, this.mLinearGradientDirection, this.mTextureSourceDecoratorOptions);
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
