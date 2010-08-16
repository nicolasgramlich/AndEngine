package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 14:29:59 - 16.08.2010
 */
public class ClickDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long CLICK_MAXIMUM_MILLISECONDS_DEFAULT = 200;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mEnabled;
	private long mClickMaximumMilliseconds = CLICK_MAXIMUM_MILLISECONDS_DEFAULT;
	private final IClickDetectorListener mClickDetectorListener;

	private long mDownTimeMilliseconds = Long.MIN_VALUE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClickDetector(final IClickDetectorListener pClickDetectorListener) {
		this(CLICK_MAXIMUM_MILLISECONDS_DEFAULT, pClickDetectorListener);
	}

	public ClickDetector(final long pClickMaximumMilliseconds, final IClickDetectorListener pClickDetectorListener) {
		this.mClickMaximumMilliseconds = pClickMaximumMilliseconds;
		this.mClickDetectorListener = pClickDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getClickMaximumMilliseconds() {
		return this.mClickMaximumMilliseconds;
	}

	public void setClickMaximumMilliseconds(final long pClickMaximumMilliseconds) {
		this.mClickMaximumMilliseconds = pClickMaximumMilliseconds;
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

	public boolean onTouchEvent(final TouchEvent pTouchEvent) {
		if(this.mEnabled) {
			switch(pTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					this.mDownTimeMilliseconds = pTouchEvent.getMotionEvent().getDownTime();
					return true;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					final long upTimeMilliseconds = pTouchEvent.getMotionEvent().getEventTime();

					if(upTimeMilliseconds - this.mDownTimeMilliseconds <= this.mClickMaximumMilliseconds) {
						this.mDownTimeMilliseconds = Long.MIN_VALUE;
						this.mClickDetectorListener.onClick(this);
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

		public void onClick(final ClickDetector pClickDetector);
	}
}
