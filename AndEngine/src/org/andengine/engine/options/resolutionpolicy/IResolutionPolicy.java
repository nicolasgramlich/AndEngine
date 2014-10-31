package org.andengine.engine.options.resolutionpolicy;

import org.andengine.opengl.view.RenderSurfaceView;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:02:35 - 29.03.2010
 */
public interface IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onMeasure(final RenderSurfaceView pRenderSurfaceView, final int pWidthMeasureSpec, final int pHeightMeasureSpec);
}
