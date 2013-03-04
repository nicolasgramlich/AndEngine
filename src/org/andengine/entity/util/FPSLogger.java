package org.andengine.entity.util;

import org.andengine.BuildConfig;
import org.andengine.util.debug.Debug;
import org.andengine.util.debug.Debug.DebugLevel;
import org.andengine.util.time.TimeConstants;

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

	private final DebugLevel mDebugLevel;

	protected float mShortestFrame = Float.MAX_VALUE;
	protected float mLongestFrame = Float.MIN_VALUE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FPSLogger() {
		this(DebugLevel.DEBUG);
	}

	public FPSLogger(final DebugLevel pDebugLevel) {
		this.mDebugLevel = pDebugLevel;
	}

	public FPSLogger(final float pAverageDuration) {
		this(pAverageDuration, DebugLevel.DEBUG);
	}

	public FPSLogger(final float pAverageDuration, final DebugLevel pDebugLevel) {
		super(pAverageDuration);

		this.mDebugLevel = pDebugLevel;
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
		if (BuildConfig.DEBUG) {
			final float framesPerSecond = this.mFrames / this.mSecondsElapsed;
			final float shortestFrameInMilliseconds = this.mShortestFrame * TimeConstants.MILLISECONDS_PER_SECOND;
			final float longestFrameInMilliseconds = this.mLongestFrame * TimeConstants.MILLISECONDS_PER_SECOND;

			Debug.log(this.mDebugLevel, String.format("FPS: %.2f (MIN: %.0f ms | MAX: %.0f ms)", framesPerSecond, shortestFrameInMilliseconds, longestFrameInMilliseconds));
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
