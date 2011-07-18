// ############################################################
// ############################################################
//
// This class is a replacement for the original GLSurfaceView, due to issue:
// http://code.google.com/p/android/issues/detail?id=2828
//
// Reason: Two sequential Activities using a GLSurfaceView leads to a deadlock in the GLThread!
//
// ############################################################
// ############################################################

package org.anddev.andengine.opengl.view;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * An implementation of SurfaceView that uses the dedicated surface for
 * displaying OpenGL rendering.
 * <p>
 * A GLSurfaceView provides the following features:
 * <p>
 * <ul>
 * <li>Manages a surface, which is a special piece of memory that can be
 * composited into the Android view system.
 * <li>Manages an EGL display, which enables OpenGL to render into a surface.
 * <li>Accepts a user-provided Renderer object that does the actual rendering.
 * <li>Renders on a dedicated thread to decouple rendering performance from the
 * UI thread.
 * <li>Supports both on-demand and continuous rendering.
 * <li>Optionally wraps, traces, and/or error-checks the renderer'MAGIC_CONSTANT OpenGL
 * calls.
 * </ul>
 * 
 * <h3>Using GLSurfaceView</h3>
 * <p>
 * Typically you use GLSurfaceView by subclassing it and overriding one or more
 * of the View system input event methods. If your application does not need to
 * override event methods then GLSurfaceView can be used as-is. For the most
 * part GLSurfaceView behavior is customized by calling "set" methods rather
 * than by subclassing. For example, unlike a regular View, drawing is delegated
 * to a separate Renderer object which is registered with the GLSurfaceView
 * using the {@link #setRenderer(Renderer)} call.
 * <p>
 * <h3>Initializing GLSurfaceView</h3>
 * All you have to do to initialize a GLSurfaceView is call
 * {@link #setRenderer(Renderer)}. However, if desired, you can modify the
 * default behavior of GLSurfaceView by calling one or more of these methods
 * before calling setRenderer:
 * <ul>
 * <li>{@link #setDebugFlags(int)}
 * <li>{@link #setEGLConfigChooser(boolean)}
 * <li>{@link #setEGLConfigChooser(EGLConfigChooser)}
 * <li>{@link #setEGLConfigChooser(int, int, int, int, int, int)}
 * <li>{@link #setGLWrapper(GLWrapper)}
 * </ul>
 * <p>
 * <h4>Choosing an EGL Configuration</h4>
 * A given Android device may support multiple possible types of drawing
 * surfaces. The available surfaces may differ in how may channels of data are
 * present, as well as how many bits are allocated to each channel. Therefore,
 * the first thing GLSurfaceView has to do when starting to render is choose
 * what type of surface to use.
 * <p>
 * By default GLSurfaceView chooses an available surface that'MAGIC_CONSTANT closest to a
 * 16-bit R5G6B5 surface with a 16-bit depth buffer and no stencil. If you would
 * prefer a different surface (for example, if you do not need a depth buffer)
 * you can override the default behavior by calling one of the
 * setEGLConfigChooser methods.
 * <p>
 * <h4>Debug Behavior</h4>
 * You can optionally modify the behavior of GLSurfaceView by calling one or
 * more of the debugging methods {@link #setDebugFlags(int)}, and
 * {@link #setGLWrapper}. These methods may be called before and/or after
 * setRenderer, but typically they are called before setRenderer so that they
 * take effect immediately.
 * <p>
 * <h4>Setting a Renderer</h4>
 * Finally, you must call {@link #setRenderer} to register a {@link Renderer}.
 * The renderer is responsible for doing the actual OpenGL rendering.
 * <p>
 * <h3>Rendering Mode</h3>
 * Once the renderer is set, you can control whether the renderer draws
 * continuously or on-demand by calling {@link #setRenderMode}. The default is
 * continuous rendering.
 * <p>
 * <h3>Activity Life-cycle</h3>
 * A GLSurfaceView must be notified when the activity is paused and resumed.
 * GLSurfaceView clients are required to call {@link #onPause()} when the
 * activity pauses and {@link #onResume()} when the activity resumes. These
 * calls allow GLSurfaceView to pause and resume the rendering thread, and also
 * allow GLSurfaceView to release and recreate the OpenGL display.
 * <p>
 * <h3>Handling events</h3>
 * <p>
 * To handle an event you will typically subclass GLSurfaceView and override the
 * appropriate method, just as you would with any other View. However, when
 * handling the event, you may need to communicate with the Renderer object
 * that'MAGIC_CONSTANT running in the rendering thread. You can do this using any standard
 * Java cross-thread communication mechanism. In addition, one relatively easy
 * way to communicate with your renderer is to call
 * {@link #queueEvent(Runnable)}. For example:
 * 
 * <pre class="prettyprint">
 * class MyGLSurfaceView extends GLSurfaceView {
 * 
 * 	private MyRenderer mMyRenderer;
 * 
 * 	public void start() {
 *         mMyRenderer = ...;
 *         setRenderer(mMyRenderer);
 *     }
 * 
 * 	public boolean onKeyDown(int keyCode, KeyEvent event) {
 * 		if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
 * 			queueEvent(new Runnable() {
 * 				// This method will be called on the rendering
 * 				// thread:
 * 				public void run() {
 * 					mMyRenderer.handleDpadCenter();
 * 				}
 * 			});
 * 			return true;
 * 		}
 * 		return super.onKeyDown(keyCode, event);
 * 	}
 * }
 * </pre>
 * 
 */
public class GLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	// ===========================================================
	// Constants
	// ===========================================================

	/**
	 * The renderer only renders when the surface is created, or when
	 * {@link #requestRender} is called.
	 * 
	 * @see #getRenderMode()
	 * @see #setRenderMode(int)
	 */
	public final static int RENDERMODE_WHEN_DIRTY = 0;
	/**
	 * The renderer is called continuously to re-render the scene.
	 * 
	 * @see #getRenderMode()
	 * @see #setRenderMode(int)
	 * @see #requestRender()
	 */
	public final static int RENDERMODE_CONTINUOUSLY = 1;

	/**
	 * Check glError() after every GL call and throw an exception if glError
	 * indicates that an error has occurred. This can be used to help track down
	 * which OpenGL ES call is causing an error.
	 * 
	 * @see #getDebugFlags
	 * @see #setDebugFlags
	 */
	public final static int DEBUG_CHECK_GL_ERROR = 1;

	/**
	 * Log GL calls to the system log at "verbose" level with tag
	 * "GLSurfaceView".
	 * 
	 * @see #getDebugFlags
	 * @see #setDebugFlags
	 */
	public final static int DEBUG_LOG_GL_CALLS = 2;

	private static final Semaphore sEglSemaphore = new Semaphore(1);

	// ===========================================================
	// Fields
	// ===========================================================

	private GLThread mGLThread;
	private EGLConfigChooser mEGLConfigChooser;
	private GLWrapper mGLWrapper;
	private int mDebugFlags;
	private int mRenderMode;
	private Renderer mRenderer;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private boolean mHasSurface;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Standard View constructor. In order to render something, you must call
	 * {@link #setRenderer} to register a renderer.
	 */
	public GLSurfaceView(final Context context) {
		super(context);
		this.init();
	}

	/**
	 * Standard View constructor. In order to render something, you must call
	 * {@link #setRenderer} to register a renderer.
	 */
	public GLSurfaceView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	private void init() {
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed
		final SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		this.mRenderMode = RENDERMODE_CONTINUOUSLY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Set the glWrapper. If the glWrapper is not null, its
	 * {@link GLWrapper#wrap(GL)} method is called whenever a surface is
	 * created. A GLWrapper can be used to wrap the GL object that'MAGIC_CONSTANT passed to
	 * the renderer. Wrapping a GL object enables examining and modifying the
	 * behavior of the GL calls made by the renderer.
	 * <p>
	 * Wrapping is typically used for debugging purposes.
	 * <p>
	 * The default value is null.
	 * 
	 * @param glWrapper
	 *            the new GLWrapper
	 */
	public void setGLWrapper(final GLWrapper glWrapper) {
		this.mGLWrapper = glWrapper;
	}

	/**
	 * Set the debug flags to a new value. The value is constructed by
	 * OR-together zero or more of the DEBUG_CHECK_* constants. The debug flags
	 * take effect whenever a surface is created. The default value is zero.
	 * 
	 * @param debugFlags
	 *            the new debug flags
	 * @see #DEBUG_CHECK_GL_ERROR
	 * @see #DEBUG_LOG_GL_CALLS
	 */
	public void setDebugFlags(final int debugFlags) {
		this.mDebugFlags = debugFlags;
	}

	/**
	 * Get the current value of the debug flags.
	 * 
	 * @return the current value of the debug flags.
	 */
	public int getDebugFlags() {
		return this.mDebugFlags;
	}

	/**
	 * Set the renderer associated with this view. Also starts the thread that
	 * will call the renderer, which in turn causes the rendering to start.
	 * <p>
	 * This method should be called once and only once in the life-cycle of a
	 * GLSurfaceView.
	 * <p>
	 * The following GLSurfaceView methods can only be called <em>before</em>
	 * setRenderer is called:
	 * <ul>
	 * <li>{@link #setEGLConfigChooser(boolean)}
	 * <li>{@link #setEGLConfigChooser(EGLConfigChooser)}
	 * <li>{@link #setEGLConfigChooser(int, int, int, int, int, int)}
	 * </ul>
	 * <p>
	 * The following GLSurfaceView methods can only be called <em>after</em>
	 * setRenderer is called:
	 * <ul>
	 * <li>{@link #getRenderMode()}
	 * <li>{@link #onPause()}
	 * <li>{@link #onResume()}
	 * <li>{@link #queueEvent(Runnable)}
	 * <li>{@link #requestRender()}
	 * <li>{@link #setRenderMode(int)}
	 * </ul>
	 * 
	 * @param renderer
	 *            the renderer to use to perform OpenGL drawing.
	 */
	public void setRenderer(final Renderer renderer) {
		if(this.mRenderer != null) {
			throw new IllegalStateException("setRenderer has already been called for this instance.");
		}

		this.mRenderer = renderer;
	}

	/**
	 * Install a custom EGLConfigChooser.
	 * <p>
	 * If this method is called, it must be called before
	 * {@link #setRenderer(Renderer)} is called.
	 * <p>
	 * If no setEGLConfigChooser method is called, then by default the view will
	 * choose a config as close to 16-bit RGB as possible, with a depth buffer
	 * as close to 16 bits as possible.
	 * 
	 * @param configChooser
	 */
	public void setEGLConfigChooser(final EGLConfigChooser configChooser) {
		if(this.mRenderer != null) {
			throw new IllegalStateException("setRenderer has already been called for this instance.");
		}
		this.mEGLConfigChooser = configChooser;
	}

	/**
	 * Install a config chooser which will choose a config as close to 16-bit
	 * RGB as possible, with or without an optional depth buffer as close to
	 * 16-bits as possible.
	 * <p>
	 * If this method is called, it must be called before
	 * {@link #setRenderer(Renderer)} is called.
	 * <p>
	 * If no setEGLConfigChooser method is called, then by default the view will
	 * choose a config as close to 16-bit RGB as possible, with a depth buffer
	 * as close to 16 bits as possible.
	 * 
	 * @param needDepth
	 */
	public void setEGLConfigChooser(final boolean needDepth) {
		this.setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
	}

	/**
	 * Install a config chooser which will choose a config with at least the
	 * specified component sizes, and as close to the specified component sizes
	 * as possible.
	 * <p>
	 * If this method is called, it must be called before
	 * {@link #setRenderer(Renderer)} is called.
	 * <p>
	 * If no setEGLConfigChooser method is called, then by default the view will
	 * choose a config as close to 16-bit RGB as possible, with a depth buffer
	 * as close to 16 bits as possible.
	 * 
	 */
	public void setEGLConfigChooser(final int redSize, final int greenSize, final int blueSize, final int alphaSize, final int depthSize, final int stencilSize) {
		this.setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize));
	}

	/**
	 * Set the rendering mode. When renderMode is RENDERMODE_CONTINUOUSLY, the
	 * renderer is called repeatedly to re-render the scene. When renderMode is
	 * RENDERMODE_WHEN_DIRTY, the renderer only rendered when the surface is
	 * created, or when {@link #requestRender} is called. Defaults to
	 * RENDERMODE_CONTINUOUSLY.
	 * <p>
	 * Using RENDERMODE_WHEN_DIRTY can improve battery life and overall system
	 * performance by allowing the GPU and CPU to idle when the view does not
	 * need to be updated.
	 * <p>
	 * This method can only be called after {@link #setRenderer(Renderer)}
	 * 
	 * @param renderMode
	 *            one of the RENDERMODE_X constants
	 * @see #RENDERMODE_CONTINUOUSLY
	 * @see #RENDERMODE_WHEN_DIRTY
	 */
	public void setRenderMode(final int renderMode) {
		this.mRenderMode = renderMode;
		if(this.mGLThread != null) {
			this.mGLThread.setRenderMode(renderMode);
		}
	}

	/**
	 * Get the current rendering mode. May be called from any thread. Must not
	 * be called before a renderer has been set.
	 * 
	 * @return the current rendering mode.
	 * @see #RENDERMODE_CONTINUOUSLY
	 * @see #RENDERMODE_WHEN_DIRTY
	 */
	public int getRenderMode() {
		return this.mRenderMode;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Request that the renderer render a frame. This method is typically used
	 * when the render mode has been set to {@link #RENDERMODE_WHEN_DIRTY}, so
	 * that frames are only rendered on demand. May be called from any thread.
	 * Must be called after onResume() and before onPause().
	 */
	public void requestRender() {
		this.mGLThread.requestRender();
	}

	/**
	 * This method is part of the SurfaceHolder.Callback interface, and is not
	 * normally called or subclassed by clients of GLSurfaceView.
	 */
	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		if(this.mGLThread != null) {
			this.mGLThread.surfaceCreated();
		}
		this.mHasSurface = true;
	}

	/**
	 * This method is part of the SurfaceHolder.Callback interface, and is not
	 * normally called or subclassed by clients of GLSurfaceView.
	 */
	@Override
	public void surfaceDestroyed(final SurfaceHolder holder) {
		// Surface will be destroyed when we return
		if(this.mGLThread != null) {
			this.mGLThread.surfaceDestroyed();
		}
		this.mHasSurface = false;
	}

	/**
	 * This method is part of the SurfaceHolder.Callback interface, and is not
	 * normally called or subclassed by clients of GLSurfaceView.
	 */
	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format, final int w, final int h) {
		if(this.mGLThread != null) {
			this.mGLThread.onWindowResize(w, h);
		}
		this.mSurfaceWidth = w;
		this.mSurfaceHeight = h;
	}

	/**
	 * Inform the view that the activity is paused. The owner of this view must
	 * call this method when the activity is paused. Calling this method will
	 * pause the rendering thread. Must not be called before a renderer has been
	 * set.
	 */
	public void onPause() {
		this.mGLThread.onPause();
		this.mGLThread.requestExitAndWait();
		this.mGLThread = null;
	}

	/**
	 * Inform the view that the activity is resumed. The owner of this view must
	 * call this method when the activity is resumed. Calling this method will
	 * recreate the OpenGL display and resume the rendering thread. Must not be
	 * called before a renderer has been set.
	 */
	public void onResume() {
		if(this.mEGLConfigChooser == null) {
			this.mEGLConfigChooser = new SimpleEGLConfigChooser(true);
		}
		this.mGLThread = new GLThread(this.mRenderer);
		this.mGLThread.start();
		this.mGLThread.setRenderMode(this.mRenderMode);
		if(this.mHasSurface) {
			this.mGLThread.surfaceCreated();
		}
		if(this.mSurfaceWidth > 0 && this.mSurfaceHeight > 0) {
			this.mGLThread.onWindowResize(this.mSurfaceWidth, this.mSurfaceHeight);
		}
		this.mGLThread.onResume();
	}

	/**
	 * Queue a runnable to be run on the GL rendering thread. This can be used
	 * to communicate with the Renderer on the rendering thread. Must be called
	 * after onResume() and before onPause().
	 * 
	 * @param r
	 *            the runnable to be run on the GL rendering thread.
	 */
	public void queueEvent(final Runnable r) {
		if(this.mGLThread != null) {
			this.mGLThread.queueEvent(r);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * A generic GL Thread. Takes care of initializing EGL and GL. Delegates to
	 * a Renderer instance to do the actual drawing. Can be configured to render
	 * continuously or on request.
	 * 
	 */
	class GLThread extends Thread {
		GLThread(final Renderer renderer) {
			super();
			this.mDone = false;
			this.mWidth = 0;
			this.mHeight = 0;
			this.mRequestRender = true;
			this.mRenderMode = RENDERMODE_CONTINUOUSLY;
			this.mRenderer = renderer;
			this.mSizeChanged = true;
			this.setName("GLThread");
		}

		@Override
		public void run() {
			/*
			 * When the android framework launches a second instance of an
			 * activity, the new instance'MAGIC_CONSTANT onCreate() method may be called
			 * before the first instance returns from onDestroy().
			 * 
			 * This semaphore ensures that only one instance at a time accesses
			 * EGL.
			 */
			try {
				try {
					sEglSemaphore.acquire();
				} catch (final InterruptedException e) {
					return;
				}
				this.guardedRun();
			} catch (final InterruptedException e) {
				// fall thru and exit normally
			} finally {
				sEglSemaphore.release();
			}
		}

		private void guardedRun() throws InterruptedException {
			this.mEglHelper = new EglHelper();
			this.mEglHelper.start();

			GL10 gl = null;
			boolean tellRendererSurfaceCreated = true;
			boolean tellRendererSurfaceChanged = true;

			/*
			 * This is our main activity thread'MAGIC_CONSTANT loop, we go until asked to
			 * quit.
			 */
			while(!this.mDone) {

				/*
				 * Update the asynchronous state (window size)
				 */
				int w, h;
				boolean changed;
				boolean needStart = false;
				synchronized (this) {
					Runnable r;
					while((r = this.getEvent()) != null) {
						r.run();
					}
					if(this.mPaused) {
						this.mEglHelper.finish();
						needStart = true;
					}
					while(this.needToWait()) {
						this.wait();
					}
					if(this.mDone) {
						break;
					}
					changed = this.mSizeChanged;
					w = this.mWidth;
					h = this.mHeight;
					this.mSizeChanged = false;
					this.mRequestRender = false;
				}
				if(needStart) {
					this.mEglHelper.start();
					tellRendererSurfaceCreated = true;
					changed = true;
				}
				if(changed) {
					gl = (GL10) this.mEglHelper.createSurface(GLSurfaceView.this.getHolder());
					tellRendererSurfaceChanged = true;
				}
				if(tellRendererSurfaceCreated) {
					this.mRenderer.onSurfaceCreated(gl, this.mEglHelper.mEglConfig);
					tellRendererSurfaceCreated = false;
				}
				if(tellRendererSurfaceChanged) {
					this.mRenderer.onSurfaceChanged(gl, w, h);
					tellRendererSurfaceChanged = false;
				}
				if((w > 0) && (h > 0)) {
					/* draw a frame here */
					this.mRenderer.onDrawFrame(gl);

					/*
					 * Once we're done with GL, we need to call swapBuffers() to
					 * instruct the system to display the rendered frame
					 */
					this.mEglHelper.swap();
				}
			}

			/*
			 * clean-up everything...
			 */
			this.mEglHelper.finish();
		}

		private boolean needToWait() {
			if(this.mDone) {
				return false;
			}

			if(this.mPaused || (!this.mHasSurface)) {
				return true;
			}

			if((this.mWidth > 0) && (this.mHeight > 0) && (this.mRequestRender || (this.mRenderMode == RENDERMODE_CONTINUOUSLY))) {
				return false;
			}

			return true;
		}

		public void setRenderMode(final int renderMode) {
			if(!((RENDERMODE_WHEN_DIRTY <= renderMode) && (renderMode <= RENDERMODE_CONTINUOUSLY))) {
				throw new IllegalArgumentException("renderMode");
			}
			synchronized (this) {
				this.mRenderMode = renderMode;
				if(renderMode == RENDERMODE_CONTINUOUSLY) {
					this.notify();
				}
			}
		}

		public int getRenderMode() {
			synchronized (this) {
				return this.mRenderMode;
			}
		}

		public void requestRender() {
			synchronized (this) {
				this.mRequestRender = true;
				this.notify();
			}
		}

		public void surfaceCreated() {
			synchronized (this) {
				this.mHasSurface = true;
				this.notify();
			}
		}

		public void surfaceDestroyed() {
			synchronized (this) {
				this.mHasSurface = false;
				this.notify();
			}
		}

		public void onPause() {
			synchronized (this) {
				this.mPaused = true;
			}
		}

		public void onResume() {
			synchronized (this) {
				this.mPaused = false;
				this.notify();
			}
		}

		public void onWindowResize(final int w, final int h) {
			synchronized (this) {
				this.mWidth = w;
				this.mHeight = h;
				this.mSizeChanged = true;
				this.notify();
			}
		}

		public void requestExitAndWait() {
			// don't call this from GLThread thread or it is a guaranteed
			// deadlock!
			synchronized (this) {
				this.mDone = true;
				this.notify();
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
			}
		}

		private Runnable getEvent() {
			synchronized (this) {
				if(this.mEventQueue.size() > 0) {
					return this.mEventQueue.remove(0);
				}

			}
			return null;
		}

		private boolean mDone;
		private boolean mPaused;
		private boolean mHasSurface;
		private int mWidth;
		private int mHeight;
		private int mRenderMode;
		private boolean mRequestRender;
		private final Renderer mRenderer;
		private final ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();
		private EglHelper mEglHelper;
		private boolean mSizeChanged;
	}

	/**
	 * An EGL helper class.
	 */

	class EglHelper {
		public EglHelper() {

		}

		/**
		 * Initialize EGL for a given configuration spec.
		 * 
		 * @param configSpec
		 */
		public void start() {
			/*
			 * Get an EGL instance
			 */
			this.mEgl = (EGL10) EGLContext.getEGL();

			/*
			 * Get to the default display.
			 */
			this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

			/*
			 * We can now initialize EGL for that display
			 */
			final int[] version = new int[2];
			this.mEgl.eglInitialize(this.mEglDisplay, version);
			this.mEglConfig = GLSurfaceView.this.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);

			/*
			 * Create an OpenGL ES context. This must be done only once, an
			 * OpenGL context is a somewhat heavy object.
			 */
			this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL10.EGL_NO_CONTEXT, null);

			this.mEglSurface = null;
		}

		/*
		 * React to the creation of a new surface by creating and returning an
		 * OpenGL interface that renders to that surface.
		 */
		public GL createSurface(final SurfaceHolder holder) {
			/*
			 * The window size has changed, so we need to create a new surface.
			 */
			if(this.mEglSurface != null) {

				/*
				 * Unbind and destroy the old EGL surface, if there is one.
				 */
				this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
			}

			/*
			 * Create an EGL surface we can render into.
			 */
			this.mEglSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, holder, null);

			/*
			 * Before we can issue GL commands, we need to make sure the context
			 * is current and bound to a surface.
			 */
			this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext);

			GL gl = this.mEglContext.getGL();
			if(GLSurfaceView.this.mGLWrapper != null) {
				gl = GLSurfaceView.this.mGLWrapper.wrap(gl);
			}

			/* Debugging disabled */
			/*
			 * if ((mDebugFlags & (DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS))!=
			 * 0) { int configFlags = 0; Writer log = null; if ((mDebugFlags &
			 * DEBUG_CHECK_GL_ERROR) != 0) { configFlags |=
			 * GLDebugHelper.CONFIG_CHECK_GL_ERROR; } if ((mDebugFlags &
			 * DEBUG_LOG_GL_CALLS) != 0) { log = new LogWriter(); } gl =
			 * GLDebugHelper.wrap(gl, configFlags, log); }
			 */
			return gl;
		}

		/**
		 * Display the current render surface.
		 * 
		 * @return false if the context has been lost.
		 */
		public boolean swap() {
			this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);

			/*
			 * Always check for EGL_CONTEXT_LOST, which means the context and
			 * all associated data were lost (For instance because the device
			 * went to sleep). We need to sleep until we get a new surface.
			 */
			return this.mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
		}

		public void finish() {
			if(this.mEglSurface != null) {
				this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
				this.mEglSurface = null;
			}
			if(this.mEglContext != null) {
				this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
				this.mEglContext = null;
			}
			if(this.mEglDisplay != null) {
				this.mEgl.eglTerminate(this.mEglDisplay);
				this.mEglDisplay = null;
			}
		}

		EGL10 mEgl;
		EGLDisplay mEglDisplay;
		EGLSurface mEglSurface;
		EGLConfig mEglConfig;
		EGLContext mEglContext;
	}

	/**
	 * A generic renderer interface.
	 * <p>
	 * The renderer is responsible for making OpenGL calls to render a frame.
	 * <p>
	 * GLSurfaceView clients typically create their own classes that implement
	 * this interface, and then call {@link GLSurfaceView#setRenderer} to
	 * register the renderer with the GLSurfaceView.
	 * <p>
	 * <h3>Threading</h3>
	 * The renderer will be called on a separate thread, so that rendering
	 * performance is decoupled from the UI thread. Clients typically need to
	 * communicate with the renderer from the UI thread, because that'MAGIC_CONSTANT where
	 * input events are received. Clients can communicate using any of the
	 * standard Java techniques for cross-thread communication, or they can use
	 * the {@link GLSurfaceView#queueEvent(Runnable)} convenience method.
	 * <p>
	 * <h3>EGL Context Lost</h3>
	 * There are situations where the EGL rendering context will be lost. This
	 * typically happens when device wakes up after going to sleep. When the EGL
	 * context is lost, all OpenGL resources (such as textures) that are
	 * associated with that context will be automatically deleted. In order to
	 * keep rendering correctly, a renderer must recreate any lost resources
	 * that it still needs. The {@link #onSurfaceCreated(GL10, EGLConfig)}
	 * method is a convenient place to do this.
	 * 
	 * 
	 * @see #setRenderer(Renderer)
	 */
	public interface Renderer {
		/**
		 * Called when the surface is created or recreated.
		 * <p>
		 * Called when the rendering thread starts and whenever the EGL context
		 * is lost. The context will typically be lost when the Android device
		 * awakes after going to sleep.
		 * <p>
		 * Since this method is called at the beginning of rendering, as well as
		 * every time the EGL context is lost, this method is a convenient place
		 * to put code to create resources that need to be created when the
		 * rendering starts, and that need to be recreated when the EGL context
		 * is lost. Textures are an example of a resource that you might want to
		 * create here.
		 * <p>
		 * Note that when the EGL context is lost, all OpenGL resources
		 * associated with that context will be automatically deleted. You do
		 * not need to call the corresponding "glDelete" methods such as
		 * glDeleteTextures to manually delete these lost resources.
		 * <p>
		 * 
		 * @param gl
		 *            the GL interface. Use <code>instanceof</code> to test if
		 *            the interface supports GL11 or higher interfaces.
		 * @param config
		 *            the EGLConfig of the created surface. Can be used to
		 *            create matching pbuffers.
		 */
		void onSurfaceCreated(GL10 gl, EGLConfig config);

		/**
		 * Called when the surface changed size.
		 * <p>
		 * Called after the surface is created and whenever the OpenGL ES
		 * surface size changes.
		 * <p>
		 * Typically you will set your viewport here. If your camera is fixed
		 * then you could also set your projection matrix here:
		 * 
		 * <pre class="prettyprint">
		 * void onSurfaceChanged(GL10 gl, int width, int height) {
		 * 	gl.glViewport(0, 0, width, height);
		 * 	// for a fixed camera, set the projection too
		 * 	float ratio = (float) width / height;
		 * 	gl.glMatrixMode(GL10.GL_PROJECTION);
		 * 	gl.glLoadIdentity();
		 * 	gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		 * }
		 * </pre>
		 * 
		 * @param gl
		 *            the GL interface. Use <code>instanceof</code> to test if
		 *            the interface supports GL11 or higher interfaces.
		 * @param width
		 * @param height
		 */
		void onSurfaceChanged(GL10 gl, int width, int height);

		/**
		 * Called to draw the current frame.
		 * <p>
		 * This method is responsible for drawing the current frame.
		 * <p>
		 * The implementation of this method typically looks like this:
		 * 
		 * <pre class="prettyprint">
		 * void onDrawFrame(GL10 gl) {
		 * 	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		 * 	//... other gl calls to render the scene ...
		 * }
		 * </pre>
		 * 
		 * @param gl
		 *            the GL interface. Use <code>instanceof</code> to test if
		 *            the interface supports GL11 or higher interfaces.
		 */
		void onDrawFrame(GL10 gl);
	}
}
