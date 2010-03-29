package org.anddev.andengine.engine.options;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

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
	private final IResolutionPolicy mResolutionPolicy;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pCamera) {
		this(pFullscreen, pScreenOrientation, pResolutionPolicy, pCamera, null);
	}

	public EngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pCamera, final ITextureSource pLoadingScreenTextureSource) {
		this.mFullscreen = pFullscreen;
		this.mScreenOrientation = pScreenOrientation;
		this.mResolutionPolicy = pResolutionPolicy;
		this.mCamera = pCamera;
		this.mLoadingScreenTextureSource = pLoadingScreenTextureSource;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ScreenOrientation getScreenOrientation() {
		return this.mScreenOrientation;
	}
	
	public IResolutionPolicy getResolutionPolicy() {
		return this.mResolutionPolicy;
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