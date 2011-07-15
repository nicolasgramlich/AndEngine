package org.anddev.andengine.entity.util;

import org.anddev.andengine.util.Debug;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:52:31 - 09.03.2010
 */
public class FPSLogger extends AverageFPSCounter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mShortestFrame = Float.MAX_VALUE;
	protected float mLongestFrame = Float.MIN_VALUE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FPSLogger() {
		super();
	}

	public FPSLogger(final float pAverageDuration) {
		super(pAverageDuration);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onHandleAverageDurationElapsed(final float pFPS) {
		this.onLogFPS();

		this.mLongestFrame = Float.MIN_VALUE;
		this.mShortestFrame = Float.MAX_VALUE;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);

		this.mShortestFrame = Math.min(this.mShortestFrame, pSecondsElapsed);
		this.mLongestFrame = Math.max(this.mLongestFrame, pSecondsElapsed);
	}

	@Override
	public void reset() {
		super.reset();

		this.mShortestFrame = Float.MAX_VALUE;
		this.mLongestFrame = Float.MIN_VALUE;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onLogFPS() {
		Debug.d(String.format("FPS: %.2f (MIN: %.0f ms | MAX: %.0f ms)",
				this.mFrames / this.mSecondsElapsed,
				this.mShortestFrame * MILLISECONDSPERSECOND,
				this.mLongestFrame * MILLISECONDSPERSECOND));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
