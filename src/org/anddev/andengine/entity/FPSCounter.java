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
	
	private long mTimestampLastLogged = System.currentTimeMillis();
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
		final long now = System.nanoTime();
		final long diff = now - this.mTimestampLastLogged;
		if(diff > TimeConstants.NANOSECONDSPERSECOND){
			Debug.d("FPS: " + (((float)this.mFramesInThisSecond * NANOSECONDSPERSECOND) / diff) + " ms");	
			this.mTimestampLastLogged = now;
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
