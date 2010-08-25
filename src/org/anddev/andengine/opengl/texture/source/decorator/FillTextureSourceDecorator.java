package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.ColorUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 11:34:01 - 24.08.2010
 */
public class FillTextureSourceDecorator extends TextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mFillPaint = new Paint();
	private final int mFillColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor) {
		super(pTextureSource);
		this.mFillColor = pColor;

		this.mFillPaint.setStyle(Style.FILL);
		this.mFillPaint.setColor(pColor);
	}

	public FillTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue) {
		this(pTextureSource, ColorUtils.RGBToColor(pRed, pGreen, pBlue));
	}

	@Override
	public FillTextureSourceDecorator clone() {
		return new FillTextureSourceDecorator(this.mTextureSource, this.mFillColor);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas) {
		pCanvas.drawRect(0, 0, pCanvas.getWidth() - 1, pCanvas.getHeight() - 1, this.mFillPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
