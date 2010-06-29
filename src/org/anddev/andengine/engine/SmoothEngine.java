package org.anddev.andengine.engine;

import java.util.Arrays;

import org.anddev.andengine.engine.options.EngineOptions;

/**
 * @author Nicolas Gramlich
 * @since 11:51:36 - 29.06.2010
 */
public class SmoothEngine extends Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int RUNNING_AVERAGE_SIZE = 5;
	private static final int RUNNING_AVERAGE_CUTOFF = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final long[] mLastFrameLengths = new long[RUNNING_AVERAGE_SIZE];
	private final long[] mLastFrameLengthsSorted = new long[RUNNING_AVERAGE_SIZE];

	private int mLastFrameLengthsRunningIndex = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SmoothEngine(final EngineOptions pEngineOptions) {
		super(pEngineOptions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected long calculateNanoSecondsElapsed(final long pNow, final long pLastTick) {
		final long actualSecondsElapsed = super.calculateNanoSecondsElapsed(pNow, pLastTick);

		final long smoothedSecondsElapsed = this.calculateLastFramesAverage();

		this.mLastFrameLengths[this.mLastFrameLengthsRunningIndex] = actualSecondsElapsed;
		this.mLastFrameLengthsRunningIndex = (this.mLastFrameLengthsRunningIndex + 1) % RUNNING_AVERAGE_SIZE;

		return smoothedSecondsElapsed;
	}

	private long calculateLastFramesAverage() {
		final long[] lastFrameLengths = this.mLastFrameLengths;
		System.arraycopy(this.mLastFrameLengths, 0, this.mLastFrameLengthsSorted, 0, RUNNING_AVERAGE_SIZE);
		Arrays.sort(this.mLastFrameLengthsSorted);
		
//		/* Median */
//		return this.mLastFrameLengthsSorted[RUNNING_AVERAGE_SIZE / 2];

		/* Running average, excluding first x and last x frames. */
		long sum = 0;
		for(int i = RUNNING_AVERAGE_CUTOFF; i < RUNNING_AVERAGE_SIZE - RUNNING_AVERAGE_CUTOFF; i++) {
			sum += lastFrameLengths[i];
		}

		return sum / (RUNNING_AVERAGE_SIZE - 2 * RUNNING_AVERAGE_CUTOFF);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
