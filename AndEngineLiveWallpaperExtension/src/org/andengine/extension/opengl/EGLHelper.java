package org.andengine.extension.opengl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

import org.andengine.util.exception.AndEngineRuntimeException;

import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.view.SurfaceHolder;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:01:07 - 10.11.2011
 */
public class EGLHelper {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

	private static final int[] EGL_ATTRIBUTES = {
		EGLHelper.EGL_CONTEXT_CLIENT_VERSION, 2,
		EGL10.EGL_NONE
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private EGL10 mEGL;
	private EGLDisplay mEGLDisplay;
	private EGLSurface mEGLSurface;
	private EGLContext mEGLContext;
	EGLConfig mEGLConfig;

	private final EGLConfigChooser mEGLConfigChooser;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EGLHelper(final EGLConfigChooser pEGLConfigChooser) {
		this.mEGLConfigChooser = pEGLConfigChooser;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void start() {
		/* Get an EGL instance. */
		this.mEGL = (EGL10) EGLContext.getEGL();

		/* Get to the default display. */
		this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		/* We can now initialize EGL for that display. */
		final int[] version = new int[2];
		if(!this.mEGL.eglInitialize(this.mEGLDisplay, version)) {
			throw new AndEngineRuntimeException(EGLHelper.class.getSimpleName() + ".eglInitialize failed." + " @(Thread: '" + Thread.currentThread().getName() + "')");
		}
		this.mEGLConfig = this.mEGLConfigChooser.chooseConfig(this.mEGL, this.mEGLDisplay);

		/* Create an OpenGL ES context. This must be done only once, an OpenGL context is a somewhat heavy object. */
		this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, this.mEGLConfig, EGL10.EGL_NO_CONTEXT, EGLHelper.EGL_ATTRIBUTES);
		if (this.mEGLContext == null || this.mEGLContext == EGL10.EGL_NO_CONTEXT) {
			throw new RuntimeException("createContext failed");
		}

		this.mEGLSurface = null;
	}

	/**
	 * React to the creation of a new surface by creating and returning an
	 * OpenGL interface that renders to that surface.
	 */
	public GL createSurface(final SurfaceHolder pSurfaceHolder) {
		/* The window size has changed, so we need to create a new surface. */
		if (this.mEGLSurface != null && this.mEGLSurface != EGL10.EGL_NO_SURFACE) {
			/* Unbind and destroy the old EGL surface, if there is one. */
			this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
			this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
		}

		/* Create an EGL surface we can render into. */
		this.mEGLSurface = this.mEGL.eglCreateWindowSurface(this.mEGLDisplay, this.mEGLConfig, pSurfaceHolder, null);

		if (this.mEGLSurface == null || this.mEGLSurface == EGL10.EGL_NO_SURFACE) {
			throw new RuntimeException("createSurface failed");
		}

		/* Before we can issue GL commands, we need to make sure the context is current and bound to a surface. */
		if (!this.mEGL.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
			throw new RuntimeException("eglMakeCurrent failed.");
		}

		return this.mEGLContext.getGL();
	}

	/**
	 * Display the current render surface.
	 * 
	 * @return false if the context has been lost.
	 */
	public boolean swapBuffers() {
		this.mEGL.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);

		/* Always check for EGL_CONTEXT_LOST, which means the context and all
		 * associated data were lost (For instance because the device went to sleep).
		 * We need to sleep until we get a new surface. */
		return this.mEGL.eglGetError() != EGL11.EGL_CONTEXT_LOST;
	}

	public void destroySurface() {
		if (this.mEGLSurface != null && this.mEGLSurface != EGL10.EGL_NO_SURFACE) {
			this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
			this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
			this.mEGLSurface = null;
		}
	}

	public void finish() {
		if (this.mEGLContext != null) {
			this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
			this.mEGLContext = null;
		}
		if (this.mEGLDisplay != null) {
			this.mEGL.eglTerminate(this.mEGLDisplay);
			this.mEGLDisplay = null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}