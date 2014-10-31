package org.andengine.extension.ui.livewallpaper;

import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.opengl.GLWallpaperService;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.sensor.orientation.IOrientationListener;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.shader.ShaderProgramManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.ConfigChooser;
import org.andengine.opengl.view.EngineRenderer;
import org.andengine.opengl.view.IRendererListener;
import org.andengine.ui.IGameInterface;
import org.andengine.util.debug.Debug;

import android.app.WallpaperManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;

/**
 * (c) Nicolas Gramlich 2010
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 7:32:25 PM - Nov 3, 2011
 */
public abstract class BaseLiveWallpaperService extends GLWallpaperService implements IGameInterface, IRendererListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected EngineOptions mEngineOptions;
	protected org.andengine.engine.Engine mEngine;

	private boolean mGamePaused;
	private boolean mGameCreated;
	private boolean mCreateGameCalled;
	private boolean mOnReloadResourcesScheduled;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate() {
		Debug.d(this.getClass().getSimpleName() + ".onCreate" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		super.onCreate();

		this.mGamePaused = true;

		this.mEngineOptions = this.onCreateEngineOptions();
		this.mEngine = this.onCreateEngine(this.mEngineOptions);
		this.mEngine.startUpdateThread();

		this.applyEngineOptions();
	}

	@Override
	public org.andengine.engine.Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new org.andengine.engine.Engine(pEngineOptions);
	}

	@Override
	public synchronized void onSurfaceCreated(final GLState pGLState) {
		Debug.d(this.getClass().getSimpleName() + ".onSurfaceCreated" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		if(this.mGameCreated) {
			this.onReloadResources();
		} else {
			if(this.mCreateGameCalled) {
				this.mOnReloadResourcesScheduled = true;
			} else {
				this.mCreateGameCalled = true;
				this.onCreateGame();
			}
		}
	}

	@Override
	public void onSurfaceChanged(final GLState pGLState, final int pWidth, final int pHeight) {
		Debug.d(this.getClass().getSimpleName() + ".onSurfaceChanged(Width=" + pWidth + ",  Height=" + pHeight + ")" + " @(Thread: '" + Thread.currentThread().getName() + "')");
	}

	protected void onCreateGame() {
		Debug.d(this.getClass().getSimpleName() + ".onCreateGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		final OnPopulateSceneCallback onPopulateSceneCallback = new OnPopulateSceneCallback() {
			@Override
			public void onPopulateSceneFinished() {
				try {
					Debug.d(BaseLiveWallpaperService.this.getClass().getSimpleName() + ".onGameCreated" + " @(Thread: '" + Thread.currentThread().getName() + "')");

					BaseLiveWallpaperService.this.onGameCreated();
				} catch(final Throwable pThrowable) {
					Debug.e(BaseLiveWallpaperService.this.getClass().getSimpleName() + ".onGameCreated failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
				}

				BaseLiveWallpaperService.this.onResumeGame();
			}
		};

		final OnCreateSceneCallback onCreateSceneCallback = new OnCreateSceneCallback() {
			@Override
			public void onCreateSceneFinished(final Scene pScene) {
				BaseLiveWallpaperService.this.mEngine.setScene(pScene);

				try {
					Debug.d(BaseLiveWallpaperService.this.getClass().getSimpleName() + ".onPopulateScene" + " @(Thread: '" + Thread.currentThread().getName() + "')");

					BaseLiveWallpaperService.this.onPopulateScene(pScene, onPopulateSceneCallback);
				} catch(final Throwable pThrowable) {
					Debug.e(BaseLiveWallpaperService.this.getClass().getSimpleName() + ".onPopulateScene failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
				}
			}
		};

		final OnCreateResourcesCallback onCreateResourcesCallback = new OnCreateResourcesCallback() {
			@Override
			public void onCreateResourcesFinished() {
				try {
					Debug.d(BaseLiveWallpaperService.this.getClass().getSimpleName() + ".onCreateScene" + " @(Thread: '" + Thread.currentThread().getName() + "')");

					BaseLiveWallpaperService.this.onCreateScene(onCreateSceneCallback);
				} catch(final Throwable pThrowable) {
					Debug.e(BaseLiveWallpaperService.this.getClass().getSimpleName() + ".onCreateScene failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
				}
			}
		};

		try {
			Debug.d(this.getClass().getSimpleName() + ".onCreateResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");

			this.onCreateResources(onCreateResourcesCallback);
		} catch(final Throwable pThrowable) {
			Debug.e(this.getClass().getSimpleName() + ".onCreateGame failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
		}
	}

	@Override
	public synchronized void onGameCreated() {
		this.mGameCreated = true;

		/* Since the potential asynchronous resource creation,
		 * the surface might already be invalid
		 * and a resource reloading might be necessary. */
		if(this.mOnReloadResourcesScheduled) {
			this.mOnReloadResourcesScheduled = false;
			try {
				this.onReloadResources();
			} catch(final Throwable pThrowable) {
				Debug.e(this.getClass().getSimpleName() + ".onReloadResources failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
			}
		}
	}

	protected synchronized void onResume() {
		Debug.d(this.getClass().getSimpleName() + ".onResume" + " @(Thread: '" + Thread.currentThread().getName() + "')");
	}

	@Override
	public synchronized void onResumeGame() {
		Debug.d(this.getClass().getSimpleName() + ".onResumeGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		this.mEngine.start();

		this.mGamePaused = false;
	}

	@Override
	public void onReloadResources() {
		Debug.d(this.getClass().getSimpleName() + ".onReloadResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		this.mEngine.onReloadResources();

		this.onResumeGame();
	}

	protected void onPause(){
		Debug.d(this.getClass().getSimpleName() + ".onPause" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		if(!this.mGamePaused) {
			this.onPauseGame();
		}
	}

	@Override
	public void onPauseGame() {
		Debug.d(this.getClass().getSimpleName() + ".onPauseGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		this.mGamePaused = true;

		this.mEngine.stop();
	}

	@Override
	public void onDestroy() {
		Debug.d(this.getClass().getSimpleName() + ".onDestroy" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		super.onDestroy();

		this.mEngine.onDestroy();

		try {
			this.onDestroyResources();
		} catch (final Throwable pThrowable) {
			Debug.e(this.getClass().getSimpleName() + ".onDestroyResources failed." + " @(Thread: '" + Thread.currentThread().getName() + "')", pThrowable);
		}

		this.onGameDestroyed();
	}

	@Override
	public void onDestroyResources() throws Exception {
		Debug.d(this.getClass().getSimpleName() + ".onDestroyResources" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		if(this.mEngine.getEngineOptions().getAudioOptions().needsMusic()) {
			this.mEngine.getMusicManager().releaseAll();
		}

		if(this.mEngine.getEngineOptions().getAudioOptions().needsSound()) {
			this.mEngine.getSoundManager().releaseAll();
		}
	}

	@Override
	public synchronized void onGameDestroyed() {
		Debug.d(this.getClass().getSimpleName() + ".onGameDestroyed" + " @(Thread: '" + Thread.currentThread().getName() + "')");

		this.mGameCreated = false;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public org.andengine.engine.Engine getEngine() {
		return this.mEngine;
	}

	public boolean isGamePaused() {
		return this.mGamePaused;
	}

	public boolean isGameRunning() {
		return !this.mGamePaused;
	}

	public boolean isGameLoaded() {
		return this.mGameCreated;
	}

	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return this.mEngine.getVertexBufferObjectManager();
	}

	public TextureManager getTextureManager() {
		return this.mEngine.getTextureManager();
	}

	public FontManager getFontManager() {
		return this.mEngine.getFontManager();
	}

	public ShaderProgramManager getShaderProgramManager() {
		return this.mEngine.getShaderProgramManager();
	}

	public SoundManager getSoundManager() throws IllegalStateException {
		return this.mEngine.getSoundManager();
	}

	public MusicManager getMusicManager() throws IllegalStateException {
		return this.mEngine.getMusicManager();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onCreateEngine() {
		return new BaseWallpaperGLEngine(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onTap(final int pX, final int pY) {
		this.mEngine.onTouch(null, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, pX, pY, 0));
	}

	protected void onDrop(final int pX, final int pY) {

	}

	protected void onOffsetsChanged(final float pXOffset, final float pYOffset, final float pXOffsetStep, final float pYOffsetStep, final int pXPixelOffset, final int pYPixelOffset) {

	}

	protected void applyEngineOptions() {

	}

	protected boolean enableVibrator() {
		return this.mEngine.enableVibrator(this);
	}

	protected boolean enableAccelerationSensor(final IAccelerationListener pAccelerationListener) {
		return this.mEngine.enableAccelerationSensor(this, pAccelerationListener);
	}

	protected boolean enableOrientationSensor(final IOrientationListener pOrientationListener) {
		return this.mEngine.enableOrientationSensor(this, pOrientationListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected class BaseWallpaperGLEngine extends GLEngine {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private EngineRenderer mEngineRenderer;
		private ConfigChooser mConfigChooser;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BaseWallpaperGLEngine(final IRendererListener pRendererListener) {
			if(this.mConfigChooser == null) {
				this.mConfigChooser = new ConfigChooser(BaseLiveWallpaperService.this.mEngine.getEngineOptions().getRenderOptions().isMultiSampling());
			}
			this.setEGLConfigChooser(this.mConfigChooser);

			this.mEngineRenderer = new EngineRenderer(BaseLiveWallpaperService.this.mEngine, this.mConfigChooser, pRendererListener);
			this.setRenderer(this.mEngineRenderer);
			this.setRenderMode(GLEngine.RENDERMODE_CONTINUOUSLY);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public Bundle onCommand(final String pAction, final int pX, final int pY, final int pZ, final Bundle pExtras, final boolean pResultRequested) {
			if(pAction.equals(WallpaperManager.COMMAND_TAP)) {
				BaseLiveWallpaperService.this.onTap(pX, pY);
			} else if (pAction.equals(WallpaperManager.COMMAND_DROP)) {
				BaseLiveWallpaperService.this.onDrop(pX, pY);
			}

			return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
		}

		@Override
		public void onOffsetsChanged(final float pXOffset, final float pYOffset, final float pXOffsetStep, final float pYOffsetStep, final int pXPixelOffset, final int pYPixelOffset) {
			BaseLiveWallpaperService.this.onOffsetsChanged(pXOffset, pYOffset, pXOffsetStep, pYOffsetStep, pXPixelOffset, pYPixelOffset);
		}

		@Override
		public void onResume() {
			super.onResume();

			BaseLiveWallpaperService.this.getEngine().onReloadResources();
			BaseLiveWallpaperService.this.onResume();
		}

		@Override
		public void onPause() {
			super.onPause();

			BaseLiveWallpaperService.this.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();

			this.mEngineRenderer = null;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}