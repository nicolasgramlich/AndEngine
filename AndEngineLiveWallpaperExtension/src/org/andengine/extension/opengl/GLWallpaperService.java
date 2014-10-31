package org.andengine.extension.opengl;

import org.andengine.util.debug.Debug;

import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 01:59:16 - 10.11.2011
 */
public class GLWallpaperService extends WallpaperService {
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
	public Engine onCreateEngine() {
		return new GLEngine();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class GLEngine extends Engine {
		// ===========================================================
		// Constants
		// ===========================================================

		public final static int RENDERMODE_WHEN_DIRTY = 0;
		public final static int RENDERMODE_CONTINUOUSLY = 1;

		// ===========================================================
		// Fields
		// ===========================================================

		private GLThread mGLThread;
		private EGLConfigChooser mEGLConfigChooser;
		private int mDebugFlags;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getDebugFlags() {
			return this.mDebugFlags;
		}

		public void setDebugFlags(final int pDebugFlags) {
			this.mDebugFlags = pDebugFlags;
		}

		public void setRenderer(final Renderer pRenderer) {
			this.checkRenderThreadState();

			if (this.mEGLConfigChooser == null) {
				throw new IllegalStateException("EGLConfigChooser must not be null.");
			}

			this.mGLThread = new GLThread(pRenderer, this.mEGLConfigChooser);
			this.mGLThread.start();
		}

		public void setEGLConfigChooser(final EGLConfigChooser pEGLConfigChooser) {
			this.checkRenderThreadState();

			this.mEGLConfigChooser = pEGLConfigChooser;
		}

		public int getRenderMode() {
			return this.mGLThread.getRenderMode();
		}

		public void setRenderMode(final int pRenderMode) {
			this.mGLThread.setRenderMode(pRenderMode);
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onVisibilityChanged(final boolean pVisibility) {
			Debug.d(this.getClass().getSimpleName() + ".onVisibilityChanged(" + pVisibility + ")");

			if (pVisibility) {
				this.onResume();
			} else {
				this.onPause();
			}

			super.onVisibilityChanged(pVisibility);
		}

		@Override
		public void onSurfaceChanged(final SurfaceHolder pSurfaceHolder, final int pFormat, final int pWidth, final int pHeight) {
			this.mGLThread.onWindowResize(pWidth, pHeight);

			super.onSurfaceChanged(pSurfaceHolder, pFormat, pWidth, pHeight);
		}

		@Override
		public void onSurfaceCreated(final SurfaceHolder pSurfaceHolder) {
			this.mGLThread.surfaceCreated(pSurfaceHolder);

			super.onSurfaceCreated(pSurfaceHolder);
		}

		@Override
		public void onSurfaceDestroyed(final SurfaceHolder pSurfaceHolder) {
			this.mGLThread.surfaceDestroyed();

			super.onSurfaceDestroyed(pSurfaceHolder);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		public void requestRender() {
			this.mGLThread.requestRender();
		}

		public void onPause() {
			this.mGLThread.onPause();
		}

		public void onResume() {
			this.mGLThread.onResume();
		}

		public void queueEvent(final Runnable pRunnable) {
			this.mGLThread.queueEvent(pRunnable);
		}

		private void checkRenderThreadState() {
			if (this.mGLThread != null) {
				throw new IllegalStateException("setRenderer has already been called for this instance.");
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}