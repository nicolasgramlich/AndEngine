package org.anddev.andengine.opengl.texture.source.decorator;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.ColorUtils;

import android.graphics.AvoidXfermode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.AvoidXfermode.Mode;

/**
 * @author Nicolas Gramlich
 * @since 22:16:41 - 06.08.2010
 */
public class ColorKeyTextureSourceDecorator extends TextureSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int TOLERANCE_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mRemoveColotPaint = new Paint();
	private final int mColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor) {
		this(pTextureSource, pColor, TOLERANCE_DEFAULT);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor, final int pTolerance) {
		super(pTextureSource);
		this.mColor = pColor;
		this.mRemoveColotPaint.setXfermode(new AvoidXfermode(pColor, pTolerance, Mode.TARGET));
		this.mRemoveColotPaint.setColor(Color.TRANSPARENT);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue) {
		this(pTextureSource, pRed, pGreen, pBlue, TOLERANCE_DEFAULT);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue, final int pTolerance) {
		this(pTextureSource, ColorUtils.RGBToColor(pRed, pGreen, pBlue));
	}

	@Override
	public ColorKeyTextureSourceDecorator clone() {
		return new ColorKeyTextureSourceDecorator(this.mTextureSource, this.mColor);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas) {
		pCanvas.drawRect(0, 0, pCanvas.getWidth() - 1, pCanvas.getHeight() - 1, this.mRemoveColotPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
