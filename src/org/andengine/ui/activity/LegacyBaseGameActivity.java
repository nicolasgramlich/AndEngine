package org.andengine.ui.activity;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;


/**
 * This class exists to provide exact backward naming compatibility to older versions of {@link BaseGameActivity}.
 * Please consider actually switching to the new {@link BaseGameActivity}.
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:27:06 - 08.03.2010
 */
@Deprecated
public abstract class LegacyBaseGameActivity extends BaseGameActivity {
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

	protected abstract Engine onLoadEngine();
	protected abstract void onLoadResources();
	protected abstract void onUnloadResources();
	protected abstract Scene onLoadScene();
	protected abstract Scene onLoadComplete();

	@Override
	public final EngineOptions onCreateEngineOptions() {
		return null;
	}

	@Override
	public final Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return this.onLoadEngine();
	}

	@Override
	public final void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		this.onLoadResources();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public final void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		final Scene scene = this.onLoadScene();

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public final void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public final void onDestroyResources() throws Exception {
		super.onDestroyResources();

		this.onUnloadResources();
	}

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();

		this.onLoadComplete();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
