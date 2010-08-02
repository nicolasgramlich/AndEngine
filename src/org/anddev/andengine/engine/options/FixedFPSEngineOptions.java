package org.anddev.andengine.engine.options;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.resolutionpolicy.IResolutionPolicy;

/**
 * @author Nicolas Gramlich
 * @since 10:18:49 - 02.08.2010
 */
public class FixedFPSEngineOptions extends EngineOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int STEPSPERSECOND_DEFAULT = 60;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mFramesPerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedFPSEngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pCamera, final int pFramesPerSecond) {
		super(pFullscreen, pScreenOrientation, pResolutionPolicy, pCamera);
		this.mFramesPerSecond = pFramesPerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getFramesPerSecond() {
		return this.mFramesPerSecond;
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
