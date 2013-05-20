package org.andengine.input.touch.detector;

import android.content.Context;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:53:40 - 04.04.2012
 */
public class SurfaceGestureDetectorAdapter extends SurfaceGestureDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SurfaceGestureDetectorAdapter(final Context pContext) {
		super(pContext);
	}

	public SurfaceGestureDetectorAdapter(final Context pContext, final float pSwipeMinDistance) {
		super(pContext, pSwipeMinDistance);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected boolean onDoubleTap() {
		return false;
	}

	@Override
	protected boolean onSingleTap() {
		return false;
	}

	@Override
	protected boolean onSwipeDown() {
		return false;
	}

	@Override
	protected boolean onSwipeLeft() {
		return false;
	}

	@Override
	protected boolean onSwipeRight() {
		return false;
	}

	@Override
	protected boolean onSwipeUp() {
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}