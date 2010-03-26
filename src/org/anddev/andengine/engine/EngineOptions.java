package org.anddev.andengine.engine;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.view.camera.Camera;

/**
 * @author  Nicolas Gramlich
 * @since  15:59:52 - 09.03.2010
 */
public class EngineOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ScreenOrientation mScreenOrientation;
	private final boolean mFullscreen;
	private final ITextureSource mLoadingScreenTextureSource;
	private final Camera mCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final Camera pCamera) {
		this(null, pFullscreen, pScreenOrientation, pCamera);
	}

	public EngineOptions(final ITextureSource pLoadingScreenTextureSource, final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final Camera pCamera) {
		this.mLoadingScreenTextureSource = pLoadingScreenTextureSource;
		this.mFullscreen = pFullscreen;
		this.mScreenOrientation = pScreenOrientation;
		this.mCamera = pCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ScreenOrientation getScreenOrientation() {
		return this.mScreenOrientation;
	}

	public boolean isFullscreen() {
		return this.mFullscreen;
	}

	public ITextureSource getLoadingScreenTextureSource() {
		return this.mLoadingScreenTextureSource;
	}

	public boolean hasLoadingScreen() {
		return this.mLoadingScreenTextureSource != null;
	}

	public Camera getCamera() {
		return this.mCamera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================



	public static enum ScreenOrientation {
		LANDSCAPE, PORTRAIT
	}
}