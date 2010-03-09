package org.anddev.andengine.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.TimeConstants;

/**
 * @author Nicolas Gramlich
 * @since 19:52:31 - 09.03.2010
 */
public class FPSCounter implements IEntity, TimeConstants {
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
		final long now = System.currentTimeMillis();
		final long diff = now - this.mTimestampLastLogged;
		if(diff > TimeConstants.MILLISECONDSPERSECOND){
			Debug.d("FPS: " + ((float)this.mFramesInThisSecond * 1000 / diff) + " ms");	
			this.mTimestampLastLogged = now;
			this.mFramesInThisSecond = 0;
		}
	}

	@Override
	public void onDraw(final GL10 pGL) {
		
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
