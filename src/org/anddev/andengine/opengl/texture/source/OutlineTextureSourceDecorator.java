package org.anddev.andengine.opengl.texture.source;

import org.anddev.andengine.util.ColorUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 17:21:12 - 06.08.2010
 */
public class OutlineTextureSourceDecorator extends TextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mPaint = new Paint();
	private final int mColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor) {
		super(pTextureSource);
		this.mColor = pColor;

		this.mPaint.setStyle(Style.STROKE);
		this.mPaint.setColor(pColor);
	}

	public OutlineTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue) {
		this(pTextureSource, ColorUtils.RGBToColor(pRed, pGreen, pBlue));
	}

	@Override
	public OutlineTextureSourceDecorator clone() {
		return new OutlineTextureSourceDecorator(this.mTextureSource, this.mColor);
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
