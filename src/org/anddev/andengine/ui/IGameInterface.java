package org.anddev.andengine.ui;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.Scene;

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

	public Scene onLoadScene();

	public void onLoadResources();

	public void onLoadComplete();

	public Engine onLoadEngine();
}
