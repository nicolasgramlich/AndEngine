package org.andengine.engine.options.resolutionpolicy;

import org.andengine.engine.options.EngineOptions;
import org.andengine.opengl.view.RenderSurfaceView;

/**
 * An implementation of the IResolutionPolicy interface is part of the {@link EngineOptions}. It tells
 * AndEngine how to deal with the different screen-sizes of different devices. <br>
 * (c) 2010 Nicolas Gramlich <br>
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
