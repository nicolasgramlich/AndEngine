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

	@Override
	public SplitScreenEngineOptions setLoadingScreenTextureSource(final ITextureSource pLoadingScreenTextureSource) {
		return (SplitScreenEngineOptions)super.setLoadingScreenTextureSource(pLoadingScreenTextureSource);
	}

	@Override
	public SplitScreenEngineOptions setNeedsMusic(final boolean pNeedsMusic) {
		return (SplitScreenEngineOptions)super.setNeedsMusic(pNeedsMusic);
	}

	@Override
	public SplitScreenEngineOptions setNeedsSound(final boolean pNeedsSound) {
		return (SplitScreenEngineOptions)super.setNeedsSound(pNeedsSound);
	}

	@Override
	public SplitScreenEngineOptions setWakeLockOptions(final WakeLockOptions pWakeLockOptions) {
		return (SplitScreenEngineOptions)super.setWakeLockOptions(pWakeLockOptions);
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
