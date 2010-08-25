package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.ColorUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 15:30:42 - 25.08.2010
 */
public class CircleFillTextureSourceDecorator extends TextureSourceDecorator {
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

	public CircleFillTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor) {
		super(pTextureSource);
		this.mFillColor = pColor;

		this.mFillPaint.setStyle(Style.FILL);
		this.mFillPaint.setColor(pColor);
	}

	public CircleFillTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue) {
		this(pTextureSource, ColorUtils.RGBToColor(pRed, pGreen, pBlue));
	}

	@Override
	public CircleFillTextureSourceDecorator clone() {
		return new CircleFillTextureSourceDecorator(this.mTextureSource, this.mFillColor);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas) {
		final float centerX = this.getWidth() / 2;
		final float centerY = this.getHeight() / 2;
		final float radius = Math.min(centerX, centerY);
		pCanvas.drawCircle(centerX, centerY, radius, this.mFillPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
