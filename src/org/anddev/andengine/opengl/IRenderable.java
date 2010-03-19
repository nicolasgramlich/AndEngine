package org.anddev.andengine.opengl;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Nicolas Gramlich
 * @since 13:25:58 - 08.03.2010
 */
public interface IRenderable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDraw(final GL10 pGL);
}
