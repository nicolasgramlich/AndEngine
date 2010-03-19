package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.GLHelper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

/**
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

	private final Renderer mRenderer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderSurfaceView(final Context pContext, final Engine pEngine) {
		super(pContext);
		this.mRenderer = new Renderer(pEngine);
		this.setRenderer(this.mRenderer);
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
	 * @author Nicolas Gramlich
	 * @since 11:45:59 - 08.03.2010
	 */
	public static class Renderer implements android.opengl.GLSurfaceView.Renderer {
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
			pGL.glViewport(0, 0, pWidth, pHeight);
			pGL.glMatrixMode(GL10.GL_PROJECTION);
			pGL.glLoadIdentity();

			pGL.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			pGL.glShadeModel(GL10.GL_FLAT);
			pGL.glDisable(GL10.GL_DITHER);
			pGL.glDisable(GL10.GL_MULTISAMPLE);

			pGL.glEnable(GL10.GL_BLEND);

			//			GLHelper.blendMode(pGL, GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

			GLHelper.enableTextures(pGL);
			GLHelper.enableTexCoordArray(pGL);
			GLHelper.enableVertexArray(pGL);

			this.setOrthographicProjection(pGL);
		}

		@Override
		public void onSurfaceCreated(final GL10 pGL, final EGLConfig pConfig) {
			pGL.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			pGL.glClearColor(0, 0, 0, 1);
			pGL.glDisable(GL10.GL_LIGHTING);
			pGL.glDisable(GL10.GL_DITHER);
			pGL.glDisable(GL10.GL_DEPTH_TEST);
			pGL.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			GLHelper.enableTextures(pGL);
			GLHelper.enableTexCoordArray(pGL);
			GLHelper.enableVertexArray(pGL);

			this.setOrthographicProjection(pGL);
		}

		@Override
		public void onDrawFrame(final GL10 pGL) {
			pGL.glMatrixMode(GL10.GL_MODELVIEW);
			pGL.glLoadIdentity();
			this.mEngine.onDrawFrame(pGL);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		private void setOrthographicProjection(final GL10 pGL) {
			GLU.gluOrtho2D(pGL, 0, this.mEngine.getGameWidth(), this.mEngine.getGameHeight(), 0);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

}
