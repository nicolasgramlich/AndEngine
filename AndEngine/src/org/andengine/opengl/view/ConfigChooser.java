package org.andengine.opengl.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.opengl.GLSurfaceView;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:31:48 - 04.08.2011
 */
public class ConfigChooser implements GLSurfaceView.EGLConfigChooser {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] BUFFER = new int[1];

	private static final int RED_SIZE = 5;
	private static final int GREEN_SIZE = 6;
	private static final int BLUE_SIZE = 5;
	private static final int DEPTH_SIZE = 0;
	private static final int ALPHA_SIZE = 0;
	private static final int STENCIL_SIZE = 0;

	private static final int MULTISAMPLE_COUNT = 2; // TODO Could be made variable?

	private static final int EGL_GLES2_BIT = 4;

	private static final int[] EGLCONFIG_ATTRIBUTES_MULTISAMPLE = {
		EGL10.EGL_RED_SIZE, ConfigChooser.RED_SIZE,
		EGL10.EGL_GREEN_SIZE, ConfigChooser.GREEN_SIZE,
		EGL10.EGL_BLUE_SIZE, ConfigChooser.BLUE_SIZE,
		EGL10.EGL_ALPHA_SIZE, ConfigChooser.ALPHA_SIZE,
		EGL10.EGL_DEPTH_SIZE, ConfigChooser.DEPTH_SIZE,
		EGL10.EGL_STENCIL_SIZE, ConfigChooser.STENCIL_SIZE,
		EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
		EGL10.EGL_SAMPLE_BUFFERS, 1,
		EGL10.EGL_SAMPLES, ConfigChooser.MULTISAMPLE_COUNT,
		EGL10.EGL_NONE
	};

	private static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
	private static final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;

	private static final int[] EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA = new int[]{
		EGL10.EGL_RED_SIZE, ConfigChooser.RED_SIZE,
		EGL10.EGL_GREEN_SIZE, ConfigChooser.GREEN_SIZE,
		EGL10.EGL_BLUE_SIZE, ConfigChooser.BLUE_SIZE,
		EGL10.EGL_ALPHA_SIZE, ConfigChooser.ALPHA_SIZE,
		EGL10.EGL_DEPTH_SIZE, ConfigChooser.DEPTH_SIZE,
		EGL10.EGL_STENCIL_SIZE, ConfigChooser.STENCIL_SIZE,
		EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
		ConfigChooser.EGL_COVERAGE_BUFFERS_NV, 1,
		ConfigChooser.EGL_COVERAGE_SAMPLES_NV, ConfigChooser.MULTISAMPLE_COUNT,  // always 5 in practice on tegra 2
		EGL10.EGL_NONE
	};

	private static final int[] EGLCONFIG_ATTRIBUTES_FALLBACK = new int[] {
		EGL10.EGL_RED_SIZE, ConfigChooser.RED_SIZE,
		EGL10.EGL_GREEN_SIZE, ConfigChooser.GREEN_SIZE,
		EGL10.EGL_BLUE_SIZE, ConfigChooser.BLUE_SIZE,
		EGL10.EGL_ALPHA_SIZE, ConfigChooser.ALPHA_SIZE,
		EGL10.EGL_DEPTH_SIZE, ConfigChooser.DEPTH_SIZE,
		EGL10.EGL_STENCIL_SIZE, ConfigChooser.STENCIL_SIZE,
		EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
		EGL10.EGL_NONE
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final boolean mMultiSamplingRequested;

	private boolean mMultiSampling;
	private boolean mCoverageMultiSampling;

	private int mRedSize = -1;
	private int mGreenSize = -1;
	private int mBlueSize = -1;
	private int mAlphaSize = -1;
	private int mDepthSize = -1;
	private int mStencilSize = -1;

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

	public int getRedSize() {
		return this.mRedSize;
	}

	public int getGreenSize() {
		return this.mGreenSize;
	}

	public int getBlueSize() {
		return this.mBlueSize;
	}

	public int getAlphaSize() {
		return this.mAlphaSize;
	}

	public int getDepthSize() {
		return this.mDepthSize;
	}

	public int getStencilSize() {
		return this.mStencilSize;
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

		if(this.mMultiSamplingRequested) {
			eglConfigCount = ConfigChooser.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_MULTISAMPLE);
			if(eglConfigCount > 0) {
				this.mMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_MULTISAMPLE, eglConfigCount, pConfigChooserMatcher);
			}

			eglConfigCount = ConfigChooser.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA);
			if(eglConfigCount > 0) {
				this.mCoverageMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA, eglConfigCount, pConfigChooserMatcher);
			}
		}

		eglConfigCount = ConfigChooser.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_FALLBACK);
		if(eglConfigCount > 0) {
			return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_FALLBACK, eglConfigCount, pConfigChooserMatcher);
		} else {
			throw new IllegalArgumentException("No " + EGLConfig.class.getSimpleName() + " found!");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static int getEGLConfigCount(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes) {
		if(pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, null, 0, ConfigChooser.BUFFER) == false) {
			throw new IllegalArgumentException("EGLCONFIG_FALLBACK failed!");
		}
		return ConfigChooser.BUFFER[0];
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes, final int pEGLConfigCount, final ConfigChooserMatcher pConfigChooserMatcher) {
		final EGLConfig[] eglConfigs = new EGLConfig[pEGLConfigCount];
		if(!pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, eglConfigs, pEGLConfigCount, ConfigChooser.BUFFER)) {
			throw new IllegalArgumentException("findEGLConfig failed!");
		}

		return this.findEGLConfig(pEGL, pEGLDisplay, eglConfigs, pConfigChooserMatcher);
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs, final ConfigChooserMatcher pConfigChooserMatcher) {
		for(int i = 0; i < pEGLConfigs.length; ++i) {
			final EGLConfig config = pEGLConfigs[i];
			if(config != null) {
				final int redSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_RED_SIZE, 0);
				final int greenSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_GREEN_SIZE, 0);
				final int blueSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_BLUE_SIZE, 0);
				final int alphaSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_ALPHA_SIZE, 0);
				final int depthSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_DEPTH_SIZE, 0);
				final int stencilSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_STENCIL_SIZE, 0);

				if(pConfigChooserMatcher.matches(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize)) {
					this.mRedSize = redSize;
					this.mGreenSize = greenSize;
					this.mBlueSize = blueSize;
					this.mAlphaSize = alphaSize;
					this.mDepthSize = depthSize;
					this.mStencilSize = stencilSize;
					return config;
				}
			}
		}
		throw new IllegalArgumentException("No EGLConfig found!");
	}

	private static int getConfigAttrib(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig pEGLConfig, final int pAttribute, final int pDefaultValue) {
		if(pEGL.eglGetConfigAttrib(pEGLDisplay, pEGLConfig, pAttribute, ConfigChooser.BUFFER)) {
			return ConfigChooser.BUFFER[0];
		} else {
			return pDefaultValue;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public enum ConfigChooserMatcher {
		// ===========================================================
		// Elements
		// ===========================================================

		STRICT() {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				if(pDepthSize == ConfigChooser.DEPTH_SIZE && pStencilSize == ConfigChooser.STENCIL_SIZE) {
					if(pRedSize == ConfigChooser.RED_SIZE && pGreenSize == ConfigChooser.GREEN_SIZE && pBlueSize == ConfigChooser.BLUE_SIZE && pAlphaSize == ConfigChooser.ALPHA_SIZE) {
						return true;
					}
				}
				return false;
			}
		},
		LOOSE_STENCIL() {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				if(pDepthSize == ConfigChooser.DEPTH_SIZE && pStencilSize >= ConfigChooser.STENCIL_SIZE) {
					if(pRedSize == ConfigChooser.RED_SIZE && pGreenSize == ConfigChooser.GREEN_SIZE && pBlueSize == ConfigChooser.BLUE_SIZE && pAlphaSize == ConfigChooser.ALPHA_SIZE) {
						return true;
					}
				}
				return false;
			}
		},
		LOOSE_DEPTH_AND_STENCIL() {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				if(pDepthSize >= ConfigChooser.DEPTH_SIZE && pStencilSize >= ConfigChooser.STENCIL_SIZE) {
					if(pRedSize == ConfigChooser.RED_SIZE && pGreenSize == ConfigChooser.GREEN_SIZE && pBlueSize == ConfigChooser.BLUE_SIZE && pAlphaSize == ConfigChooser.ALPHA_SIZE) {
						return true;
					}
				}
				return false;
			}
		},
		ANY() {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				return true;
			}
		};

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

		public abstract boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize);

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}