package org.anddev.andengine.engine;

import org.anddev.andengine.engine.options.FixedStepEngineOptions;

/**
 * A subclass of {@link Engine} that tries to achieve a specific amount of updates per second. 
 * When the time since the last update is bigger long the steplength, additional updates are executed.
 *  
 * @author Nicolas Gramlich
 * @since 10:17:47 - 02.08.2010
 */
public class FixedStepEngine extends Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final float mStepLength;
	private float mSecondsElapsedAccumulator;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedStepEngine(final FixedStepEngineOptions pFixedStepEngineOptions) {
		super(pFixedStepEngineOptions);
		this.mStepLength = 1.0f / pFixedStepEngineOptions.getStepsPerSecond();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mSecondsElapsedAccumulator += pSecondsElapsed;

		final float stepLength = this.mStepLength;
		while(this.mSecondsElapsedAccumulator >= stepLength) {
			super.onUpdate(stepLength);
			this.mSecondsElapsedAccumulator -= stepLength;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
