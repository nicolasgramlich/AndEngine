package org.anddev.andengine.entity;

import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.TimeConstants;

/**
 * @author Nicolas Gramlich
 * @since 19:52:31 - 09.03.2010
 */
public class FPSCounter implements IUpdateHandler, TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mSecondsElapsed;
	private int mFramesInThisSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mFramesInThisSecond++;
		this.mSecondsElapsed += pSecondsElapsed;
		if(this.mSecondsElapsed > 1){
			Debug.d("FPS: " + (this.mFramesInThisSecond / this.mSecondsElapsed) + " ms");
			this.mSecondsElapsed = 0;
			this.mFramesInThisSecond = 0;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
