package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 14:29:59 - 16.08.2010
 */
public abstract class ScrollDetector implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float TRIGGER_SCROLL_MINIMUM_DISTANCE_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mEnabled = true;

	private float mTriggerScrollMinimumDistance;

	private final IScrollDetectorListener mScrollDetectorListener;

	private boolean mTriggered;

	private float mLastX;
	private float mLastY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScrollDetector(final IScrollDetectorListener pScrollDetectorListener) {
		this(TRIGGER_SCROLL_MINIMUM_DISTANCE_DEFAULT, pScrollDetectorListener);
	}

	public ScrollDetector(final float pTriggerScrollMinimumDistance, final IScrollDetectorListener pScrollDetectorListener) {
		this.mTriggerScrollMinimumDistance = pTriggerScrollMinimumDistance;
		this.mScrollDetectorListener = pScrollDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getTriggerScrollMinimumDistance() {
		return this.mTriggerScrollMinimumDistance;
	}

	public void setTriggerScrollMinimumDistance(final float pTriggerScrollMinimumDistance) {
		this.mTriggerScrollMinimumDistance = pTriggerScrollMinimumDistance;
	}

	public boolean isEnabled() {
		return this.mEnabled;
	}

	public void setEnabled(final boolean pEnabled) {
		this.mEnabled = pEnabled;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(this.mEnabled) {
			final float touchX = this.getX(pSceneTouchEvent);
			final float touchY = this.getY(pSceneTouchEvent);

			switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					this.mLastX = touchX;
					this.mLastY = touchY;
					this.mTriggered = false;
					return true;
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					final float distanceX = touchX - this.mLastX;
					final float distanceY = touchY - this.mLastY;

					final float triggerScrollMinimumDistance = this.mTriggerScrollMinimumDistance;
					if(this.mTriggered || Math.abs(distanceX) > triggerScrollMinimumDistance || Math.abs(distanceY) > triggerScrollMinimumDistance) {
						this.mScrollDetectorListener.onScroll(this, pSceneTouchEvent, distanceX, distanceY);
						this.mLastX = touchX;
						this.mLastY = touchY;
						this.mTriggered = true;
					}
					return true;
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected float getX(final TouchEvent pTouchEvent) {
		return pTouchEvent.getX();
	}

	protected float getY(final TouchEvent pTouchEvent) {
		return pTouchEvent.getY();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IScrollDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY);
	}
}
