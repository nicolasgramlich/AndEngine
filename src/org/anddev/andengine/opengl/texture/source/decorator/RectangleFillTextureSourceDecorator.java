package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Canvas;

/**
 * @author Nicolas Gramlich
 * @since 11:34:01 - 24.08.2010
 */
public class RectangleFillTextureSourceDecorator extends FillTextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangleFillTextureSourceDecorator(final ITextureSource pTextureSource, final int pFillColor) {
		super(pTextureSource, pFillColor);
	}

	public RectangleFillTextureSourceDecorator(final ITextureSource pTextureSource, final float pFillColorRed, final float pFillColorGreen, final float pFillColorBlue) {
		super(pTextureSource, pFillColorRed, pFillColorGreen, pFillColorBlue);
	}

	@Override
	public RectangleFillTextureSourceDecorator clone() {
		return new RectangleFillTextureSourceDecorator(this.mTextureSource, this.mFillColor);
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
