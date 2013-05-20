package org.andengine.entity.scene;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.IMatcher;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:01:18 - 27.03.2012
 */
public interface ITouchArea {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean contains(final float pX, final float pY);

	public float[] convertSceneCoordinatesToLocalCoordinates(final float pX, final float pY);
	public float[] convertLocalCoordinatesToSceneCoordinates(final float pX, final float pY);

	/**
	 * This method only fires if this {@link ITouchArea} is registered to the {@link Scene} via {@link Scene#registerTouchArea(ITouchArea)}.
	 * @param pSceneTouchEvent
	 * @return <code>true</code> if the event was handled (that means {@link IOnAreaTouchListener} of the {@link Scene} will not be fired!), otherwise <code>false</code>.
	 */
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITouchAreaMatcher extends IMatcher<ITouchArea> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}