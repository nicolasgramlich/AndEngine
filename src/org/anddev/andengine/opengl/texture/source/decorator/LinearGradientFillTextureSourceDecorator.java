package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.ColorUtils;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;

/**
 * @author Nicolas Gramlich
 * @since 11:34:01 - 24.08.2010
 */
public class LinearGradientFillTextureSourceDecorator extends TextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mLinearGradientPaint = new Paint();
	private final LinearGradientDirection mLinearGradientDirection;
	private final int mFromColor;
	private final int mToColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection) {
		super(pTextureSource);
		this.mFromColor = pFromColor;
		this.mToColor = pToColor;
		this.mLinearGradientDirection = pLinearGradientDirection;

		this.mLinearGradientPaint.setStyle(Style.FILL);
		
		final int width = pTextureSource.getWidth();
		final int height = pTextureSource.getHeight();
		
		final float fromX = pLinearGradientDirection.getFromX() * width;
		final float fromY = pLinearGradientDirection.getFromY() * height;
		final float toX = pLinearGradientDirection.getToX() * width;
		final float toY = pLinearGradientDirection.getToY() * height;
		
		this.mLinearGradientPaint.setShader(new LinearGradient(fromX, fromY, toX, toY, pFromColor, pToColor, TileMode.CLAMP));
	}

	public LinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final float pFromRed, final float pFromGreen, final float pFromBlue, final float pToRed, final float pToGreen, final float pToBlue, final LinearGradientDirection pLinearGradientDirection) {
		this(pTextureSource, ColorUtils.RGBToColor(pFromRed, pFromGreen, pFromBlue), ColorUtils.RGBToColor(pToRed, pToGreen, pToBlue), pLinearGradientDirection);
	}

	@Override
	public LinearGradientFillTextureSourceDecorator clone() {
		return new LinearGradientFillTextureSourceDecorator(this.mTextureSource, this.mFromColor, this.mToColor, this.mLinearGradientDirection);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas) {
		pCanvas.drawRect(0, 0, pCanvas.getWidth() - 1, pCanvas.getHeight() - 1, this.mLinearGradientPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum LinearGradientDirection {
		LEFT_TO_RIGHT(0, 0, 1, 0),
		RIGHT_TO_LEFT(1, 0, 0, 0),
		BOTTOM_TO_TOP(0, 0, 0, 1),
		TOP_TO_BOTTOM(0, 1, 0, 0),
		TOPLEFT_TO_BOTTOMRIGHT(0, 0, 1, 1),
		BOTTOMRIGHT_TO_TOPLEFT(1, 1, 0, 0),
		TOPRIGHT_TO_BOTTOMLEFT(1, 0, 0, 1),
		BOTTOMLEFT_TO_TOPRIGHT(0, 1, 1, 0);

		private final int mFromX;
		private final int mFromY;
		private final int mToX;
		private final int mToY;

		private LinearGradientDirection(final int pFromX, final int pFromY, final int pToX, final int pToY) {
			this.mFromX = pFromX;
			this.mFromY = pFromY;
			this.mToX = pToX;
			this.mToY = pToY;
		}

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
	}
}
