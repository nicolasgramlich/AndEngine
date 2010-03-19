package org.anddev.andengine.entity.background;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Nicolas Gramlich
 * @since 13:29:52 - 08.03.2010
 */
public class FixedColorBackground extends BaseBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mRed;
	private final float mGreen;
	private final float mBlue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedColorBackground(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		pGL.glClearColor(this.mRed, this.mGreen, this.mBlue, 1.0f);
		pGL.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) { }

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
