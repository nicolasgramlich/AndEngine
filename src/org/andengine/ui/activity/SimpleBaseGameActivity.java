package org.andengine.ui.activity;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.IGameInterface;

/**
 * This class exists so that the callback parameters of the methods in
 * {@link IGameInterface} get called automatically. 
 * <br>
 * (c) Zynga 2011
 * @see BaseGameActivity
 * 
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:05:42 - 23.12.2011
 */
public abstract class SimpleBaseGameActivity extends BaseGameActivity {
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

	/**
	 * Load all the resources, including custom fonts and sprites. (Bitmaps are
	 * usually loaded with {@link BitmapTextureAtlas}.)
	 */
	protected abstract void onCreateResources();

	/**
	 * @return A {@link Scene} that contains all objects to be drawn on the screen.
	 */
	protected abstract Scene onCreateScene();

	@Override
	public final void onCreateResources(
			final OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		this.onCreateResources();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public final void onCreateScene(
			final OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		final Scene scene = this.onCreateScene();

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public final void onPopulateScene(final Scene pScene,
			final OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
