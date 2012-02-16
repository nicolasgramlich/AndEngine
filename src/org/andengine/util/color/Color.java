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

	public static final int ABGR_PACKED_RED_CLEAR = 0X000000FF;
	public static final int ABGR_PACKED_GREEN_CLEAR = 0X0000FF00;
	public static final int ABGR_PACKED_BLUE_CLEAR = 0X00FF0000;
	public static final int ABGR_PACKED_ALPHA_CLEAR = 0XFF000000;

	public static final int ARGB_PACKED_BLUE_SHIFT = 0;
	public static final int ARGB_PACKED_GREEN_SHIFT = 8;
	public static final int ARGB_PACKED_RED_SHIFT = 16;
	public static final int ARGB_PACKED_ALPHA_SHIFT = 24;
	
	public static final int ARGB_PACKED_BLUE_CLEAR = 0X000000FF;
	public static final int ARGB_PACKED_GREEN_CLEAR = 0X0000FF00;
	public static final int ARGB_PACKED_RED_CLEAR = 0X00FF0000;
	public static final int ARGB_PACKED_ALPHA_CLEAR = 0XFF000000;

	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color GREEN = new Color(0, 1, 0, 1);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color PINK = new Color(1, 0, 1, 1);
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	public static final float WHITE_PACKED = Color.WHITE.getABGRPackedFloat();
	public static final float BLACK_PACKED = Color.BLACK.getABGRPackedFloat();
	public static final float RED_PACKED = Color.RED.getABGRPackedFloat();
	public static final float YELLOW_PACKED = Color.YELLOW.getABGRPackedFloat();
	public static final float GREEN_PACKED = Color.GREEN.getABGRPackedFloat();
	public static final float CYAN_PACKED = Color.CYAN.getABGRPackedFloat();
	public static final float BLUE_PACKED = Color.BLUE.getABGRPackedFloat();
	public static final float PINK_PACKED = Color.PINK.getABGRPackedFloat();
	public static final float TRANSPARENT_PACKED = Color.TRANSPARENT.getABGRPackedFloat();
	
	public static final int WHITE_PACKED_INT = Color.WHITE.getABGRPacked();
	public static final int BLACK_PACKED_INT = Color.BLACK.getABGRPacked();
	public static final int RED_PACKED_INT = Color.RED.getABGRPacked();
	public static final int YELLOW_PACKED_INT = Color.YELLOW.getABGRPacked();
	public static final int GREEN_PACKED_INT = Color.GREEN.getABGRPacked();
	public static final int CYAN_PACKED_INT = Color.CYAN.getABGRPacked();
	public static final int BLUE_PACKED_INT = Color.BLUE.getABGRPacked();
	public static final int PINK_PACKED_INT = Color.PINK.getABGRPacked();
	public static final int TRANSPARENT_PACKED_INT = Color.TRANSPARENT.getABGRPacked();

	// ===========================================================
	// Fields
	// ===========================================================

	private float mRed;
	private float mGreen;
	private float mBlue;
	private float mAlpha;

	private int mABGRPacked;
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

		this.mABGRPacked = pColor.mABGRPacked;
		this.mABGRPackedFloat = pColor.mABGRPackedFloat;
	}

	public final boolean setChecking(final Color pColor) {
		if(this.mABGRPackedFloat != pColor.mABGRPackedFloat) {
			this.mRed = pColor.mRed;
			this.mGreen = pColor.mGreen;
			this.mBlue = pColor.mBlue;
			this.mAlpha = pColor.mAlpha;

			this.mABGRPacked = pColor.mABGRPacked;
			this.mABGRPackedFloat = pColor.mABGRPackedFloat;
			return true;
		}
		return false;
	}

	public final int getABGRPacked() {
		return this.mABGRPacked;
	}

	public final float getABGRPackedFloat() {
		return this.mABGRPackedFloat;
	}

	/**
	 * @return the same format as {@link android.graphics.Color}.
	 */
	public final int getARGBPacked() {
		return ((int)(255 * this.mAlpha) << Color.ARGB_PACKED_ALPHA_SHIFT) | ((int)(255 * this.mRed) << Color.ARGB_PACKED_RED_SHIFT) | ((int)(255 * this.mGreen) << Color.ARGB_PACKED_GREEN_SHIFT) | ((int)(255 * this.mBlue) << Color.ARGB_PACKED_BLUE_SHIFT);
	}

	public final void reset() {
		this.set(Color.WHITE);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int hashCode() {
		return this.mABGRPacked;
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
		return this.mABGRPacked == pColor.mABGRPacked;
	}

	private final void packABGRRed() {
		this.mABGRPacked = (this.mABGRPacked & Color.ABGR_PACKED_RED_CLEAR) | ((int)(255 * this.mRed) << Color.ABGR_PACKED_RED_CLEAR);
		this.mABGRPackedFloat = Float.intBitsToFloat(this.mABGRPacked & 0XFEFFFFFF);
	}

	private final void packABGRGreen() {
		this.mABGRPacked = (this.mABGRPacked & Color.ABGR_PACKED_GREEN_CLEAR) | ((int)(255 * this.mGreen) << Color.ABGR_PACKED_GREEN_CLEAR);
		this.mABGRPackedFloat = Float.intBitsToFloat(this.mABGRPacked & 0XFEFFFFFF);
	}

	private final void packABGRBlue() {
		this.mABGRPacked = (this.mABGRPacked & Color.ABGR_PACKED_BLUE_CLEAR) | ((int)(255 * this.mBlue) << Color.ABGR_PACKED_BLUE_CLEAR);
		this.mABGRPackedFloat = Float.intBitsToFloat(this.mABGRPacked & 0XFEFFFFFF);
	}

	private final void packABGRAlpha() {
		this.mABGRPacked = (this.mABGRPacked & Color.ABGR_PACKED_ALPHA_CLEAR) | ((int)(255 * this.mAlpha) << Color.ABGR_PACKED_ALPHA_SHIFT);
		this.mABGRPackedFloat = Float.intBitsToFloat(this.mABGRPacked & 0XFEFFFFFF);
	}

	private final void packABGR() {
		this.mABGRPacked = ((int)(255 * this.mAlpha) << Color.ABGR_PACKED_ALPHA_SHIFT) | ((int)(255 * this.mBlue) << Color.ABGR_PACKED_BLUE_SHIFT) | ((int)(255 * this.mGreen) << Color.ABGR_PACKED_GREEN_SHIFT) | ((int)(255 * this.mRed) << Color.ABGR_PACKED_RED_SHIFT);
		this.mABGRPackedFloat = Float.intBitsToFloat(this.mABGRPacked & 0XFEFFFFFF);
	}

	public static final int packABGR(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return ((int)(255 * pAlpha) << Color.ABGR_PACKED_ALPHA_SHIFT) | ((int)(255 * pBlue) << Color.ABGR_PACKED_BLUE_SHIFT) | ((int)(255 * pGreen) << Color.ABGR_PACKED_GREEN_SHIFT) | ((int)(255 * pRed) << Color.ABGR_PACKED_RED_SHIFT);
	}

	public static final float packABGRFloat(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return Float.intBitsToFloat(Color.packABGR(pRed, pGreen, pBlue, pAlpha) & 0XFEFFFFFF);
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
