package org.anddev.andengine.engine;

import org.anddev.andengine.entity.Scene;

/**
 * @author Nicolas Gramlich
 * @since 18:04:48 - 09.03.2010
 */
public interface EngineHook {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
    public void updateScene(final Scene pScene);
    public void updateInput();
    public void updateAI();
    public void updatePhysics();
    public void updateSound();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public class EngineHookAdaper implements EngineHook {
		@Override
		public void updateAI() { }

		@Override
		public void updateInput() { }

		@Override
		public void updatePhysics() { }

		@Override
		public void updateScene(final Scene pScene) { }

		@Override
		public void updateSound() { }
	}
}
