/**
 * 
 */
package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:42:29 - 28.06.2010
 */
abstract class BaseConfigChooser implements EGLConfigChooser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int[] mConfigSpec;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseConfigChooser(final int[] pConfigSpec) {
		this.mConfigSpec = pConfigSpec;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	abstract EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs);

	@Override
	public EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay) {
		final int[] num_config = new int[1];
		pEGL.eglChooseConfig(pEGLDisplay, this.mConfigSpec, null, 0, num_config);

		final int numConfigs = num_config[0];

		if(numConfigs <= 0) {
			throw new IllegalArgumentException("No configs match configSpec");
		}

		final EGLConfig[] configs = new EGLConfig[numConfigs];
		pEGL.eglChooseConfig(pEGLDisplay, this.mConfigSpec, configs, numConfigs, num_config);
		final EGLConfig config = this.chooseConfig(pEGL, pEGLDisplay, configs);
		if(config == null) {
			throw new IllegalArgumentException("No config chosen");
		}
		return config;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}