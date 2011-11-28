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
	public void onCreateResources();
	public void onDestroyResources();
	public Scene onCreateScene();
	public void onGameCreated();

	public void onPauseGame();
	public void onResumeGame();
}
