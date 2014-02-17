package org.andengine.engine;

import org.andengine.engine.options.EngineOptions;
import org.andengine.util.time.TimeConstants;

/**
 * A subclass of {@link Engine} that tries to achieve a specific amount of
 * updates per second. When the time since the last update is bigger long the
 * steplength, additional updates are executed.
 * 
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:17:47 - 02.08.2010
 */
public class LimitedFPSEngine extends Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final long mPreferredFrameLengthNanoseconds;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LimitedFPSEngine(final EngineOptions pEngineOptions, final int pFramesPerSecond) {
		super(pEngineOptions);
		this.mPreferredFrameLengthNanoseconds = TimeConstants.NANOSECONDS_PER_SECOND / pFramesPerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final long pNanosecondsElapsed) throws InterruptedException {
		final long preferredFrameLengthNanoseconds = this.mPreferredFrameLengthNanoseconds;
		final long deltaFrameLengthNanoseconds = preferredFrameLengthNanoseconds - pNanosecondsElapsed;

		if(deltaFrameLengthNanoseconds <= 0) {
			super.onUpdate(pNanosecondsElapsed);
		} else {
			final int sleepTimeMilliseconds = (int) (deltaFrameLengthNanoseconds / TimeConstants.NANOSECONDS_PER_MILLISECOND);

			Thread.sleep(sleepTimeMilliseconds);
			super.onUpdate(pNanosecondsElapsed + deltaFrameLengthNanoseconds);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
