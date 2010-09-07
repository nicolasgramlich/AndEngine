package org.anddev.andengine.ui;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;

/**
 * @author Nicolas Gramlich
 * @since 12:03:08 - 14.03.2010
 */
public interface IGameInterface {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public Engine onLoadEngine();
	public void onLoadResources();
	public void onUnloadResources();
	public Scene onLoadScene();
	public void onLoadComplete();
	
	public void onGamePaused();
	public void onGameResumed(); 
}
