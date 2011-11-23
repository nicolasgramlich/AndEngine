package org.andengine.opengl.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.opengl.GLES20;
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

	private Renderer mRenderer;
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

	public void setRenderer(final Engine pEngine, final IRendererListener pRendererListener) {
		if (pEngine.getEngineOptions().getRenderOptions().isMultiSampling()) {
			if(this.mConfigChooser == null) {
				this.mConfigChooser = new ConfigChooser(true);
			}
			this.setEGLConfigChooser(this.mConfigChooser);
		} else {
			this.setEGLConfigChooser(false);
		}

		this.setOnTouchListener(pEngine);
		this.mRenderer = new Renderer(pEngine, this.mConfigChooser, pRendererListener);
		this.setRenderer(this.mRenderer);
	}

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure(final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
		this.mRenderer.mEngine.getEngineOptions().getResolutionPolicy().onMeasure(this, pWidthMeasureSpec, pHeightMeasureSpec);
	}

	public void setMeasuredDimensionProxy(final int pMeasuredWidth, final int pMeasuredHeight) {
		this.setMeasuredDimension(pMeasuredWidth, pMeasuredHeight);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface IRendererListener {
		// ===========================================================
		// Constants
		// ===========================================================

		public void onSurfaceCreated();
		public void onSurfaceChanged(final int pWidth, final int pHeight);

		// ===========================================================
		// Methods
		// ===========================================================
	}

	public static class Renderer implements GLSurfaceView.Renderer {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final Engine mEngine;
		private final ConfigChooser mConfigChooser;
		private final boolean mMultiSampling;
		private final IRendererListener mRendererListener;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Renderer(final Engine pEngine, final ConfigChooser pConfigChooser, final IRendererListener pRendererListener) {
			this.mEngine = pEngine;
			this.mConfigChooser = pConfigChooser;
			this.mRendererListener = pRendererListener;
			this.mMultiSampling = this.mEngine.getEngineOptions().getRenderOptions().isMultiSampling();
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onSurfaceCreated(final GL10 pGL, final EGLConfig pEGLConfig) {
			if(this.mRendererListener != null) {
				this.mRendererListener.onSurfaceCreated();
			}
			GLState.reset(this.mEngine.getEngineOptions().getRenderOptions());

			// TODO Check if available and make available through EngineOptions-RenderOptions
//			GLES20.glEnable(GLES20.GL_POLYGON_SMOOTH);
//			GLES20.glHint(GLES20.GL_POLYGON_SMOOTH_HINT, GLES20.GL_NICEST);
//			GLES20.glEnable(GLES20.GL_LINE_SMOOTH);
//			GLES20.glHint(GLES20.GL_LINE_SMOOTH_HINT, GLES20.GL_NICEST);
//			GLES20.glEnable(GLES20.GL_POINT_SMOOTH);
//			GLES20.glHint(GLES20.GL_POINT_SMOOTH_HINT, GLES20.GL_NICEST);

			GLState.disableDither();
			GLState.disableDepthTest();

			GLState.enableBlend();

			/* Enabling culling doesn't really make sense, because triangles are never drawn 'backwards' on purpose. */
//			GLState.enableCulling();
//			GLES20.glFrontFace(GLES20.GL_CCW);
//			GLES20.glCullFace(GLES20.GL_BACK);
		}

		@Override
		public void onSurfaceChanged(final GL10 pGL, final int pWidth, final int pHeight) {
			if(this.mRendererListener != null) {
				this.mRendererListener.onSurfaceChanged(pWidth, pHeight);
			}
			this.mEngine.setSurfaceSize(pWidth, pHeight);
			GLES20.glViewport(0, 0, pWidth, pHeight);
			GLState.loadProjectionGLMatrixIdentity();
		}

		@Override
		public void onDrawFrame(final GL10 pGL) {
			if (this.mMultiSampling && this.mConfigChooser.isCoverageMultiSampling()) {
				final int GL_COVERAGE_BUFFER_BIT_NV = 0x8000;
				GLES20.glClear(GL_COVERAGE_BUFFER_BIT_NV);
			}

			try {
				this.mEngine.onDrawFrame();
			} catch (final InterruptedException e) {
				Debug.e("GLThread interrupted!", e);
			}
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}