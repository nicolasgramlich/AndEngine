package org.andengine.opengl.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import org.andengine.engine.options.ConfigChooserOptions;

import android.opengl.GLSurfaceView;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:31:48 - 04.08.2011
 */
public class ConfigChooser implements GLSurfaceView.EGLConfigChooser {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] BUFFER = new int[1];

	private static final int MULTISAMPLE_COUNT = 2; // TODO Could be made variable?

	private static final int EGL_GLES2_BIT = 4;

	private static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
	private static final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;

	private final int[] mMultiSampleEGLConfig;

	private final int[] mNvidiaCoverageMultiSampleEGLConfig;

	private final int[] mDefaultEGLConfig;

	// ===========================================================
	// Fields
	// ===========================================================

	private final boolean mRequestedMultiSampling;

	private boolean mActualMultiSampling;
	private boolean mActualCoverageMultiSampling;

	private final int mRequestedRedSize;
	private final int mRequestedGreenSize;
	private final int mRequestedBlueSize;
	private final int mRequestedAlphaSize;
	private final int mRequestedDepthSize;
	private final int mRequestedStencilSize;

	private int mActualRedSize = -1;
	private int mActualGreenSize = -1;
	private int mActualBlueSize = -1;
	private int mActualAlphaSize = -1;
	private int mActualDepthSize = -1;
	private int mActualStencilSize = -1;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConfigChooser(final ConfigChooserOptions pConfigChooserOptions) {
		this(pConfigChooserOptions.getRequestedRedSize(), pConfigChooserOptions.getRequestedGreenSize(), pConfigChooserOptions.getRequestedBlueSize(), pConfigChooserOptions.getRequestedAlphaSize(), pConfigChooserOptions.getRequestedDepthSize(), pConfigChooserOptions.getRequestedStencilSize(), pConfigChooserOptions.isRequestedMultiSampling());
	}

	public ConfigChooser(final int pRequestedRedSize, final int pRequestedGreenSize, final int pRequestedBlueSize, final int pRequestedAlphaSize, final int pRequestedDepthSize, final int pRequestedStencilSize, final boolean pRequestedMultiSampling) {
		this.mRequestedRedSize = pRequestedRedSize;
		this.mRequestedGreenSize = pRequestedGreenSize;
		this.mRequestedBlueSize = pRequestedBlueSize;
		this.mRequestedAlphaSize = pRequestedAlphaSize;
		this.mRequestedDepthSize = pRequestedDepthSize;
		this.mRequestedStencilSize = pRequestedStencilSize;
		this.mRequestedMultiSampling = pRequestedMultiSampling;

		this.mMultiSampleEGLConfig = new int[] {
			EGL10.EGL_RED_SIZE, pRequestedRedSize,
			EGL10.EGL_GREEN_SIZE, pRequestedGreenSize,
			EGL10.EGL_BLUE_SIZE, pRequestedBlueSize,
			EGL10.EGL_ALPHA_SIZE, pRequestedAlphaSize,
			EGL10.EGL_DEPTH_SIZE, pRequestedDepthSize,
			EGL10.EGL_STENCIL_SIZE, pRequestedStencilSize,
			EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
			EGL10.EGL_SAMPLE_BUFFERS, 1,
			EGL10.EGL_SAMPLES, ConfigChooser.MULTISAMPLE_COUNT,
			EGL10.EGL_NONE
		};

		this.mNvidiaCoverageMultiSampleEGLConfig = new int[] {
			EGL10.EGL_RED_SIZE, pRequestedRedSize,
			EGL10.EGL_GREEN_SIZE, pRequestedGreenSize,
			EGL10.EGL_BLUE_SIZE, pRequestedBlueSize,
			EGL10.EGL_ALPHA_SIZE, pRequestedAlphaSize,
			EGL10.EGL_DEPTH_SIZE, pRequestedDepthSize,
			EGL10.EGL_STENCIL_SIZE, pRequestedStencilSize,
			EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
			ConfigChooser.EGL_COVERAGE_BUFFERS_NV, 1,
			ConfigChooser.EGL_COVERAGE_SAMPLES_NV, ConfigChooser.MULTISAMPLE_COUNT,
			EGL10.EGL_NONE
		};

		this.mDefaultEGLConfig = new int[] {
			EGL10.EGL_RED_SIZE, pRequestedRedSize,
			EGL10.EGL_GREEN_SIZE, pRequestedGreenSize,
			EGL10.EGL_BLUE_SIZE, pRequestedBlueSize,
			EGL10.EGL_ALPHA_SIZE, pRequestedAlphaSize,
			EGL10.EGL_DEPTH_SIZE, pRequestedDepthSize,
			EGL10.EGL_STENCIL_SIZE, pRequestedStencilSize,
			EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
			EGL10.EGL_NONE
		};
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isRequestedMultiSampling() {
		return this.mRequestedMultiSampling;
	}

	public boolean isActualMultiSampling() {
		return this.mActualMultiSampling;
	}

	public boolean isActualCoverageMultiSampling() {
		return this.mActualCoverageMultiSampling;
	}

	public int getRequestedRedSize() {
		return this.mRequestedRedSize;
	}

	public int getActualRedSize() {
		return this.mActualRedSize;
	}

	public int getRequestedGreenSize() {
		return this.mRequestedGreenSize;
	}

	public int getActualGreenSize() {
		return this.mActualGreenSize;
	}

	public int getRequestedSize() {
		return this.mRequestedBlueSize;
	}

	public int getActualBlueSize() {
		return this.mActualBlueSize;
	}

	public int getRequestedAlphaSize() {
		return this.mRequestedAlphaSize;
	}

	public int getActualAlphaSize() {
		return this.mActualAlphaSize;
	}

	public int getRequestedDepthSize() {
		return this.mRequestedDepthSize;
	}

	public int getActualDepthSize() {
		return this.mActualDepthSize;
	}

	public int getRequestedStencilSize() {
		return this.mRequestedStencilSize;
	}

	public int getActualStencilSize() {
		return this.mActualStencilSize;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay) {
		try {
			return this.chooseConfig(pEGL, pEGLDisplay, ConfigChooserMatcher.STRICT);
		} catch (final IllegalArgumentException e) {

		}

		try {
			return this.chooseConfig(pEGL, pEGLDisplay, ConfigChooserMatcher.LOOSE_STENCIL);
		} catch (final IllegalArgumentException e) {

		}

		try {
			return this.chooseConfig(pEGL, pEGLDisplay, ConfigChooserMatcher.LOOSE_DEPTH_AND_STENCIL);
		} catch (final IllegalArgumentException e) {

		}

		return this.chooseConfig(pEGL, pEGLDisplay, ConfigChooserMatcher.ANY);
	}

	private EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final ConfigChooserMatcher pConfigChooserMatcher) throws IllegalArgumentException {
		ConfigChooser.BUFFER[0] = 0;

		int eglConfigCount;

		if (this.mRequestedMultiSampling) {
			eglConfigCount = this.getEGLConfigCount(pEGL, pEGLDisplay, this.mMultiSampleEGLConfig);
			if (eglConfigCount > 0) {
				this.mActualMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, this.mMultiSampleEGLConfig, eglConfigCount, pConfigChooserMatcher);
			}

			eglConfigCount = this.getEGLConfigCount(pEGL, pEGLDisplay, this.mNvidiaCoverageMultiSampleEGLConfig);
			if (eglConfigCount > 0) {
				this.mActualCoverageMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, this.mNvidiaCoverageMultiSampleEGLConfig, eglConfigCount, pConfigChooserMatcher);
			}
		}

		eglConfigCount = this.getEGLConfigCount(pEGL, pEGLDisplay, this.mDefaultEGLConfig);
		if (eglConfigCount > 0) {
			return this.findEGLConfig(pEGL, pEGLDisplay, this.mDefaultEGLConfig, eglConfigCount, pConfigChooserMatcher);
		} else {
			throw new IllegalArgumentException("No " + EGLConfig.class.getSimpleName() + " found!");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int getEGLConfigCount(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes) {
		if (pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, null, 0, ConfigChooser.BUFFER) == false) {
			throw new IllegalArgumentException("EGLCONFIG_FALLBACK failed!");
		}
		return ConfigChooser.BUFFER[0];
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes, final int pEGLConfigCount, final ConfigChooserMatcher pConfigChooserMatcher) {
		final EGLConfig[] eglConfigs = new EGLConfig[pEGLConfigCount];
		if (!pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, eglConfigs, pEGLConfigCount, ConfigChooser.BUFFER)) {
			throw new IllegalArgumentException("findEGLConfig failed!");
		}

		return this.findEGLConfig(pEGL, pEGLDisplay, eglConfigs, pConfigChooserMatcher);
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs, final ConfigChooserMatcher pConfigChooserMatcher) {
		for (int i = 0; i < pEGLConfigs.length; ++i) {
			final EGLConfig config = pEGLConfigs[i];
			if (config != null) {
				final int redSize = this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_RED_SIZE, 0);
				final int greenSize = this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_GREEN_SIZE, 0);
				final int blueSize = this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_BLUE_SIZE, 0);
				final int alphaSize = this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_ALPHA_SIZE, 0);
				final int depthSize = this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_DEPTH_SIZE, 0);
				final int stencilSize = this.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_STENCIL_SIZE, 0);

				if (pConfigChooserMatcher.matches(this.mRequestedRedSize, redSize, this.mRequestedGreenSize, greenSize, this.mRequestedBlueSize, blueSize, this.mRequestedAlphaSize, alphaSize, this.mRequestedDepthSize, depthSize, this.mRequestedStencilSize, stencilSize)) {
					this.mActualRedSize = redSize;
					this.mActualGreenSize = greenSize;
					this.mActualBlueSize = blueSize;
					this.mActualAlphaSize = alphaSize;
					this.mActualDepthSize = depthSize;
					this.mActualStencilSize = stencilSize;
					return config;
				}
			}
		}
		throw new IllegalArgumentException("No EGLConfig found!");
	}

	private int getConfigAttrib(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig pEGLConfig, final int pAttribute, final int pDefaultValue) {
		if (pEGL.eglGetConfigAttrib(pEGLDisplay, pEGLConfig, pAttribute, ConfigChooser.BUFFER)) {
			return ConfigChooser.BUFFER[0];
		} else {
			return pDefaultValue;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}