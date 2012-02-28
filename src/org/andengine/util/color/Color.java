package org.andengine.util.color;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:23:08 - 12.08.2011
 */
public class Color {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int ABGR_PACKED_RED_SHIFT = 0;
	public static final int ABGR_PACKED_GREEN_SHIFT = 8;
	public static final int ABGR_PACKED_BLUE_SHIFT = 16;
	public static final int ABGR_PACKED_ALPHA_SHIFT = 24;

	public static final int ABGR_PACKED_RED_CLEAR = 0XFFFFFF00;
	public static final int ABGR_PACKED_GREEN_CLEAR = 0XFFFF00FF;
	public static final int ABGR_PACKED_BLUE_CLEAR = 0XFF00FFFF;
	public static final int ABGR_PACKED_ALPHA_CLEAR = 0X00FFFFFF;

	public static final int ARGB_PACKED_BLUE_SHIFT = 0;
	public static final int ARGB_PACKED_GREEN_SHIFT = 8;
	public static final int ARGB_PACKED_RED_SHIFT = 16;
	public static final int ARGB_PACKED_ALPHA_SHIFT = 24;

	public static final int ARGB_PACKED_BLUE_CLEAR = 0XFFFFFF00;
	public static final int ARGB_PACKED_GREEN_CLEAR = 0XFFFF00FF;
	public static final int ARGB_PACKED_RED_CLEAR = 0XFF00FFFF;
	public static final int ARGB_PACKED_ALPHA_CLEAR = 0X00FFFFFF;

	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color GREEN = new Color(0, 1, 0, 1);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color PINK = new Color(1, 0, 1, 1);
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	public static final int WHITE_ABGR_PACKED_INT = Color.WHITE.getABGRPackedInt();
	public static final int BLACK_ABGR_PACKED_INT = Color.BLACK.getABGRPackedInt();
	public static final int RED_ABGR_PACKED_INT = Color.RED.getABGRPackedInt();
	public static final int YELLOW_ABGR_PACKED_INT = Color.YELLOW.getABGRPackedInt();
	public static final int GREEN_ABGR_PACKED_INT = Color.GREEN.getABGRPackedInt();
	public static final int CYAN_ABGR_PACKED_INT = Color.CYAN.getABGRPackedInt();
	public static final int BLUE_ABGR_PACKED_INT = Color.BLUE.getABGRPackedInt();
	public static final int PINK_ABGR_PACKED_INT = Color.PINK.getABGRPackedInt();
	public static final int TRANSPARENT_ABGR_PACKED_INT = Color.TRANSPARENT.getABGRPackedInt();

	public static final float WHITE_ABGR_PACKED_FLOAT = Color.WHITE.getABGRPackedFloat();
	public static final float BLACK_ABGR_PACKED_FLOAT = Color.BLACK.getABGRPackedFloat();
	public static final float RED_ABGR_PACKED_FLOAT = Color.RED.getABGRPackedFloat();
	public static final float YELLOW_ABGR_PACKED_FLOAT = Color.YELLOW.getABGRPackedFloat();
	public static final float GREEN_ABGR_PACKED_FLOAT = Color.GREEN.getABGRPackedFloat();
	public static final float CYAN_ABGR_PACKED_FLOAT = Color.CYAN.getABGRPackedFloat();
	public static final float BLUE_ABGR_PACKED_FLOAT = Color.BLUE.getABGRPackedFloat();
	public static final float PINK_ABGR_PACKED_FLOAT = Color.PINK.getABGRPackedFloat();
	public static final float TRANSPARENT_ABGR_PACKED_FLOAT = Color.TRANSPARENT.getABGRPackedFloat();
	
	public static final int WHITE_ARGB_PACKED_INT = Color.WHITE.getARGBPackedInt();
	public static final int BLACK_ARGB_PACKED_INT = Color.BLACK.getARGBPackedInt();
	public static final int RED_ARGB_PACKED_INT = Color.RED.getARGBPackedInt();
	public static final int YELLOW_ARGB_PACKED_INT = Color.YELLOW.getARGBPackedInt();
	public static final int GREEN_ARGB_PACKED_INT = Color.GREEN.getARGBPackedInt();
	public static final int CYAN_ARGB_PACKED_INT = Color.CYAN.getARGBPackedInt();
	public static final int BLUE_ARGB_PACKED_INT = Color.BLUE.getARGBPackedInt();
	public static final int PINK_ARGB_PACKED_INT = Color.PINK.getARGBPackedInt();
	public static final int TRANSPARENT_ARGB_PACKED_INT = Color.TRANSPARENT.getARGBPackedInt();

	// ===========================================================
	// Fields
	// ===========================================================

	private float mRed;
	private float mGreen;
	private float mBlue;
	private float mAlpha;

	private int mABGRPackedInt;
	private float mABGRPackedFloat;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Color(final Color pColor) {
		this.set(pColor);
	}

	public Color(final float pRed, final float pGreen, final float pBlue) {
		this(pRed, pGreen, pBlue, 1);
	}

	public Color(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.set(pRed, pGreen, pBlue, pAlpha);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final float getRed() {
		return this.mRed;
	}

	public final float getGreen() {
		return this.mGreen;
	}

	public final float getBlue() {
		return this.mBlue;
	}

	public final float getAlpha() {
		return this.mAlpha;
	}

	public final void setRed(final float pRed) {
		this.mRed = pRed;

		this.packABGRRed();
	}

	public final boolean setRedChecking(final float pRed) {
		if(this.mRed != pRed) {
			this.mRed = pRed;

			this.packABGRRed();
			return true;
		}
		return false;
	}

	public final void setGreen(final float pGreen) {
		this.mGreen = pGreen;

		this.packABGRGreen();
	}

	public final boolean setGreenChecking(final float pGreen) {
		if(this.mGreen != pGreen) {
			this.mGreen = pGreen;

			this.packABGRGreen();
			return true;
		}
		return false;
	}

	public final void setBlue(final float pBlue) {
		this.mBlue = pBlue;

		this.packABGRBlue();
	}

	public final boolean setBlueChecking(final float pBlue) {
		if(this.mBlue != pBlue) {
			this.mBlue = pBlue;

			this.packABGRBlue();
			return true;
		}
		return false;
	}

	public final void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;

		this.packABGRAlpha();
	}

	public final boolean setAlphaChecking(final float pAlpha) {
		if(this.mAlpha != pAlpha) {
			this.mAlpha = pAlpha;

			this.packABGRAlpha();
			return true;
		}
		return false;
	}

	public final void set(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;

		this.packABGR();
	}

	public final boolean setChecking(final float pRed, final float pGreen, final float pBlue) {
		if((this.mRed != pRed) || (this.mGreen != pGreen) || (this.mBlue != pBlue)) {
			this.mRed = pRed;
			this.mGreen = pGreen;
			this.mBlue = pBlue;

			this.packABGR();
			return true;
		}
		return false;
	}

	public final void set(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;

		this.packABGR();
	}

	public final boolean setChecking(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if((this.mAlpha != pAlpha) || (this.mRed != pRed) || (this.mGreen != pGreen) || (this.mBlue != pBlue)) {
			this.mRed = pRed;
			this.mGreen = pGreen;
			this.mBlue = pBlue;
			this.mAlpha = pAlpha;

			this.packABGR();
			return true;
		}
		return false;
	}

	public final void set(final Color pColor) {
		this.mRed = pColor.mRed;
		this.mGreen = pColor.mGreen;
		this.mBlue = pColor.mBlue;
		this.mAlpha = pColor.mAlpha;

		this.mABGRPackedInt = pColor.mABGRPackedInt;
		this.mABGRPackedFloat = pColor.mABGRPackedFloat;
	}

	public final boolean setChecking(final Color pColor) {
		if(this.mABGRPackedInt != pColor.mABGRPackedInt) {
			this.mRed = pColor.mRed;
			this.mGreen = pColor.mGreen;
			this.mBlue = pColor.mBlue;
			this.mAlpha = pColor.mAlpha;

			this.mABGRPackedInt = pColor.mABGRPackedInt;
			this.mABGRPackedFloat = pColor.mABGRPackedFloat;
			return true;
		}
		return false;
	}

	public final int getABGRPackedInt() {
		return this.mABGRPackedInt;
	}

	public final float getABGRPackedFloat() {
		return this.mABGRPackedFloat;
	}

	/**
	 * @return the same format as {@link android.graphics.Color}.
	 */
	public final int getARGBPackedInt() {
		return ColorUtils.convertRGBAToARGBPackedInt(this.mRed, this.mGreen, this.mBlue, this.mAlpha);
	}

	public final void reset() {
		this.set(Color.WHITE);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int hashCode() {
		return this.mABGRPackedInt;
	}

	@Override
	public boolean equals(final Object pObject) {
		if(this == pObject) {
			return true;
		} else if(pObject == null) {
			return false;
		} else if(this.getClass() != pObject.getClass()) {
			return false;
		}

		return this.equals((Color) pObject);
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("[Red: ")
			.append(this.mRed)
			.append(", Green: ")
			.append(this.mGreen)
			.append(", Blue: ")
			.append(this.mBlue)
			.append(", Alpha: ")
			.append(this.mAlpha)
			.append("]")
			.toString();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean equals(final Color pColor) {
		return this.mABGRPackedInt == pColor.mABGRPackedInt;
	}

	private final void packABGRRed() {
		this.mABGRPackedInt = (this.mABGRPackedInt & Color.ABGR_PACKED_RED_CLEAR) | ((int)(255 * this.mRed) << Color.ABGR_PACKED_RED_CLEAR);
		this.mABGRPackedFloat = ColorUtils.convertPackedIntToPackedFloat(this.mABGRPackedInt);
	}

	private final void packABGRGreen() {
		this.mABGRPackedInt = (this.mABGRPackedInt & Color.ABGR_PACKED_GREEN_CLEAR) | ((int)(255 * this.mGreen) << Color.ABGR_PACKED_GREEN_CLEAR);
		this.mABGRPackedFloat = ColorUtils.convertPackedIntToPackedFloat(this.mABGRPackedInt);
	}

	private final void packABGRBlue() {
		this.mABGRPackedInt = (this.mABGRPackedInt & Color.ABGR_PACKED_BLUE_CLEAR) | ((int)(255 * this.mBlue) << Color.ABGR_PACKED_BLUE_CLEAR);
		this.mABGRPackedFloat = ColorUtils.convertPackedIntToPackedFloat(this.mABGRPackedInt);
	}

	private final void packABGRAlpha() {
		this.mABGRPackedInt = (this.mABGRPackedInt & Color.ABGR_PACKED_ALPHA_CLEAR) | ((int)(255 * this.mAlpha) << Color.ABGR_PACKED_ALPHA_SHIFT);
		this.mABGRPackedFloat = ColorUtils.convertPackedIntToPackedFloat(this.mABGRPackedInt);
	}

	private final void packABGR() {
		this.mABGRPackedInt = ColorUtils.convertRGBAToABGRPackedInt(this.mRed, this.mGreen, this.mBlue, this.mAlpha);
		this.mABGRPackedFloat = ColorUtils.convertPackedIntToPackedFloat(this.mABGRPackedInt);
	}

	public final void mix(final Color pColorA, final float pPercentageA, final Color pColorB, final float pPercentageB) {
		final float red = (pColorA.mRed * pPercentageA) + (pColorB.mRed * pPercentageB);
		final float green = (pColorA.mGreen * pPercentageA) + (pColorB.mGreen * pPercentageB);
		final float blue = (pColorA.mBlue * pPercentageA) + (pColorB.mBlue * pPercentageB);
		final float alpha = (pColorA.mAlpha * pPercentageA) + (pColorB.mAlpha * pPercentageB);

		this.set(red, green, blue, alpha);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
