package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;

import android.content.Context;
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

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderSurfaceView(final Context pContext) {
		super(pContext);
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
	public static class Renderer implements GLSurfaceView.Renderer {
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
			pGL.glViewport(0, 0, pWidth, pHeight);
			pGL.glLoadIdentity();
		}

		@Override
		public void onSurfaceCreated(final GL10 pGL, final EGLConfig pConfig) {
			Debug.d("onSurfaceCreated");
			GLHelper.reset(pGL);

			GLHelper.setPerspectiveCorrectionHintFastest(pGL);
			//			pGL.glEnable(GL10.GL_POLYGON_SMOOTH);
			//			pGL.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
			//			pGL.glEnable(GL10.GL_LINE_SMOOTH);
			//			pGL.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
			//			pGL.glEnable(GL10.GL_POINT_SMOOTH);
			//			pGL.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);

			GLHelper.setShadeModelFlat(pGL);

			GLHelper.disableLightning(pGL);
			GLHelper.disableDither(pGL);
			GLHelper.disableDepthTest(pGL);
			GLHelper.disableMultisample(pGL);

			GLHelper.enableBlend(pGL);
			GLHelper.enableTextures(pGL);
			GLHelper.enableTexCoordArray(pGL);
			GLHelper.enableVertexArray(pGL);

			GLHelper.enableCulling(pGL);
			pGL.glFrontFace(GL10.GL_CCW);
			pGL.glCullFace(GL10.GL_BACK);

			GLHelper.enableExtensions(pGL, this.mEngine.getEngineOptions().getRenderOptions());
		}

		@Override
		public void onDrawFrame(final GL10 pGL) {
			try {
				this.mEngine.onDrawFrame(pGL);
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
