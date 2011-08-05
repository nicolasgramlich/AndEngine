package org.anddev.andengine.opengl;

import org.anddev.andengine.engine.camera.Camera;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public void onDraw(final Camera pCamera);
}
