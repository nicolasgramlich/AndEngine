package org.andengine.input.touch.detector;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:16:19 - 04.11.2010
 */
public class PinchZoomDetector extends BaseDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private final IPinchZoomDetectorListener mPinchZoomDetectorListener;

	private float mInitialDistance;
	private float mCurrentDistance;

	private boolean mPinchZooming;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PinchZoomDetector(final IPinchZoomDetectorListener pPinchZoomDetectorListener) {
		this.mPinchZoomDetectorListener = pPinchZoomDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isZooming() {
		return this.mPinchZooming;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * When {@link #isZooming()} this method will call through to {@link IPinchZoomDetectorListener#onPinchZoomFinished(PinchZoomDetector, TouchEvent, float)}.
	 */
	@Override
	public void reset() {
		if (this.mPinchZooming) {
			this.mPinchZoomDetectorListener.onPinchZoomFinished(this, null, this.getZoomFactor());
		}

		this.mInitialDistance = 0;
		this.mCurrentDistance = 0;
		this.mPinchZooming = false;
	}

	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		final int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

		switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:
				if (!this.mPinchZooming && PinchZoomDetector.hasTwoOrMorePointers(motionEvent)) {
					this.mInitialDistance = PinchZoomDetector.calculatePointerDistance(motionEvent);
					this.mCurrentDistance = this.mInitialDistance;
					if (this.mInitialDistance > PinchZoomDetector.TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT) {
						this.mPinchZooming = true;
						this.mPinchZoomDetectorListener.onPinchZoomStarted(this, pSceneTouchEvent);
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (this.mPinchZooming) {
					this.mPinchZooming = false;
					this.mPinchZoomDetectorListener.onPinchZoomFinished(this, pSceneTouchEvent, this.getZoomFactor());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (this.mPinchZooming) {
					if (PinchZoomDetector.hasTwoOrMorePointers(motionEvent)) {
						this.mCurrentDistance = PinchZoomDetector.calculatePointerDistance(motionEvent);
						if (this.mCurrentDistance > PinchZoomDetector.TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT) {
							this.mPinchZoomDetectorListener.onPinchZoom(this, pSceneTouchEvent, this.getZoomFactor());
						}
					} else {
						this.mPinchZooming = false;
						this.mPinchZoomDetectorListener.onPinchZoomFinished(this, pSceneTouchEvent, this.getZoomFactor());
					}
				}
				break;
		}
		return true;
	}

	private float getZoomFactor() {
		return this.mCurrentDistance / this.mInitialDistance;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Calculate the euclidian distance between the first two fingers.
	 */
	private static float calculatePointerDistance(final MotionEvent pMotionEvent) {
		return MathUtils.distance(pMotionEvent.getX(0), pMotionEvent.getY(0), pMotionEvent.getX(1), pMotionEvent.getY(1));
	}

	private static boolean hasTwoOrMorePointers(final MotionEvent pMotionEvent) {
		return pMotionEvent.getPointerCount() >= 2;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPinchZoomDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pSceneTouchEvent);
		public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor);
		public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor);
	}
}
