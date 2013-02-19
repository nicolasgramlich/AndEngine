package org.andengine.opengl.texture.atlas.bitmap.source;

import org.andengine.opengl.util.GLHelper;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:41:39 - 07.06.2011
 */
public class ColorSwapBitmapTextureAtlasSource extends BaseBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int TOLERANCE_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mColorKeyColorARGBPackedInt;
	protected final int mTolerance;
	protected final int mColorSwapColorARGBPackedInt;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorSwapBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final Color pColorKeyColor, final Color pColorSwapColor) {
		this(pBitmapTextureAtlasSource, pColorKeyColor.getARGBPackedInt(), pColorSwapColor.getARGBPackedInt());
	}

	public ColorSwapBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pColorKeyColorARGBPackedInt, final int pColorSwapColorARGBPackedInt) {
		this(pBitmapTextureAtlasSource, pColorKeyColorARGBPackedInt, ColorSwapBitmapTextureAtlasSource.TOLERANCE_DEFAULT, pColorSwapColorARGBPackedInt);
	}

	public ColorSwapBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final Color pColorKeyColor, final int pTolerance, final Color pColorSwapColor) {
		this(pBitmapTextureAtlasSource, pColorKeyColor.getARGBPackedInt(), pTolerance, pColorSwapColor.getARGBPackedInt());
	}

	public ColorSwapBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pColorKeyColorARGBPackedInt, final int pTolerance, final int pColorSwapColorARGBPackedInt) {
		super(pBitmapTextureAtlasSource);

		this.mColorKeyColorARGBPackedInt = pColorKeyColorARGBPackedInt;
		this.mTolerance = pTolerance;
		this.mColorSwapColorARGBPackedInt = pColorSwapColorARGBPackedInt;
	}

	@Override
	public ColorSwapBitmapTextureAtlasSource deepCopy() {
		return new ColorSwapBitmapTextureAtlasSource(this.mBitmapTextureAtlasSource, this.mColorKeyColorARGBPackedInt, this.mTolerance, this.mColorSwapColorARGBPackedInt);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		return this.swapColor(super.onLoadBitmap(pBitmapConfig));
	}

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig, final boolean pMutable) {
		return this.swapColor(super.onLoadBitmap(pBitmapConfig, pMutable));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected Bitmap swapColor(final Bitmap pBitmap) {
		final Config config = pBitmap.getConfig();
		switch (config) {
			case ARGB_8888:
				return ColorSwapBitmapTextureAtlasSource.swapColorARGB_8888(pBitmap, this.mColorKeyColorARGBPackedInt, this.mTolerance, this.mColorSwapColorARGBPackedInt);
			default:
				Debug.w("Unexpected " + Bitmap.Config.class.getSimpleName() + ": '" + config + "'.");
				return pBitmap;
		}
	}

	protected static final Bitmap swapColorARGB_8888(final Bitmap pBitmap, final int pColorKeyColorARGBPackedInt, final int pTolerance, final int pColorSwapColorARGBPackedInt) {
		final int[] pixelsARGB_8888 = GLHelper.getPixelsARGB_8888(pBitmap);
		pBitmap.recycle();

		final int keyRed = (pColorKeyColorARGBPackedInt >> Color.ARGB_PACKED_RED_SHIFT) & 0xFF;
		final int keyGreen = (pColorKeyColorARGBPackedInt >> Color.ARGB_PACKED_GREEN_SHIFT) & 0xFF;
		final int keyBlue = (pColorKeyColorARGBPackedInt >> Color.ARGB_PACKED_BLUE_SHIFT) & 0xFF;
		final int keyAlpha = (pColorKeyColorARGBPackedInt >> Color.ARGB_PACKED_ALPHA_SHIFT) & 0xFF;

		for (int i = pixelsARGB_8888.length - 1; i >= 0; i--) {
			final int pixelARGB_8888 = pixelsARGB_8888[i];

			final int red = (pixelARGB_8888 >> Color.ARGB_PACKED_RED_SHIFT) & 0xFF;
			if (Math.abs(red - keyRed) <= pTolerance) {
				final int green = (pixelARGB_8888 >> Color.ARGB_PACKED_GREEN_SHIFT) & 0xFF;
				if (Math.abs(green - keyGreen) <= pTolerance) {
					final int blue = (pixelARGB_8888 >> Color.ARGB_PACKED_BLUE_SHIFT) & 0xFF;
					if (Math.abs(blue - keyBlue) <= pTolerance) {
						final int alpha = (pixelARGB_8888 >> Color.ARGB_PACKED_ALPHA_SHIFT) & 0xFF;
						if (Math.abs(alpha - keyAlpha) <= pTolerance) {
							pixelsARGB_8888[i] = pColorSwapColorARGBPackedInt;
						}
					}
				}
			}
		}

		return Bitmap.createBitmap(pixelsARGB_8888, pBitmap.getWidth(), pBitmap.getHeight(), Config.ARGB_8888);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
