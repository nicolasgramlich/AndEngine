package org.andengine.opengl.view;

import org.andengine.engine.Engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:57:29 - 08.03.2010
 */
public class RenderSurfaceView extends GLSurfaceView {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private EngineRenderer mEngineRenderer;
	private ConfigChooser mConfigChooser;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderSurfaceView(final Context pContext) {
		super(pContext);

		this.setEGLContextClientVersion(2);
	}

	public RenderSurfaceView(final Context pContext, final AttributeSet pAttrs) {
		super(pContext, pAttrs);

		this.setEGLContextClientVersion(2);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ConfigChooser getConfigChooser() throws IllegalStateException {
		if(this.mConfigChooser == null) {
			throw new IllegalStateException(ConfigChooser.class.getSimpleName() + " not yet set.");
		}
		return this.mConfigChooser;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure(final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
		this.mEngineRenderer.mEngine.getEngineOptions().getResolutionPolicy().onMeasure(this, pWidthMeasureSpec, pHeightMeasureSpec);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void setMeasuredDimensionProxy(final int pMeasuredWidth, final int pMeasuredHeight) {
		this.setMeasuredDimension(pMeasuredWidth, pMeasuredHeight);
	}

	public void setRenderer(final Engine pEngine, final IRendererListener pRendererListener) {
		if(this.mConfigChooser == null) {
			final boolean multiSampling = pEngine.getEngineOptions().getRenderOptions().isMultiSampling();
			final boolean argb8888 = pEngine.getEngineOptions().getRenderOptions().isARGB8888();
			this.mConfigChooser = new ConfigChooser(multiSampling, argb8888);
			
			//This isn't 100% correct: before mConfigChooser.chooseConfig() is called 
			//we don't know if RGBA_8888 was actually selected but we also need
			//a EGL10 and EGLDisplay to call that, which won't happen until after
			//setEGLConfigChooser and we need to call setFormat before that to 
			//have devices like the Droid 1 and 2 not crash.
			//Calling setFormate(RGBA_8888) when the GLConfig was RGB_565 hasn't
			//broken anything yet
			if(argb8888) getHolder().setFormat(android.graphics.PixelFormat.RGBA_8888);
		}
		
		this.setEGLConfigChooser(this.mConfigChooser);
		
		this.setOnTouchListener(pEngine);
		this.mEngineRenderer = new EngineRenderer(pEngine, this.mConfigChooser, pRendererListener);
		this.setRenderer(this.mEngineRenderer);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}