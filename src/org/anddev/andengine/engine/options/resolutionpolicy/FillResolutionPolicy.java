package org.anddev.andengine.engine.options.resolutionpolicy;

import org.anddev.andengine.opengl.view.RenderSurfaceView;

import android.view.View.MeasureSpec;

/**
 * @author Nicolas Gramlich
 * @since 11:22:48 - 29.03.2010
 */
public class FillResolutionPolicy implements IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onMeasure(final RenderSurfaceView pRenderSurfaceView, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
		final int measuredWidth = MeasureSpec.getSize(pWidthMeasureSpec);
		final int measuredHeight = MeasureSpec.getSize(pHeightMeasureSpec);

		pRenderSurfaceView.setMeasuredDimensionProxy(measuredWidth, measuredHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
