package org.andengine.ui;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:03:08 - 14.03.2010
 */
public interface IGameInterface {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public EngineOptions onCreateEngineOptions();
	public Engine onCreateEngine(final EngineOptions pEngineOptions);

	public void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception;
	public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception;
	public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception;

	public void onReloadResources() throws Exception;
	public void onDestroyResources() throws Exception;

	public void onGameCreated();
	public void onResumeGame();
	public void onPauseGame();
	public void onGameDestroyed();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface OnCreateResourcesCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onCreateResourcesFinished();
	}

	public static interface OnCreateSceneCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onCreateSceneFinished(final Scene pScene);
	}

	public static interface OnPopulateSceneCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onPopulateSceneFinished();
	}
}
