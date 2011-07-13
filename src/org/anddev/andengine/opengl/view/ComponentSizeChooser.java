package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:54:06 - 28.06.2010
 */
public class ComponentSizeChooser extends BaseConfigChooser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int[] mValue;
	// Subclasses can adjust these values:
	protected int mRedSize;
	protected int mGreenSize;
	protected int mBlueSize;
	protected int mAlphaSize;
	protected int mDepthSize;
	protected int mStencilSize;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ComponentSizeChooser(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
		super(new int[] { EGL10.EGL_RED_SIZE, pRedSize, EGL10.EGL_GREEN_SIZE, pGreenSize, EGL10.EGL_BLUE_SIZE, pBlueSize, EGL10.EGL_ALPHA_SIZE, pAlphaSize, EGL10.EGL_DEPTH_SIZE, pDepthSize, EGL10.EGL_STENCIL_SIZE, pStencilSize, EGL10.EGL_NONE });
		this.mValue = new int[1];
		this.mRedSize = pRedSize;
		this.mGreenSize = pGreenSize;
		this.mBlueSize = pBlueSize;
		this.mAlphaSize = pAlphaSize;
		this.mDepthSize = pDepthSize;
		this.mStencilSize = pStencilSize;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs) {
		EGLConfig closestConfig = null;
		int closestDistance = 1000;
		for(final EGLConfig config : pEGLConfigs) {
			final int r = this.findConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_RED_SIZE, 0);
			final int g = this.findConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_GREEN_SIZE, 0);
			final int b = this.findConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_BLUE_SIZE, 0);
			final int a = this.findConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_ALPHA_SIZE, 0);
			final int d = this.findConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_DEPTH_SIZE, 0);
			final int s = this.findConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_STENCIL_SIZE, 0);
			final int distance = Math.abs(r - this.mRedSize) + Math.abs(g - this.mGreenSize) + Math.abs(b - this.mBlueSize) + Math.abs(a - this.mAlphaSize) + Math.abs(d - this.mDepthSize) + Math.abs(s - this.mStencilSize);
			if(distance < closestDistance) {
				closestDistance = distance;
				closestConfig = config;
			}
		}
		return closestConfig;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int findConfigAttrib(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig pEGLConfig, final int pAttribute, final int pDefaultValue) {
		if(pEGL.eglGetConfigAttrib(pEGLDisplay, pEGLConfig, pAttribute, this.mValue)) {
			return this.mValue[0];
		}
		return pDefaultValue;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}