package org.anddev.andengine.engine.options;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 22:33:05 - 27.03.2010
 */
public class SplitScreenEngineOptions extends EngineOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Camera mSecondCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SplitScreenEngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pFirstCamera, final Camera pSecondCamera) {
		super(pFullscreen, pScreenOrientation, pResolutionPolicy, pFirstCamera);
		this.mSecondCamera = pSecondCamera;
	}

	public SplitScreenEngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pFirstCamera, final Camera pSecondCamera, final ITextureSource pLoadingScreenTextureSource) {
		super(pFullscreen, pScreenOrientation, pResolutionPolicy, pFirstCamera, pLoadingScreenTextureSource);
		this.mSecondCamera = pSecondCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Deprecated
	@Override
	public Camera getCamera() {
		return super.getCamera();
	}

	public Camera getFirstCamera() {
		return super.getCamera();
	}

	public Camera getSecondCamera() {
		return this.mSecondCamera;
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
}
