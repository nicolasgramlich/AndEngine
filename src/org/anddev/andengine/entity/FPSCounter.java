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

	private static final float AVERAGE_DURATION = 5;

	// ===========================================================
	// Fields
	// ===========================================================

	private float mSecondsElapsed;

	private int mFramesInThisSecond;

	private float mShortestFrame = Float.MAX_VALUE;
	private float mLongestFrame = Float.MIN_VALUE;

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

//		Debug.d("Elapsed: " + pSecondsElapsed);

		this.mShortestFrame = Math.min(this.mShortestFrame, pSecondsElapsed);

		this.mLongestFrame = Math.max(this.mLongestFrame, pSecondsElapsed);

		if(this.mSecondsElapsed > AVERAGE_DURATION){
			Debug.d(String.format("FPS: %.2f (MIN: %.0f ms | MAX: %.0f ms)", (this.mFramesInThisSecond / this.mSecondsElapsed), this.mShortestFrame * 1000, this.mLongestFrame * 1000));

			this.mSecondsElapsed -= AVERAGE_DURATION;
			this.mFramesInThisSecond = 0;

			this.mLongestFrame = Float.MIN_VALUE;
			this.mShortestFrame = Float.MAX_VALUE;
		}
	}

	@Override
	public void reset() {
		this.mSecondsElapsed = 0;

		this.mFramesInThisSecond = 0;

		this.mShortestFrame = Float.MAX_VALUE;
		this.mLongestFrame = Float.MIN_VALUE;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
