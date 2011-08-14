package org.anddev.andengine.util.color;

/**
 * TODO Potentially too much work is being done when packing the whole Color, when i.e. only Alpha had been changed. Solution keep the int packed color and update only relevant parts before converting it to float. This saves a couple of bit operations.
 * 
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:23:08 - 12.08.2011
 */
public class Color {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mRed;
	private float mGreen;
	private float mBlue;
	private float mAlpha;

	private float mPacked;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Color(final float pRed, final float pGreen, final float pBlue) {
		this(pRed, pGreen, pBlue, 1);
	}

	public Color(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.set(pRed, pGreen, pBlue, pAlpha);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getRed() {
		return this.mRed;
	}

	public float getGreen() {
		return this.mGreen;
	}

	public float getBlue() {
		return this.mBlue;
	}

	public float getAlpha() {
		return this.mAlpha;
	}

	public void setRed(final float pRed) {
		this.mRed = pRed;

		this.pack();
	}

	public boolean setRedChecking(final float pRed) {
		if(this.mRed != pRed) {
			this.mRed = pRed;

			this.pack();
			return true;
		}
		return false;
	}

	public void setGreen(final float pGreen) {
		this.mGreen = pGreen;

		this.pack();
	}

	public boolean setGreenChecking(final float pGreen) {
		if(this.mGreen != pGreen) {
			this.mGreen = pGreen;

			this.pack();
			return true;
		}
		return false;
	}

	public void setBlue(final float pBlue) {
		this.mBlue = pBlue;

		this.pack();
	}

	public boolean setBlueChecking(final float pBlue) {
		if(this.mBlue != pBlue) {
			this.mBlue = pBlue;

			this.pack();
			return true;
		}
		return false;
	}

	public void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;

		this.pack();
	}

	public boolean setAlphaChecking(final float pAlpha) {
		if(this.mAlpha != pAlpha) {
			this.mAlpha = pAlpha;

			this.pack();
			return true;
		}
		return false;
	}

	public void set(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;

		this.pack();
	}

	public boolean setChanging(final float pRed, final float pGreen, final float pBlue) {
		if(this.mRed != pRed || this.mGreen != pGreen || this.mBlue != pBlue) {
			this.mRed = pRed;
			this.mGreen = pGreen;
			this.mBlue = pBlue;
			return true;
		}
		return false;
	}

	public void set(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;

		this.pack();
	}

	public boolean setChanging(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(this.mAlpha != pAlpha || this.mRed != pRed || this.mGreen != pGreen || this.mBlue != pBlue) {
			this.mRed = pRed;
			this.mGreen = pGreen;
			this.mBlue = pBlue;

			this.pack();
			return true;
		}
		return false;
	}

	public void set(final Color pColor) {
		this.mRed = pColor.mRed;
		this.mGreen = pColor.mGreen;
		this.mBlue = pColor.mBlue;
		this.mAlpha = pColor.mAlpha;

		this.mPacked = pColor.mPacked;
	}

	public boolean setChecking(final Color pColor) {
		if(this.mAlpha != pColor.mAlpha || this.mRed != pColor.mRed || this.mGreen != pColor.mGreen || this.mBlue != pColor.mBlue) {
			this.mRed = pColor.mRed;
			this.mGreen = pColor.mGreen;
			this.mBlue = pColor.mBlue;
			this.mAlpha = pColor.mAlpha;

			this.mPacked = pColor.mPacked;
			return true;
		}
		return false;
	}

	public float getPacked() {
		return this.mPacked;
	}

	public void reset() {
		this.set(1, 1, 1, 1);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void pack() {
		final int packed = ((int)(255 * this.mAlpha) << 24) | ((int)(255 * this.mBlue) << 16) | ((int)(255 * this.mGreen) << 8) | ((int)(255 * this.mRed));
		this.mPacked = Float.intBitsToFloat(packed & 0XFEFFFFFF);
	}

	public static float pack(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		int o = android.graphics.Color.argb(255, 128, 64, 32);
		final int packed = ((int)(255 * pAlpha) << 24) | ((int)(255 * pBlue) << 16) | ((int)(255 * pGreen) << 8) | ((int)(255 * pRed));
		return Float.intBitsToFloat(packed & 0XFEFFFFFF);
	}

	public void mix(final Color pColorA, final float pPercentageA, final Color pColorB, final float pPercentageB) {
		final float red = pColorA.mRed * pPercentageA + pColorB.mRed * pPercentageB;
		final float green = pColorA.mGreen * pPercentageA + pColorB.mGreen * pPercentageB;
		final float blue = pColorA.mBlue * pPercentageA + pColorB.mBlue * pPercentageB;
		final float alpha = pColorA.mAlpha * pPercentageA + pColorB.mAlpha * pPercentageB;
		
		this.set(red, green, blue, alpha);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
