package org.andengine.opengl.view;

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
public class ConfigChooser implements GLSurfaceView.EGLConfigChooser {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MULTISAMPLE_COUNT = 2; // TODO Could be made variable?

	private static final int[] BUFFER = new int[1];

	private static final int EGL_GLES2_BIT = 4;

	private static final int[] EGLCONFIG_ATTRIBUTES_MULTISAMPLE = {
		EGL10.EGL_RED_SIZE, 5,
		EGL10.EGL_GREEN_SIZE, 6,
		EGL10.EGL_BLUE_SIZE, 5,
		EGL10.EGL_DEPTH_SIZE, 0,
		EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
		EGL10.EGL_SAMPLE_BUFFERS, 1,
		EGL10.EGL_SAMPLES, ConfigChooser.MULTISAMPLE_COUNT,
		EGL10.EGL_NONE
	};

	private static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
	private static final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;

	private static final int[] EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA = new int[]{
		EGL10.EGL_RED_SIZE, 5,
		EGL10.EGL_GREEN_SIZE, 6,
		EGL10.EGL_BLUE_SIZE, 5,
		EGL10.EGL_DEPTH_SIZE, 0,
		EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
		ConfigChooser.EGL_COVERAGE_BUFFERS_NV, 1,
		ConfigChooser.EGL_COVERAGE_SAMPLES_NV, ConfigChooser.MULTISAMPLE_COUNT,  // always 5 in practice on tegra 2
		EGL10.EGL_NONE
	};

	private static final int[] EGLCONFIG_ATTRIBUTES_FALLBACK = new int[] {
		EGL10.EGL_RED_SIZE, 5,
		EGL10.EGL_GREEN_SIZE, 6,
		EGL10.EGL_BLUE_SIZE, 5,
		EGL10.EGL_DEPTH_SIZE, 0,
		EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
		EGL10.EGL_NONE
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final boolean mMultiSamplingRequested;

	private boolean mMultiSampling;
	private boolean mCoverageMultiSampling;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConfigChooser(final boolean pMultiSamplingRequested) {
		this.mMultiSamplingRequested = pMultiSamplingRequested;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isMultiSampling() {
		return this.mMultiSampling;
	}

	public boolean isCoverageMultiSampling() {
		return this.mCoverageMultiSampling;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay) {
		ConfigChooser.BUFFER[0] = 0;

		int eglConfigCount;

		if(this.mMultiSamplingRequested) {
			eglConfigCount = this.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_MULTISAMPLE);
			if(eglConfigCount > 0) {
				this.mMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_MULTISAMPLE, eglConfigCount);
			}

			eglConfigCount = this.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA);
			if(eglConfigCount > 0) {
				this.mCoverageMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA, eglConfigCount);
			}
		}

		eglConfigCount = this.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_FALLBACK);
		if(eglConfigCount > 0) {
			return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_FALLBACK, eglConfigCount);
		} else {
			throw new IllegalArgumentException("No EGLConfig found!");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int getEGLConfigCount(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes) {
		if(pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, null, 0, ConfigChooser.BUFFER) == false) {
			throw new IllegalArgumentException("EGLCONFIG_FALLBACK failed!");
		}
		return ConfigChooser.BUFFER[0];
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes, final int pEGLConfigCount) {
		final EGLConfig[] eglConfigs = new EGLConfig[pEGLConfigCount];
		if(!pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, eglConfigs, pEGLConfigCount, ConfigChooser.BUFFER)) {
			throw new IllegalArgumentException("findEGLConfig failed!");
		}

		return this.findEGLConfig(pEGL, pEGLDisplay, eglConfigs);
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs) {
		for(int i = 0; i < pEGLConfigs.length; ++i) {
			final EGLConfig config = pEGLConfigs[i];
			if(config != null && this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_RED_SIZE, 0) == 5) {
				return config;
			}
		}
		throw new IllegalArgumentException("No EGLConfig found!");
	}

	private int getConfigAttrib(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig pEGLConfig, final int pAttribute, final int pDefaultValue) {
		if(pEGL.eglGetConfigAttrib(pEGLDisplay, pEGLConfig, pAttribute, ConfigChooser.BUFFER)) {
			return ConfigChooser.BUFFER[0];
		} else {
			return pDefaultValue;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}