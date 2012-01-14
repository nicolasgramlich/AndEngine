package org.andengine.engine.options;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:59:52 - 09.03.2010
 */
public class EngineOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private EngineLock mEngineLock;

	private final boolean mFullscreen;
	private final ScreenOrientation mScreenOrientation;
	private final IResolutionPolicy mResolutionPolicy;
	private final Camera mCamera;

	private final TouchOptions mTouchOptions = new TouchOptions();
	private final AudioOptions mAudioOptions = new AudioOptions();
	private final RenderOptions mRenderOptions = new RenderOptions();

	private WakeLockOptions mWakeLockOptions = WakeLockOptions.SCREEN_ON;
	private int mUpdateThreadPriority = android.os.Process.THREAD_PRIORITY_DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pCamera) {
		this.mFullscreen = pFullscreen;
		this.mScreenOrientation = pScreenOrientation;
		this.mResolutionPolicy = pResolutionPolicy;
		this.mCamera = pCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean hasEngineLock() {
		return this.mEngineLock != null;
	}

	public EngineLock getEngineLock() {
		return this.mEngineLock;
	}

	public void setEngineLock(final EngineLock pEngineLock) {
		this.mEngineLock = pEngineLock;
	}

	public TouchOptions getTouchOptions() {
		return this.mTouchOptions;
	}

	public AudioOptions getAudioOptions() {
		return this.mAudioOptions;
	}

	public RenderOptions getRenderOptions() {
		return this.mRenderOptions;
	}

	public boolean isFullscreen() {
		return this.mFullscreen;
	}

	public ScreenOrientation getScreenOrientation() {
		return this.mScreenOrientation;
	}

	public IResolutionPolicy getResolutionPolicy() {
		return this.mResolutionPolicy;
	}

	public Camera getCamera() {
		return this.mCamera;
	}

	public int getUpdateThreadPriority() {
		return this.mUpdateThreadPriority;
	}

	/**
	 * @param pUpdateThreadPriority Use constants from: {@link android.os.Process}.
	 */
	public void setUpdateThreadPriority(final int pUpdateThreadPriority) {
		this.mUpdateThreadPriority = pUpdateThreadPriority;
	}

	public WakeLockOptions getWakeLockOptions() {
		return this.mWakeLockOptions;
	}

	public EngineOptions setWakeLockOptions(final WakeLockOptions pWakeLockOptions) {
		this.mWakeLockOptions = pWakeLockOptions;
		return this;
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
		// ===========================================================
		// Elements
		// ===========================================================

		/** The app will be fixed in its default Landscape mode. */
		LANDSCAPE_FIXED,
		/** The app will automatically rotate between the Landscape modes, depending on the orientation of the device. */
		LANDSCAPE_SENSOR,
		/** The app will be fixed in its default Portrait mode. */
		PORTRAIT_FIXED,
		/** The app will automatically rotate between the Portrait modes, depending on the orientation of the device. */
		PORTRAIT_SENSOR;
	}
}