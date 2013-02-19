package org.andengine.opengl.view;

import org.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:57:29 - 08.03.2010
 */
public interface IRendererListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public void onSurfaceCreated(final GLState pGlState);
	public void onSurfaceChanged(final GLState pGlState, final int pWidth, final int pHeight);

	// ===========================================================
	// Methods
	// ===========================================================
}