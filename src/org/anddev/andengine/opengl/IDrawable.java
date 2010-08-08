package org.anddev.andengine.opengl;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;


/**
 * @author Nicolas Gramlich
 * @since 10:50:58 - 08.08.2010
 */
public interface IDrawable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDraw(final GL10 pGL, final Camera pCamera);
}
