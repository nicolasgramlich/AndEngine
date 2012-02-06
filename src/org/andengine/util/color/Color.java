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

	private static final int PACKED_INT_ALPHA_RED = 0;
	private static final int PACKED_INT_ALPHA_GREEN = 8;
	private static final int PACKED_INT_ALPHA_BLUE = 16;
	private static final int PACKED_INT_ALPHA_SHIFT = 24;

	private static final int PACKED_INT_RED_CLEAR = 0X00FFFFFF;
	private static final int PACKED_INT_GREEN_CLEAR = 0X00FFFFFF;
	private static final int PACKED_INT_BLUE_CLEAR = 0X00FFFFFF;
	private static final int PACKED_INT_ALPHA_CLEAR = 0X00FFFFFF;

	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color GREEN = new Color(0, 1, 0, 1);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color PINK = new Color(1, 0, 1, 1);
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);

	public static final float WHITE_PACKED = Color.WHITE.getFloatPacked();
	public static final float BLACK_PACKED = Color.BLACK.getFloatPacked();
	public static final float RED_PACKED = Color.RED.getFloatPacked();
	public static final float YELLOW_PACKED = Color.YELLOW.getFloatPacked();
	public static final float GREEN_PACKED = Color.GREEN.getFloatPacked();
	public static final float CYAN_PACKED = Color.CYAN.getFloatPacked();
	public static final float BLUE_PACKED = Color.BLUE.getFloatPacked();
	public static final float PINK_PACKED = Color.PINK.getFloatPacked();
	public static final float TRANSPARENT_PACKED = Color.TRANSPARENT.getFloatPacked();

	// ===========================================================
	// Fields
	// ===========================================================

	private float mRed;
	private float mGreen;
	private float mBlue;
	private float mAlpha;

	private int mIntPacked;
	private float mFloatPacked;

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

		this.packRed();
	}

	public final boolean setRedChecking(final float pRed) {
		if(this.mRed != pRed) {
			this.mRed = pRed;

			this.packRed();
			return true;
		}
		return false;
	}

	public final void setGreen(final float pGreen) {
		this.mGreen = pGreen;

		this.packGreen();
	}

	public final boolean setGreenChecking(final float pGreen) {
		if(this.mGreen != pGreen) {
			this.mGreen = pGreen;

			this.packGreen();
			return true;
		}
		return false;
	}

	public final void setBlue(final float pBlue) {
		this.mBlue = pBlue;

		this.packBlue();
	}

	public final boolean setBlueChecking(final float pBlue) {
		if(this.mBlue != pBlue) {
			this.mBlue = pBlue;

			this.packBlue();
			return true;
		}
		return false;
	}

	public final void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;

		this.packAlpha();
	}

	public final boolean setAlphaChecking(final float pAlpha) {
		if(this.mAlpha != pAlpha) {
			this.mAlpha = pAlpha;

			this.packAlpha();
			return true;
		}
		return false;
	}

	public final void set(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;

		this.pack();
	}

	public final boolean setChecking(final float pRed, final float pGreen, final float pBlue) {
		if((this.mRed != pRed) || (this.mGreen != pGreen) || (this.mBlue != pBlue)) {
			this.mRed = pRed;
			this.mGreen = pGreen;
			this.mBlue = pBlue;

			this.pack();
			return true;
		}
		return false;
	}

	public final void set(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;

		this.pack();
	}

	public final boolean setChecking(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if((this.mAlpha != pAlpha) || (this.mRed != pRed) || (this.mGreen != pGreen) || (this.mBlue != pBlue)) {
			this.mRed = pRed;
			this.mGreen = pGreen;
			this.mBlue = pBlue;
			this.mAlpha = pAlpha;

			this.pack();
			return true;
		}
		return false;
	}

	public final void set(final Color pColor) {
		this.mRed = pColor.mRed;
		this.mGreen = pColor.mGreen;
		this.mBlue = pColor.mBlue;
		this.mAlpha = pColor.mAlpha;

		this.mIntPacked = pColor.mIntPacked;
		this.mFloatPacked = pColor.mFloatPacked;
	}

	public final boolean setChecking(final Color pColor) {
		if(this.mFloatPacked != pColor.mFloatPacked) {
			this.mRed = pColor.mRed;
			this.mGreen = pColor.mGreen;
			this.mBlue = pColor.mBlue;
			this.mAlpha = pColor.mAlpha;

			this.mIntPacked = pColor.mIntPacked;
			this.mFloatPacked = pColor.mFloatPacked;
			return true;
		}
		return false;
	}

	public final float getIntPacked() {
		return this.mIntPacked;
	}

	public final float getFloatPacked() {
		return this.mFloatPacked;
	}

	public final void reset() {
		this.set(Color.WHITE);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

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

	private final void packRed() {
		this.mIntPacked = (this.mIntPacked & Color.PACKED_INT_RED_CLEAR) | ((int)(255 * this.mRed) << Color.PACKED_INT_RED_CLEAR);
		this.mFloatPacked = Float.intBitsToFloat(this.mIntPacked & 0XFEFFFFFF);
	}

	private final void packGreen() {
		this.mIntPacked = (this.mIntPacked & Color.PACKED_INT_GREEN_CLEAR) | ((int)(255 * this.mGreen) << Color.PACKED_INT_GREEN_CLEAR);
		this.mFloatPacked = Float.intBitsToFloat(this.mIntPacked & 0XFEFFFFFF);
	}

	private final void packBlue() {
		this.mIntPacked = (this.mIntPacked & Color.PACKED_INT_BLUE_CLEAR) | ((int)(255 * this.mBlue) << Color.PACKED_INT_BLUE_CLEAR);
		this.mFloatPacked = Float.intBitsToFloat(this.mIntPacked & 0XFEFFFFFF);
	}

	private final void packAlpha() {
		this.mIntPacked = (this.mIntPacked & Color.PACKED_INT_ALPHA_CLEAR) | ((int)(255 * this.mAlpha) << Color.PACKED_INT_ALPHA_SHIFT);
		this.mFloatPacked = Float.intBitsToFloat(this.mIntPacked & 0XFEFFFFFF);
	}

	private final void pack() {
		this.mIntPacked = ((int)(255 * this.mAlpha) << Color.PACKED_INT_ALPHA_SHIFT) | ((int)(255 * this.mBlue) << Color.PACKED_INT_ALPHA_BLUE) | ((int)(255 * this.mGreen) << Color.PACKED_INT_ALPHA_GREEN) | ((int)(255 * this.mRed) << Color.PACKED_INT_ALPHA_RED);
		this.mFloatPacked = Float.intBitsToFloat(this.mIntPacked & 0XFEFFFFFF);
	}

	public static final int packInt(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return ((int)(255 * pAlpha) << Color.PACKED_INT_ALPHA_SHIFT) | ((int)(255 * pBlue) << Color.PACKED_INT_ALPHA_BLUE) | ((int)(255 * pGreen) << Color.PACKED_INT_ALPHA_GREEN) | ((int)(255 * pRed) << Color.PACKED_INT_ALPHA_RED);
	}

	public static final float pack(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		return Float.intBitsToFloat(Color.packInt(pRed, pGreen, pBlue, pAlpha) & 0XFEFFFFFF);
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
