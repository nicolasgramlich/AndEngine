package org.anddev.andengine.util;

import android.graphics.Color;

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

	private static final int COLOR_FLOAT_TO_INT_FACTOR = 255;

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

	/**
	 * @param pHue [0 .. 360)
	 * @param pSaturation [0...1]
	 * @param pValue [0...1]
	 */
	public static int HSVToColor(final float pHue, final float pSaturation, final float pValue) {
		HSV_TO_COLOR[HSV_TO_COLOR_HUE_INDEX] = pHue;
		HSV_TO_COLOR[HSV_TO_COLOR_SATURATION_INDEX] = pSaturation;
		HSV_TO_COLOR[HSV_TO_COLOR_VALUE_INDEX] = pValue;
		return Color.HSVToColor(HSV_TO_COLOR);
	}

	public static int RGBToColor(final float pRed, final float pGreen, final float pBlue) {
		return Color.rgb((int)(pRed * COLOR_FLOAT_TO_INT_FACTOR), (int)(pGreen * COLOR_FLOAT_TO_INT_FACTOR), (int)(pBlue * COLOR_FLOAT_TO_INT_FACTOR));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
