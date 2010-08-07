package org.anddev.andengine.opengl.texture.source;

import org.anddev.andengine.util.ColorUtils;

import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
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

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mPaint = new Paint();
	private final Paint mTransparentPaint = new Paint();
	private final int mColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final int pColor) {
		super(pTextureSource);
		this.mColor = pColor;
		this.mTransparentPaint.setXfermode(new AvoidXfermode(pColor, 0, Mode.TARGET));
		this.mTransparentPaint.setColor(Color.TRANSPARENT);
	}

	public ColorKeyTextureSourceDecorator(final ITextureSource pTextureSource, final float pRed, final float pGreen, final float pBlue) {
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
	public Bitmap onLoadBitmap() {
		final Bitmap bitmap = super.onLoadBitmap();

		final Bitmap out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		final Canvas c = new Canvas(out);
		c.drawBitmap(bitmap, 0, 0, this.mPaint);
		c.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, this.mTransparentPaint);

		return out;
	}

	@Override
	protected void onDecorateBitmap(final Canvas pCanvas) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
