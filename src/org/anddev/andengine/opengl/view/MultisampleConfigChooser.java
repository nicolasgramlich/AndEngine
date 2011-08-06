package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.opengl.GLSurfaceView;

/**
 * TODO Check with GLES1 version for DEPTH_SIZE...
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:31:48 - 04.08.2011
 */
public class MultisampleConfigChooser implements GLSurfaceView.EGLConfigChooser {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int EGL_OPENGL_ES2_BIT = 4;

	private static final int[] EGLCONFIG_MULTISAMPLE = {
		EGL10.EGL_RED_SIZE, 5,
		EGL10.EGL_GREEN_SIZE, 6,
		EGL10.EGL_BLUE_SIZE, 5,
		EGL10.EGL_DEPTH_SIZE, 16,
		EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
		EGL10.EGL_SAMPLE_BUFFERS, 1 /* true */,
		EGL10.EGL_SAMPLES, 2,
		EGL10.EGL_NONE
	};

	private static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
	private static final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;

	private static final int[] EGLCONFIG_MULTISAMPLE_NVIDIA = new int[]{
		EGL10.EGL_RED_SIZE, 5,
		EGL10.EGL_GREEN_SIZE, 6,
		EGL10.EGL_BLUE_SIZE, 5,
		EGL10.EGL_DEPTH_SIZE, 16,
		EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
		EGL_COVERAGE_BUFFERS_NV, 1,
		EGL_COVERAGE_SAMPLES_NV, 2,  // always 5 in practice on tegra 2
		EGL10.EGL_NONE
	};

	private static final int[] EGLCONFIG_FALLBACK = new int[] { 
		EGL10.EGL_RED_SIZE, 5, 
		EGL10.EGL_GREEN_SIZE, 6, 
		EGL10.EGL_BLUE_SIZE, 5, 
		EGL10.EGL_DEPTH_SIZE, 16, 
		EGL10.EGL_RENDERABLE_TYPE, 4 /*  */,
		EGL10.EGL_NONE
	};


	// ===========================================================
	// Fields
	// ===========================================================

	private int[] mBuffer = new int[1];
	private boolean mUsesCoverageAa;
	

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
	public EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay) {
		this.mBuffer[0] = 0;
		
		if(pEGL.eglChooseConfig(pEGLDisplay, EGLCONFIG_MULTISAMPLE, null, 0, this.mBuffer) == false) {
			throw new IllegalArgumentException("EGLCONFIG_MULTISAMPLE failed!");
		}
		int numConfigs = this.mBuffer[0];

		if(numConfigs <= 0) {
			if(pEGL.eglChooseConfig(pEGLDisplay, EGLCONFIG_MULTISAMPLE_NVIDIA, null, 0, this.mBuffer) == false) {
				throw new IllegalArgumentException("EGLCONFIG_MULTISAMPLE_NVIDIA failed!");
			}
			numConfigs = this.mBuffer[0];

			if(numConfigs <= 0) {
				if(pEGL.eglChooseConfig(pEGLDisplay, EGLCONFIG_FALLBACK, null, 0, this.mBuffer) == false) {
					throw new IllegalArgumentException("EGLCONFIG_FALLBACK failed!");
				}
				numConfigs = this.mBuffer[0];

				if(numConfigs <= 0) {
					throw new IllegalArgumentException("No EGLConfig found!");
				}
			} else {
				this.mUsesCoverageAa = true;
			}
		}

		// Get all matching configurations.
		final EGLConfig[] configs = new EGLConfig[numConfigs];
		if(!pEGL.eglChooseConfig(pEGLDisplay, EGLCONFIG_MULTISAMPLE, configs, numConfigs, this.mBuffer)) {
			throw new IllegalArgumentException("eglChooseConfig failed!");
		}

		return findEGLConfig(pEGL, pEGLDisplay, configs);
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs) {
		for(int i = 0; i < pEGLConfigs.length; ++i) {
			EGLConfig config = pEGLConfigs[i];
			if(this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_RED_SIZE, 0) == 5) {
				return config;
			}
		}
		throw new IllegalArgumentException("No EGLConfig found!");
	}
	

	// ===========================================================
	// Methods
	// ===========================================================

	private int getConfigAttrib(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig pEGLConfig, final int pAttribute, final int pDefaultValue) {
		if(pEGL.eglGetConfigAttrib(pEGLDisplay, pEGLConfig, pAttribute, this.mBuffer)) {
			return this.mBuffer[0];
		} else {
			return pDefaultValue;
		}
	}

	public boolean usesCoverageAa() {
		return this.mUsesCoverageAa;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}