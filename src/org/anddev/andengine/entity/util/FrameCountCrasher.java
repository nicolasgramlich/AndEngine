package org.anddev.andengine.entity.util;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.TimeConstants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:52:31 - 09.03.2010
 */
public class FrameCountCrasher implements IUpdateHandler, TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mFramesLeft;

	private final float[] mFrameLengths;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FrameCountCrasher(final int pFrameCount) {
		this.mFramesLeft = pFrameCount;
		this.mFrameLengths = new float[pFrameCount];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mFramesLeft--;

		final float[] frameLengths = this.mFrameLengths;
		if(this.mFramesLeft >= 0) {
			frameLengths[this.mFramesLeft] = pSecondsElapsed;
		} else {
			for(int i = frameLengths.length - 1; i >= 0; i--) {
				Debug.d("Elapsed: " + frameLengths[i]);
			}

			throw new RuntimeException();
		}
	}

	@Override
	public void reset() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
