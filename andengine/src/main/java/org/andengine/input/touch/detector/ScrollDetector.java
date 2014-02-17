package org.andengine.input.touch.detector;

import org.andengine.input.touch.TouchEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:29:59 - 16.08.2010
 */
public class ScrollDetector extends BaseDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float TRIGGER_SCROLL_MINIMUM_DISTANCE_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private float mTriggerScrollMinimumDistance;
	private final IScrollDetectorListener mScrollDetectorListener;
	
	private int mPointerID = TouchEvent.INVALID_POINTER_ID;

	private boolean mTriggering;

	private float mLastX;
	private float mLastY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScrollDetector(final IScrollDetectorListener pScrollDetectorListener) {
		this(ScrollDetector.TRIGGER_SCROLL_MINIMUM_DISTANCE_DEFAULT, pScrollDetectorListener);
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void reset() {
		if(this.mTriggering) {
			this.mScrollDetectorListener.onScrollFinished(this, this.mPointerID, 0, 0);
		}

		this.mLastX = 0;
		this.mLastY = 0;
		this.mTriggering = false;
		this.mPointerID = TouchEvent.INVALID_POINTER_ID;
	}

	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final float touchX = this.getX(pSceneTouchEvent);
		final float touchY = this.getY(pSceneTouchEvent);

		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.prepareScroll(pSceneTouchEvent.getPointerID(), touchX, touchY);
				return true;
			case TouchEvent.ACTION_MOVE:
				if(this.mPointerID == TouchEvent.INVALID_POINTER_ID) {
					this.prepareScroll(pSceneTouchEvent.getPointerID(), touchX, touchY);
					return true;
				} else if(this.mPointerID == pSceneTouchEvent.getPointerID()) {
					final float distanceX = touchX - this.mLastX;
					final float distanceY = touchY - this.mLastY;

					final float triggerScrollMinimumDistance = this.mTriggerScrollMinimumDistance;
					if(this.mTriggering || Math.abs(distanceX) > triggerScrollMinimumDistance || Math.abs(distanceY) > triggerScrollMinimumDistance) {
						if(!this.mTriggering) {
							this.triggerOnScrollStarted(distanceX, distanceY);
						} else {
							this.triggerOnScroll(distanceX, distanceY);
						}
						
						this.mLastX = touchX;
						this.mLastY = touchY;
						this.mTriggering = true;
					}
					return true;
				} else {
					return false;
				}
				case TouchEvent.ACTION_UP:
				case TouchEvent.ACTION_CANCEL:
					if(this.mPointerID == pSceneTouchEvent.getPointerID()) {
						final float distanceX = touchX - this.mLastX;
						final float distanceY = touchY - this.mLastY;

						if(this.mTriggering) {
							this.triggerOnScrollFinished(distanceX, distanceY);
						}

						this.mPointerID = TouchEvent.INVALID_POINTER_ID;
					}
					return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	private void prepareScroll(final int pPointerID, final float pTouchX, final float pTouchY) {
		this.mLastX = pTouchX;
		this.mLastY = pTouchY;
		this.mTriggering = false;
		this.mPointerID = pPointerID;
	}
	
	private void triggerOnScrollStarted(final float pDistanceX, final float pDistanceY) {
		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			this.mScrollDetectorListener.onScrollStarted(this, this.mPointerID, pDistanceX, pDistanceY);
		}
	}
	
	private void triggerOnScroll(final float pDistanceX, final float pDistanceY) {
		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			this.mScrollDetectorListener.onScroll(this, this.mPointerID, pDistanceX, pDistanceY);
		}
	}
	
	private void triggerOnScrollFinished(final float pDistanceX, final float pDistanceY) {
		this.mTriggering = false;
		
		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			this.mScrollDetectorListener.onScrollFinished(this, this.mPointerID, pDistanceX, pDistanceY);
		}
	}

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

		public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY);
		public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY);
		public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY);
	}
}
