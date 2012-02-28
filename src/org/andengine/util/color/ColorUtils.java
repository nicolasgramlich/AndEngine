package org.andengine.util.color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:13:45 - 04.08.2010
 */
public class ColorUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float[] HSV_TO_COLOR = new float[3];
	private static final int HSV_TO_COLOR_HUE_INDEX = 0;
	private static final int HSV_TO_COLOR_SATURATION_INDEX = 1;
	private static final int HSV_TO_COLOR_VALUE_INDEX = 2;

	private static final int INT_BITS_TO_FLOAT_MASK = 0xFFFFFFFF;
//	private static final int INT_BITS_TO_FLOAT_MASK = 0xFEFFFFFF; // To avoid producing NaN in the int->float conversion.

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pHue [0 .. 360)
	 * @param pSaturation [0...1]
	 * @param pValue [0...1]
	 */
	public static final int convertHSVToARGBPackedInt(final float pHue, final float pSaturation, final float pValue) {
		ColorUtils.HSV_TO_COLOR[ColorUtils.HSV_TO_COLOR_HUE_INDEX] = pHue;
		ColorUtils.HSV_TO_COLOR[ColorUtils.HSV_TO_COLOR_SATURATION_INDEX] = pSaturation;
		ColorUtils.HSV_TO_COLOR[ColorUtils.HSV_TO_COLOR_VALUE_INDEX] = pValue;

		return android.graphics.Color.HSVToColor(ColorUtils.HSV_TO_COLOR);
	}

	/**
	 * @param pHue [0 .. 360)
	 * @param pSaturation [0...1]
	 * @param pValue [0...1]
	 */
	public static final Color convertHSVToColor(final float pHue, final float pSaturation, final float pValue) {
		return ColorUtils.convertARGBPackedIntToColor(ColorUtils.convertHSVToARGBPackedInt(pHue, pSaturation, pValue));
	}


	public static Color convertARGBPackedIntToColor(final int pARGBPackedInt) {
		final float alpha = ColorUtils.extractAlphaFromARGBPackedInt(pARGBPackedInt);
		final float red = ColorUtils.extractRedFromARGBPackedInt(pARGBPackedInt);
		final float green = ColorUtils.extractGreenFromARGBPackedInt(pARGBPackedInt);
		final float blue = ColorUtils.extractBlueFromARGBPackedInt(pARGBPackedInt);

		return new Color(red, green, blue, alpha);
	}

	public static Color convertABGRPackedIntToColor(final int pABGRPackedInt) {
		final float alpha = ColorUtils.extractAlphaFromABGRPackedInt(pABGRPackedInt);
		final float blue = ColorUtils.extractBlueFromABGRPackedInt(pABGRPackedInt);
		final float green = ColorUtils.extractGreenFromABGRPackedInt(pABGRPackedInt);
		final float red = ColorUtils.extractRedFromABGRPackedInt(pABGRPackedInt);

		return new Color(red, green, blue, alpha);
	}


	public static final int convertRGBAToARGBPackedInt(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return ((int)(255 * pAlpha) << Color.ARGB_PACKED_ALPHA_SHIFT) | ((int)(255 * pRed) << Color.ARGB_PACKED_RED_SHIFT) | ((int)(255 * pGreen) << Color.ARGB_PACKED_GREEN_SHIFT) | ((int)(255 * pBlue) << Color.ARGB_PACKED_BLUE_SHIFT);
	}

	public static final float convertRGBAToARGBPackedFloat(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return ColorUtils.convertPackedIntToPackedFloat(ColorUtils.convertRGBAToARGBPackedInt(pRed, pGreen, pBlue, pAlpha));
	}

	public static final int convertRGBAToABGRPackedInt(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return ((int)(255 * pAlpha) << Color.ABGR_PACKED_ALPHA_SHIFT) | ((int)(255 * pBlue) << Color.ABGR_PACKED_BLUE_SHIFT) | ((int)(255 * pGreen) << Color.ABGR_PACKED_GREEN_SHIFT) | ((int)(255 * pRed) << Color.ABGR_PACKED_RED_SHIFT);
	}

	public static final float convertRGBAToABGRPackedFloat(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return ColorUtils.convertPackedIntToPackedFloat(ColorUtils.convertRGBAToABGRPackedInt(pRed, pGreen, pBlue, pAlpha));
	}


	public static final float convertPackedIntToPackedFloat(final int pPackedInt) {
		return Float.intBitsToFloat(pPackedInt & ColorUtils.INT_BITS_TO_FLOAT_MASK);
	}


	public static float extractRedFromABGRPackedInt(final int pABGRPackedInt) {
		return ((pABGRPackedInt >> Color.ABGR_PACKED_RED_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractGreenFromABGRPackedInt(final int pABGRPackedInt) {
		return ((pABGRPackedInt >> Color.ABGR_PACKED_GREEN_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractBlueFromABGRPackedInt(final int pABGRPackedInt) {
		return ((pABGRPackedInt >> Color.ABGR_PACKED_BLUE_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractAlphaFromABGRPackedInt(final int pABGRPackedInt) {
		return ((pABGRPackedInt >> Color.ABGR_PACKED_ALPHA_SHIFT) & 0xFF) / 255.0f;
	}


	public static float extractBlueFromARGBPackedInt(final int pARGBPackedInt) {
		return ((pARGBPackedInt >> Color.ARGB_PACKED_BLUE_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractGreenFromARGBPackedInt(final int pARGBPackedInt) {
		return ((pARGBPackedInt >> Color.ARGB_PACKED_GREEN_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractRedFromARGBPackedInt(final int pARGBPackedInt) {
		return ((pARGBPackedInt >> Color.ARGB_PACKED_RED_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractAlphaFromARGBPackedInt(final int pARGBPackedInt) {
		return ((pARGBPackedInt >> Color.ARGB_PACKED_ALPHA_SHIFT) & 0xFF) / 255.0f;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
