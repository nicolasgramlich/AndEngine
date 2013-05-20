package org.andengine.entity.scene;

import org.andengine.input.touch.TouchEvent;

/**
 * An interface for a callback to be invoked when a {@link TouchEvent} is
 * dispatched to a {@link Scene}. The callback will be invoked
 * after all {@link ITouchArea}s have been checked and none consumed the {@link TouchEvent}.
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:02:20 - 27.03.2012
 */
public interface IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Called when a {@link TouchEvent} is dispatched to a {@link Scene}.
	 *
	 * @param pScene The {@link Scene} that the {@link TouchEvent} has been dispatched to.
	 * @param pSceneTouchEvent The {@link TouchEvent} object containing full information about the event.
	 *
	 * @return <code>true</code> if this {@link IOnSceneTouchListener} has consumed the {@link TouchEvent}, <code>false</code> otherwise.
	 */
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent);
}