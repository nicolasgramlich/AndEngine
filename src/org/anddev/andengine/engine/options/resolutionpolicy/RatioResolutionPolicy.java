package org.anddev.andengine.engine.options.resolutionpolicy;

import org.anddev.andengine.opengl.view.RenderSurfaceView;

import android.view.View.MeasureSpec;

/**
 * @author Nicolas Gramlich
 * @since 11:23:00 - 29.03.2010
 */
public class RatioResolutionPolicy implements IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mRatio;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RatioResolutionPolicy(final float pRatio) {
		this.mRatio = pRatio;
	}

	public RatioResolutionPolicy(final int pWidthRatio, final int pHeightRatio) {
		this.mRatio = (float)pWidthRatio / pHeightRatio;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onMeasure(final RenderSurfaceView pRenderSurfaceView, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
//		final int specWidthMode = MeasureSpec.getMode(pWidthMeasureSpec);
//		final int specHeightMode = MeasureSpec.getMode(pHeightMeasureSpec);

		final int specWidth = MeasureSpec.getSize(pWidthMeasureSpec);
		final int specHeight = MeasureSpec.getSize(pHeightMeasureSpec);

		final float desiredRatio = this.mRatio;
		final float realRatio = (float)specWidth / specHeight;

		int measuredWidth;
		int measuredHeight;
		if(realRatio < desiredRatio) {
			measuredWidth = specWidth;
			measuredHeight = Math.round(measuredWidth / desiredRatio);
		} else {
			measuredHeight = specHeight;
			measuredWidth = Math.round(measuredHeight * desiredRatio);
		}

		pRenderSurfaceView.setMeasuredDimensionProxy(measuredWidth, measuredHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
