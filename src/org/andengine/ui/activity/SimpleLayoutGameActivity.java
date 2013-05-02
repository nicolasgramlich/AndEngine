package org.andengine.ui.activity;

import java.io.IOException;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface;


/**
 * This class exists so that the callback parameters of the methods in {@link IGameInterface} get called automatically.
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 3:11:05 PM - Jan 12, 2012
 */
public abstract class SimpleLayoutGameActivity extends LayoutGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onCreateResources() throws IOException;
	protected abstract Scene onCreateScene() throws IOException;

	@Override
	public final void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
		this.onCreateResources();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public final void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
		final Scene scene = this.onCreateScene();

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public final void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
