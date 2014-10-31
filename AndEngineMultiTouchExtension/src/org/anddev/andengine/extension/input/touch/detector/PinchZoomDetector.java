package org.anddev.andengine.extension.input.touch.detector;

import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.BaseDetector;

import android.util.FloatMath;
import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
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

	public PinchZoomDetector(final IPinchZoomDetectorListener pPinchZoomDetectorListener) throws MultiTouchException {
		if (MultiTouch.isSupportedByAndroidVersion() == false) {
			throw new MultiTouchException();
		}
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

    @Override
    public void reset() {

    }

    @Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		final int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

		switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:
				if (!this.mPinchZooming) {
					this.mInitialDistance = PinchZoomDetector.calculatePointerDistance(motionEvent);
					if (this.mInitialDistance > TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT) {
						this.mPinchZooming = true;
						this.mPinchZoomDetectorListener.onPinchZoomStarted(this, pSceneTouchEvent);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (this.mPinchZooming) {
					this.mPinchZooming = false;
					this.mPinchZoomDetectorListener.onPinchZoomFinished(this, pSceneTouchEvent, this.getZoomFactor());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (this.mPinchZooming) {
					this.mCurrentDistance = PinchZoomDetector.calculatePointerDistance(motionEvent);
					if (this.mCurrentDistance > TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT) {
						this.mPinchZoomDetectorListener.onPinchZoom(this, pSceneTouchEvent, this.getZoomFactor());
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
		final float x = pMotionEvent.getX(0) - pMotionEvent.getX(1);
		final float y = pMotionEvent.getY(0) - pMotionEvent.getY(1);
		return FloatMath.sqrt(x * x + y * y);
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
