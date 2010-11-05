package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Canvas;

/**
 * @author Nicolas Gramlich
 * @since 11:34:01 - 24.08.2010
 */
public class RectangleLinearGradientFillTextureSourceDecorator extends LinearGradientFillTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangleLinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final int pFromColor, final int pToColor, final LinearGradientDirection pLinearGradientDirection) {
		super(pTextureSource, pFromColor, pToColor, pLinearGradientDirection);
	}

	public RectangleLinearGradientFillTextureSourceDecorator(final ITextureSource pTextureSource, final float pFromRed, final float pFromGreen, final float pFromBlue, final float pToRed, final float pToGreen, final float pToBlue, final LinearGradientDirection pLinearGradientDirection) {
		super(pTextureSource, pFromRed, pFromGreen, pFromBlue, pToRed, pToGreen, pToBlue, pLinearGradientDirection);
	}

	@Override
	public RectangleLinearGradientFillTextureSourceDecorator clone() {
		return new RectangleLinearGradientFillTextureSourceDecorator(this.mTextureSource, this.mFromColor, this.mToColor, this.mLinearGradientDirection);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas) {
		pCanvas.drawRect(0, 0, pCanvas.getWidth() - 1, pCanvas.getHeight() - 1, this.mPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
