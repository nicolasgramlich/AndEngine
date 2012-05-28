package org.andengine.engine.handler;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.util.IMatcher;

/**
 * An update handler is something which you can create, and register within your
 * {@link Engine} or {@link Scene} using
 * {@link Engine#registerUpdateHandler(IUpdateHandler)} or
 * {@link Scene#registerUpdateHandler(IUpdateHandler)} so that the code in
 * {@link #onUpdate(float)} is run on every update of the engine. If your
 * program does not require updating on every tick, consider using a listener,
 * like {@link IOnAreaTouchListener} for listening to when an area is touched.
 * Also don't forget to unregister when you don't need the update handler
 * anymore.<br>
 * <br>
 * (c) 2010 Nicolas Gramlich <br>
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:24:09 - 11.03.2010
 */
public interface IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * The code that should happen with each tick of the engine.
	 * 
	 * @param pSecondsElapsed
	 *            How many seconds have elapsed since the last update
	 */
	public void onUpdate(final float pSecondsElapsed);

	/**
	 * Code to perform when the handler is reset. (Gets called, for example, when the scene it's registered to is reset.)
	 */
	public void reset();

	// TODO Maybe add onRegister and onUnregister. (Maybe add
	// SimpleUpdateHandler that implements all methods, but onUpdate)

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface IUpdateHandlerMatcher extends IMatcher<IUpdateHandler> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
