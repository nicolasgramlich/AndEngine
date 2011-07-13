package org.anddev.andengine.entity.scene.background;

import static org.anddev.andengine.util.constants.ColorConstants.COLOR_FACTOR_INT_TO_FLOAT;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:45:24 - 19.07.2010
 */
public class ColorBackground extends BaseBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mRed = 0.0f;
	private float mGreen = 0.0f;
	private float mBlue = 0.0f;
	private float mAlpha = 1.0f;

	private boolean mColorEnabled = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	protected ColorBackground() {

	}

	public ColorBackground(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	public ColorBackground(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Sets the color using the arithmetic scheme (0.0f - 1.0f RGB triple).
	 * @param pRed The red color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pGreen The green color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pBlue The blue color value. Should be between 0.0 and 1.0, inclusive.
	 */
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	/**
	 * Sets the color using the arithmetic scheme (0.0f - 1.0f RGB quadruple).
	 * @param pRed The red color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pGreen The green color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pBlue The blue color value. Should be between 0.0 and 1.0, inclusive.
	 * @param pAlpha The alpha color value. Should be between 0.0 and 1.0, inclusive.
	 */
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.setColor(pRed, pGreen, pBlue);
		this.mAlpha = pAlpha;
	}

	/**
	 * Sets the color using the digital 8-bit per channel scheme (0 - 255 RGB triple).
	 * @param pRed The red color value. Should be between 0 and 255, inclusive.
	 * @param pGreen The green color value. Should be between 0 and 255, inclusive.
	 * @param pBlue The blue color value. Should be between 0 and 255, inclusive.
	 */
	public void setColor(final int pRed, final int pGreen, final int pBlue) throws IllegalArgumentException {
		this.setColor(pRed / COLOR_FACTOR_INT_TO_FLOAT, pGreen / COLOR_FACTOR_INT_TO_FLOAT, pBlue / COLOR_FACTOR_INT_TO_FLOAT);
	}

	/**
	 * Sets the color using the digital 8-bit per channel scheme (0 - 255 RGB quadruple).
	 * @param pRed The red color value. Should be between 0 and 255, inclusive.
	 * @param pGreen The green color value. Should be between 0 and 255, inclusive.
	 * @param pBlue The blue color value. Should be between 0 and 255, inclusive.
	 */
	public void setColor(final int pRed, final int pGreen, final int pBlue, final int pAlpha) throws IllegalArgumentException {
		this.setColor(pRed / COLOR_FACTOR_INT_TO_FLOAT, pGreen / COLOR_FACTOR_INT_TO_FLOAT, pBlue / COLOR_FACTOR_INT_TO_FLOAT, pAlpha / COLOR_FACTOR_INT_TO_FLOAT);
	}

	public void setColorEnabled(final boolean pColorEnabled) {
		this.mColorEnabled = pColorEnabled;
	}

	public boolean isColorEnabled() {
		return this.mColorEnabled;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GL10 pGL, final Camera pCamera) {
		if(this.mColorEnabled) {
			pGL.glClearColor(this.mRed, this.mGreen, this.mBlue, this.mAlpha);
			pGL.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
