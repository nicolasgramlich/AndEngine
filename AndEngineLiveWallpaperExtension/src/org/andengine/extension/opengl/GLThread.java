package org.andengine.extension.opengl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.extension.opengl.GLWallpaperService.GLEngine;

import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;

class GLThread extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final GLThreadManager sGLThreadManager = new GLThreadManager();

	private GLThread mEGLOwnerThread;

	private final EGLConfigChooser mEGLConfigChooser;

	public SurfaceHolder mSurfaceHolder;
	private boolean mSizeChanged = true;

	// Once the thread is started, all accesses to the following member
	// variables are protected by the sGLThreadManager monitor
	public boolean mDone;
	private boolean mPaused;
	private boolean mHasSurface;
	private boolean mWaitingForSurface;
	private boolean mHaveEGL;
	private int mWidth;
	private int mHeight;
	private int mRenderMode = GLEngine.RENDERMODE_CONTINUOUSLY;
	private boolean mRequestRender = true;
	private boolean mEventsWaiting;
	// End of member variables protected by the sGLThreadManager monitor.

	private final Renderer mRenderer;
	private final ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();
	private EGLHelper mEGLHelper;

	// ===========================================================
	// Constructors
	// ===========================================================

	GLThread(final Renderer pRenderer, final EGLConfigChooser pEGLConfigChooser) {
		this.mRenderer = pRenderer;
		this.mEGLConfigChooser = pEGLConfigChooser;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	private boolean isDone() {
		synchronized (this.sGLThreadManager) {
			return this.mDone;
		}
	}

	public int getRenderMode() {
		synchronized (this.sGLThreadManager) {
			return this.mRenderMode;
		}
	}

	public void setRenderMode(final int pRenderMode) {
		if (GLEngine.RENDERMODE_WHEN_DIRTY != pRenderMode && pRenderMode != GLEngine.RENDERMODE_CONTINUOUSLY) {
			throw new IllegalArgumentException("Illegal pRenderMode: '" + pRenderMode + "'.");
		}
		synchronized (this.sGLThreadManager) {
			this.mRenderMode = pRenderMode;
			if (pRenderMode == GLEngine.RENDERMODE_CONTINUOUSLY) {
				this.sGLThreadManager.notifyAll();
			}
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		this.setName("GLThread " + this.getId());

		try {
			this.guardedRun();
		} catch (final InterruptedException e) {
			/* Fall through and exit normally. */
		} finally {
			this.sGLThreadManager.threadExiting(this);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * This private method should only be called inside a synchronized(sGLThreadManager) block.
	 */
	private void stopEGLLocked() {
		if (this.mHaveEGL) {
			this.mHaveEGL = false;
			this.mEGLHelper.destroySurface();
			this.mEGLHelper.finish();
			this.sGLThreadManager.releaseEglSurface(this);
		}
	}

	private void guardedRun() throws InterruptedException {
		this.mEGLHelper = new EGLHelper(this.mEGLConfigChooser);
		try {
			GL10 gl = null;
			boolean tellRendererSurfaceCreated = true;
			boolean tellRendererSurfaceChanged = true;

			/* This is our main activity thread's loop, we go until asked to quit. */
			while (!this.isDone()) {
				/* Update the asynchronous state (window size). */
				int w = 0;
				int h = 0;
				boolean changed = false;
				boolean needStart = false;
				boolean eventsWaiting = false;

				synchronized (this.sGLThreadManager) {
					while (true) {
						/* Manage acquiring and releasing the SurfaceView surface and the EGL surface. */
						if (this.mPaused) {
							this.stopEGLLocked();
						}
						if (!this.mHasSurface) {
							if (!this.mWaitingForSurface) {
								this.stopEGLLocked();
								this.mWaitingForSurface = true;
								this.sGLThreadManager.notifyAll();
							}
						} else {
							if (!this.mHaveEGL) {
								if (this.sGLThreadManager.tryAcquireEglSurface(this)) {
									this.mHaveEGL = true;
									this.mEGLHelper.start();
									this.mRequestRender = true;
									needStart = true;
								}
							}
						}

						/* Check if we need to wait. If not, update any state that needs to be updated,
						 * copy any state thatneeds to be copied, and use "break" to exit the wait loop. */
						if (this.mDone) {
							return;
						}

						if (this.mEventsWaiting) {
							eventsWaiting = true;
							this.mEventsWaiting = false;
							break;
						}

						if ((!this.mPaused) && this.mHasSurface && this.mHaveEGL && (this.mWidth > 0) && (this.mHeight > 0) && (this.mRequestRender || (this.mRenderMode == GLEngine.RENDERMODE_CONTINUOUSLY))) {
							changed = this.mSizeChanged;
							w = this.mWidth;
							h = this.mHeight;
							this.mSizeChanged = false;
							this.mRequestRender = false;
							if (this.mHasSurface && this.mWaitingForSurface) {
								changed = true;
								this.mWaitingForSurface = false;
								this.sGLThreadManager.notifyAll();
							}
							break;
						}

						this.sGLThreadManager.wait();
					}
				}

				/* Handle queued events. */
				if (eventsWaiting) {
					Runnable r;
					while ((r = this.getEvent()) != null) {
						r.run();
						if (this.isDone()) {
							return;
						}
					}
					/* Go back and see if we need to wait to render. */
					continue;
				}

				if (needStart) {
					tellRendererSurfaceCreated = true;
					changed = true;
				}
				if (changed) {
					gl = (GL10) this.mEGLHelper.createSurface(this.mSurfaceHolder);
					tellRendererSurfaceChanged = true;
				}
				if (tellRendererSurfaceCreated) {
					this.mRenderer.onSurfaceCreated(gl, this.mEGLHelper.mEGLConfig);
					tellRendererSurfaceCreated = false;
				}
				if (tellRendererSurfaceChanged) {
					this.mRenderer.onSurfaceChanged(gl, w, h);
					tellRendererSurfaceChanged = false;
				}
				if ((w > 0) && (h > 0)) {
					/* draw a frame here */
					this.mRenderer.onDrawFrame(gl);

					/* Once we're done with GL, we need to call swapBuffers() to
					 * instruct the system to display the rendered frame. */
					this.mEGLHelper.swapBuffers();
				}
			}
		} finally {
			/* Clean-up everything. */
			synchronized (this.sGLThreadManager) {
				this.stopEGLLocked();
			}
		}
	}

	public void requestRender() {
		synchronized (this.sGLThreadManager) {
			this.mRequestRender = true;
			this.sGLThreadManager.notifyAll();
		}
	}

	public void surfaceCreated(final SurfaceHolder holder) {
		this.mSurfaceHolder = holder;
		synchronized (this.sGLThreadManager) {
			this.mHasSurface = true;
			this.sGLThreadManager.notifyAll();
		}
	}

	public void surfaceDestroyed() {
		synchronized (this.sGLThreadManager) {
			this.mHasSurface = false;
			this.sGLThreadManager.notifyAll();
			while (!this.mWaitingForSurface && this.isAlive() && !this.mDone) {
				try {
					this.sGLThreadManager.wait();
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void onPause() {
		synchronized (this.sGLThreadManager) {
			this.mPaused = true;
			this.sGLThreadManager.notifyAll();
		}
	}

	public void onResume() {
		synchronized (this.sGLThreadManager) {
			this.mPaused = false;
			this.mRequestRender = true;
			this.sGLThreadManager.notifyAll();
		}
	}

	public void onWindowResize(final int w, final int h) {
		synchronized (this.sGLThreadManager) {
			this.mWidth = w;
			this.mHeight = h;
			this.mSizeChanged = true;
			this.sGLThreadManager.notifyAll();
		}
	}

	public void requestExitAndWait() {
		// don't call this from GLThread thread or it is a guaranteed
		// deadlock!
		synchronized (this.sGLThreadManager) {
			this.mDone = true;
			this.sGLThreadManager.notifyAll();
		}
		try {
			this.join();
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Queue an "event" to be run on the GL rendering thread.
	 * 
	 * @param r
	 *            the runnable to be run on the GL rendering thread.
	 */
	public void queueEvent(final Runnable r) {
		synchronized (this) {
			this.mEventQueue.add(r);
			synchronized (this.sGLThreadManager) {
				this.mEventsWaiting = true;
				this.sGLThreadManager.notifyAll();
			}
		}
	}

	private Runnable getEvent() {
		synchronized (this) {
			if (this.mEventQueue.size() > 0) {
				return this.mEventQueue.remove(0);
			}

		}
		return null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class GLThreadManager {

		public synchronized void threadExiting(final GLThread thread) {
			thread.mDone = true;
			if (GLThread.this.mEGLOwnerThread == thread) {
				GLThread.this.mEGLOwnerThread = null;
			}
			this.notifyAll();
		}

		/*
		 * Tries once to acquire the right to use an EGL surface. Does not
		 * block.
		 * 
		 * @return true if the right to use an EGL surface was acquired.
		 */
		public synchronized boolean tryAcquireEglSurface(final GLThread thread) {
			if (GLThread.this.mEGLOwnerThread == thread || GLThread.this.mEGLOwnerThread == null) {
				GLThread.this.mEGLOwnerThread = thread;
				this.notifyAll();
				return true;
			}
			return false;
		}

		public synchronized void releaseEglSurface(final GLThread thread) {
			if (GLThread.this.mEGLOwnerThread == thread) {
				GLThread.this.mEGLOwnerThread = null;
			}
			this.notifyAll();
		}
	}
}