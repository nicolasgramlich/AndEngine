package org.andengine.opengl.texture;

import org.andengine.BuildConfig;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.util.debug.Debug;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:26:41 - 05.04.2012
 */
public class DebugTextureStateListener<T extends ITextureAtlasSource> implements ITextureStateListener {
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

	@Override
	public void onLoadedToHardware(final ITexture pTexture) {
		if (BuildConfig.DEBUG) {
			Debug.d("Texture loaded: " + pTexture.toString());
		}
	}

	@Override
	public void onUnloadedFromHardware(final ITexture pTexture) {
		if (BuildConfig.DEBUG) {
			Debug.d("Texture unloaded: " + pTexture.toString());
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}