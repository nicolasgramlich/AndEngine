package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * An interface for choosing an EGLConfig configuration from a list of
 * potential configurations.
 * <p>
 * This interface must be implemented by clients wishing to call
 * {@link GLSurfaceView#setEGLConfigChooser(EGLConfigChooser)}
 *
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:53:49 - 28.06.2010
 */
public interface EGLConfigChooser {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Choose a configuration from the list. Implementors typically
	 * implement this method by calling {@link EGL10#eglChooseConfig} and
	 * iterating through the results. Please consult the EGL specification
	 * available from The Khronos Group to learn how to call
	 * eglChooseConfig.
	 * 
	 * @param pEGL the EGL10 for the current display.
	 * @param pEGLDisplay the current display.
	 * @return the chosen configuration.
	 */
	public EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay);
}