package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.ColorUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * @author Nicolas Gramlich
 * @since 15:33:25 - 25.08.2010
 */
public class CircleOutlineTextureSourceDecorator extends TextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mOutlinePaint = new Paint();
	private final int mOutlineColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CircleOutlineTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor) {
		super(pTextureSource);
		this.mOutlineColor = pColor;

		this.mOutlinePaint.setStyle(Style.STROKE);
		this.mOutlinePaint.setColor(pColor);
	}

	public CircleOutlineTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue) {
		this(pTextureSource, ColorUtils.RGBToColor(pRed, pGreen, pBlue));
	}

	@Override
	public CircleOutlineTextureSourceDecorator clone() {
		return new CircleOutlineTextureSourceDecorator(this.mTextureSource, this.mOutlineColor);
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
		pCanvas.drawCircle(centerX, centerY, radius, this.mOutlinePaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
