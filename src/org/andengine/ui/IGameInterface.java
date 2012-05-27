package org.andengine.ui;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/**
 * The lifecycle
 * <br>
 * (c) 2010 Nicolas Gramlich <br>
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

	/**
	 * Create an instance of {@link EngineOptions} to suit your needs
	 * @return The {@link EngineOptions} that the {@link Engine} needs to be created
	 */
	public EngineOptions onCreateEngineOptions();
	
	/**
	 * @param pEngineOptions The {@link EngineOptions} that the {@link Engine} needs to be created
	 * @return An instance of {@link Engine} that the application runs on
	 */
	public Engine onCreateEngine(final EngineOptions pEngineOptions);

	/**
	 * Load all the resources, including custom fonts and sprites. (Bitmaps are usually loaded with {@link BitmapTextureAtlas}.)
	 * @param pOnCreateResourcesCallback When done loading the textures, use {@link OnCreateResourcesCallback#onCreateResourcesFinished()} to finish
	 * @throws Exception
	 */
	public void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception;
	/**
	 * Create a {@link Scene} that contains all objects to be drawn on the screen.
	 * @param pOnCreateSceneCallback When done loading the textures, use {@link OnCreateSceneCallback#onCreateSceneFinished(Scene)} to finish
	 * @throws Exception
	 */
	public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception;
	/**
	 * 
	 * @param pScene The {@link Scene} that should be populated
	 * @param pOnPopulateSceneCallback When done loading the textures, use {@link OnPopulateSceneCallback#onPopulateSceneFinished()} to finish
	 * @throws Exception
	 */
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
