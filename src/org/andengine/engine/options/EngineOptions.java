package org.andengine.engine.options;

import org.andengine.engine.Engine;
import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.Engine.UpdateThread;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;

/**
 * Specifies the options with which to create the {@link org.andengine.engine.Engine Engine}, like if the application runs full screen, what the aspect ratio is, etc.
 * 
 * <br>
 * (c) 2010 Nicolas Gramlich <br>
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

	private UpdateThread mUpdateThread;
	private int mUpdateThreadPriority = android.os.Process.THREAD_PRIORITY_DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Creates the options that are needed to create an {@link org.andengine.engine.Engine Engine}. 
	 * @param pFullscreen Whether the app should run full screen
	 * @param pScreenOrientation Whether the app should be in portrait mode or landscape mode, and whether it should react to tilting the device. Takes a value of {@link org.andengine.engine.options.ScreenOrientation ScreenOrientation}.
	 * @param pResolutionPolicy The {@link org.andengine.engine.options.resolutionpolicy.IResolutionPolicy IResolutionPolicy} that determines how the pixels should be proportioned on the screen. Usually a {@link org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy RatioResolutionPolicy} that stretches a given aspect ratio.
	 * @param pCamera The {@link org.andengine.engine.camera.Camera Camera} that follow that portion of the {@link org.andengine.entity.scene.Scene Scene} that should be drawed to the screen.
	 */
	public EngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final IResolutionPolicy pResolutionPolicy, final Camera pCamera) {
		this.mFullscreen = pFullscreen;
		this.mScreenOrientation = pScreenOrientation;
		this.mResolutionPolicy = pResolutionPolicy;
		this.mCamera = pCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	/**
	 * @return Whether the engine has an {@link org.andengine.engine.Engine.EngineLock EngineLock}
	*/
	public boolean hasEngineLock() {
		return this.mEngineLock != null;
	}

	/**
	 * @return The {@link org.andengine.engine.Engine.EngineLock EngineLock} of the {@link org.andengine.engine.Engine Engine}, null if there's none
	 */
	public EngineLock getEngineLock() {
		return this.mEngineLock;
	}

	/**
	 * @param pEngineLock The {@link org.andengine.engine.Engine.EngineLock EngineLock} that the {@link org.andengine.engine.Engine Engine} should have. Only needed if you need to run mutually exclusive to the UpdateThread and the open GL Thread.
	 */
	public void setEngineLock(final EngineLock pEngineLock) {
		this.mEngineLock = pEngineLock;
	}

	/**
	 * @return The {@link TouchOptions} that specify whether the {@link Engine} needs multitouch and the minimum amount of time between two touch events 
	 */
	public TouchOptions getTouchOptions() {
		return this.mTouchOptions;
	}

	/**
	 * @return The {@link AudioOptions} that specify the {@link SoundOptions} and the {@link MusicOptions}
	 */
	public AudioOptions getAudioOptions() {
		return this.mAudioOptions;
	}

	/**
	 * @return The {@link RenderOptions} that specify how the graphics should be rendered (dithering, multisample, etc.)
	 */
	public RenderOptions getRenderOptions() {
		return this.mRenderOptions;
	}

	/**
	 * @return Whether the application uses the entire screen of the device
	 */
	public boolean isFullscreen() {
		return this.mFullscreen;
	}

	/**
	 * @return The {@link ScreenOrientation} of the {@link Engine}: whether it's in portrait mode or landscape mode, and how it reacts the tilting of the device.
	 */
	public ScreenOrientation getScreenOrientation() {
		return this.mScreenOrientation;
	}

	/**
	 * @return The {@link org.andengine.engine.options.resolutionpolicy.IResolutionPolicy IResolutionPolicy} that determines how the pixels should be proportioned on the screen. Usually a {@link org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy RatioResolutionPolicy} that stretches a given aspect ratio. 
	 */
	public IResolutionPolicy getResolutionPolicy() {
		return this.mResolutionPolicy;
	}

	/**
	 * @return The {@link org.andengine.engine.camera.Camera Camera} that follow that portion of the {@link org.andengine.entity.scene.Scene Scene} that should be drawed to the screen.
	 *
	 */
	public Camera getCamera() {
		return this.mCamera;
	}

	/**
	 * @return Whether there is an {@link UpdateThread}
	 */
	public boolean hasUpdateThread() {
		return this.mUpdateThread != null;
	}

	/**
	 * @return The {@link UpdateThread}, null if there's none
	 */
	public UpdateThread getUpdateThread() {
		return this.mUpdateThread;
	}

	/**
	 * @param pUpdateThread The {@link UpdateThread} that should run 
	 */
	public void setUpdateThread(final UpdateThread pUpdateThread) {
		this.mUpdateThread = pUpdateThread;
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

	/**
	 * @return The {@link WakeLockOptions} that specifies whether the device is allowed to turn off screen or turn down brightness
	 */
	public WakeLockOptions getWakeLockOptions() {
		return this.mWakeLockOptions;
	}

	/**
	 * @param pWakeLockOptions The {@link WakeLockOptions} that specifies whether the device is allowed to turn off screen or turn down brightness
	 * @return The new {@link EngineOptions} (not a copy)
	 */
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
}