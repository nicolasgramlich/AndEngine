package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;

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
	private MultisampleConfigChooser mMultisampleConfigChooser;
	private boolean mUseMultisampling;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderSurfaceView(final Context pContext) {
		super(pContext);

		this.setEGLContextClientVersion(2);
		if (this.mUseMultisampling) {
			this.setEGLConfigChooser(this.mMultisampleConfigChooser = new MultisampleConfigChooser());
		}
	}

	public RenderSurfaceView(final Context pContext, final AttributeSet pAttrs) {
		super(pContext, pAttrs);
	}

	public void setRenderer(final Engine pEngine) {
		this.setOnTouchListener(pEngine);
		this.mRenderer = new Renderer(pEngine);
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

	/**
	 * (c) 2010 Nicolas Gramlich
	 * (c) 2011 Zynga Inc.
	 * 
	 * @author Nicolas Gramlich
	 * @since 11:45:59 - 08.03.2010
	 */
	public class Renderer implements GLSurfaceView.Renderer {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final Engine mEngine;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Renderer(final Engine pEngine) {
			this.mEngine = pEngine;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onSurfaceChanged(final GL10 pGL, final int pWidth, final int pHeight) {
			Debug.d("onSurfaceChanged: pWidth=" + pWidth + "  pHeight=" + pHeight);
			this.mEngine.setSurfaceSize(pWidth, pHeight);
			GLES20.glViewport(0, 0, pWidth, pHeight);
			GLHelper.glLoadIdentity();
		}

		@Override
		public void onSurfaceCreated(final GL10 pGL, final EGLConfig pConfig) {
			Debug.d("onSurfaceCreated");
			GLHelper.reset();

			GLHelper.setPerspectiveCorrectionHintFastest();
			//			pGL.glEnable(GL10.GL_POLYGON_SMOOTH);
			//			pGL.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
			//			pGL.glEnable(GL10.GL_LINE_SMOOTH);
			//			pGL.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
			//			pGL.glEnable(GL10.GL_POINT_SMOOTH);
			//			pGL.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);

			GLHelper.disableLightning();
			GLHelper.disableDither();
			GLHelper.disableDepthTest();
			GLHelper.disableMultisample();

			GLHelper.enableBlend();
			GLHelper.enableTextures();
			GLHelper.enableTexCoordArray();
			GLHelper.enableVertexArray();

			GLHelper.enableCulling();
			GLES20.glFrontFace(GL10.GL_CCW);
			GLES20.glCullFace(GL10.GL_BACK);

			GLHelper.enableExtensions(this.mEngine.getEngineOptions().getRenderOptions());
		}

		@Override
		public void onDrawFrame(final GL10 pGL) {
			int clearMask = GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT;
			if (RenderSurfaceView.this.mUseMultisampling && RenderSurfaceView.this.mMultisampleConfigChooser.usesCoverageAa()) {
				final int GL_COVERAGE_BUFFER_BIT_NV = 0x8000;
				clearMask |= GL_COVERAGE_BUFFER_BIT_NV;
			}
			GLES20.glClear(clearMask);

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
