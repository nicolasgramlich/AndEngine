package org.andengine.input.touch.detector;

import org.andengine.input.touch.TouchEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @author Greg Haynes
 * @since 14:29:59 - 16.08.2010
 */
public class ClickDetector extends BaseDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long TRIGGER_CLICK_MAXIMUM_MILLISECONDS_DEFAULT = 200;

	// ===========================================================
	// Fields
	// ===========================================================

	private long mTriggerClickMaximumMilliseconds;
	private final IClickDetectorListener mClickDetectorListener;

	private int mPointerID = TouchEvent.INVALID_POINTER_ID;

	private long mDownTimeMilliseconds = Long.MIN_VALUE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClickDetector(final IClickDetectorListener pClickDetectorListener) {
		this(ClickDetector.TRIGGER_CLICK_MAXIMUM_MILLISECONDS_DEFAULT, pClickDetectorListener);
	}

	public ClickDetector(final long pTriggerClickMaximumMilliseconds, final IClickDetectorListener pClickDetectorListener) {
		this.mTriggerClickMaximumMilliseconds = pTriggerClickMaximumMilliseconds;
		this.mClickDetectorListener = pClickDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getTriggerClickMaximumMilliseconds() {
		return this.mTriggerClickMaximumMilliseconds;
	}

	public void setTriggerClickMaximumMilliseconds(final long pClickMaximumMilliseconds) {
		this.mTriggerClickMaximumMilliseconds = pClickMaximumMilliseconds;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void reset() {
		this.mDownTimeMilliseconds = Long.MIN_VALUE;
		this.mPointerID = TouchEvent.INVALID_POINTER_ID;
	}

	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.prepareClick(pSceneTouchEvent);
				return true;
			case TouchEvent.ACTION_MOVE:
				if (this.mPointerID == pSceneTouchEvent.getPointerID()) {
					final long moveTimeMilliseconds = pSceneTouchEvent.getMotionEvent().getEventTime();
					if (moveTimeMilliseconds - this.mDownTimeMilliseconds <= this.mTriggerClickMaximumMilliseconds) {
						return true;
					}
				}
				return false;
			case TouchEvent.ACTION_UP:
			case TouchEvent.ACTION_CANCEL:
				if (this.mPointerID == pSceneTouchEvent.getPointerID()) {
					final long upTimeMilliseconds = pSceneTouchEvent.getMotionEvent().getEventTime();

					boolean handled;
					if (upTimeMilliseconds - this.mDownTimeMilliseconds <= this.mTriggerClickMaximumMilliseconds) {
						this.mDownTimeMilliseconds = Long.MIN_VALUE;
						this.mClickDetectorListener.onClick(this, this.mPointerID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						handled = true;
					} else {
						handled = false;
					}

					this.mPointerID = TouchEvent.INVALID_POINTER_ID;

					return handled;
				} else {
					return false;
				}
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void prepareClick(final TouchEvent pSceneTouchEvent) {
		this.mDownTimeMilliseconds = pSceneTouchEvent.getMotionEvent().getDownTime();
		this.mPointerID = pSceneTouchEvent.getPointerID();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IClickDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY);
	}
}
