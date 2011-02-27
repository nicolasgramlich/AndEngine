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
	protected final int mFromColor;
	protected final int mToColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection) {
		this(pTextureSource, pTextureSourceDecoratorShape, pFromColor, pToColor, pLinearGradientDirection, null);
	}

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final ITextureSourceDecoratorShape pTextureSourceDecoratorShape, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection, final TextureSourceDecoratorOptions pTextureSourceDecoratorOptions) {
		super(pTextureSource, pTextureSourceDecoratorShape, pTextureSourceDecoratorOptions);
		this.mFromColor = pFromColor;
		this.mToColor = pToColor;
		this.mLinearGradientDirection = pLinearGradientDirection;

		this.mPaint.setStyle(Style.FILL);

		final int width = pTextureSource.getWidth();
		final int height = pTextureSource.getHeight();

		final float fromX = pLinearGradientDirection.getFromX() * width;
		final float fromY = pLinearGradientDirection.getFromY() * height;
		final float toX = pLinearGradientDirection.getToX() * width;
		final float toY = pLinearGradientDirection.getToY() * height;

		this.mPaint.setShader(new LinearGradient(fromX, fromY, toX, toY, pFromColor, pToColor, TileMode.CLAMP));
	}

	@Override
	public LinearGradientFillTextureSourceDecorator clone() {
		return new LinearGradientFillTextureSourceDecorator(this.mTextureSource, this.mTextureSourceDecoratorShape, this.mFromColor, this.mToColor, this.mLinearGradientDirection, this.mTextureSourceDecoratorOptions);
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

		LEFT_TO_RIGHT(0, 0, 1, 0),
		RIGHT_TO_LEFT(1, 0, 0, 0),
		BOTTOM_TO_TOP(0, 0, 0, 1),
		TOP_TO_BOTTOM(0, 1, 0, 0),
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

		final int getFromX() {
			return this.mFromX;
		}

		final int getFromY() {
			return this.mFromY;
		}

		final int getToX() {
			return this.mToX;
		}

		final int getToY() {
			return this.mToY;
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
