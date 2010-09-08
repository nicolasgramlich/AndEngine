package org.anddev.andengine.input.touch.controller;

import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.pool.RunnablePoolItem;
import org.anddev.andengine.util.pool.RunnablePoolUpdateHandler;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 21:06:40 - 13.07.2010
 */
public abstract class BaseTouchController implements ITouchController  {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITouchEventCallback mTouchEventCallback;

	private final boolean mRunOnUpdateThread;

	private final RunnablePoolUpdateHandler<TouchEventRunnablePoolItem> mTouchEventRunnablePoolUpdateHandler = new RunnablePoolUpdateHandler<TouchEventRunnablePoolItem>() {
		@Override
		protected TouchEventRunnablePoolItem onAllocatePoolItem() {
			return new TouchEventRunnablePoolItem();
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTouchController(final boolean pRunOnUpdateThread, final ITouchEventCallback pTouchEventCallback) {
		this.mRunOnUpdateThread = pRunOnUpdateThread;
		this.mTouchEventCallback = pTouchEventCallback;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void reset() {
		if(this.mRunOnUpdateThread) {
			this.mTouchEventRunnablePoolUpdateHandler.reset();
		}
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mRunOnUpdateThread) {
			this.mTouchEventRunnablePoolUpdateHandler.onUpdate(pSecondsElapsed);
		}
	}

	protected boolean fireTouchEvent(final float pX, final float pY, final int pAction, final int pPointerID, final MotionEvent pMotionEvent) {
		final TouchEvent touchEvent = TouchEvent.obtain();
		touchEvent.set(pX, pY, pAction, pPointerID, pMotionEvent);

		if(this.mRunOnUpdateThread) {
			final TouchEventRunnablePoolItem touchEventRunnablePoolItem = this.mTouchEventRunnablePoolUpdateHandler.obtainPoolItem();
			touchEventRunnablePoolItem.set(touchEvent);
			this.mTouchEventRunnablePoolUpdateHandler.postPoolItem(touchEventRunnablePoolItem);
			return true;
		} else {
			final boolean handled = this.mTouchEventCallback.onTouchEvent(touchEvent);
			TouchEvent.recycle(touchEvent);
			return handled;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	class TouchEventRunnablePoolItem extends RunnablePoolItem {
		// ===========================================================
		// Fields
		// ===========================================================

		private TouchEvent mTouchEvent;

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void set(final TouchEvent pTouchEvent) {
			this.mTouchEvent = pTouchEvent;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public void run() {
			BaseTouchController.this.mTouchEventCallback.onTouchEvent(this.mTouchEvent);
			TouchEvent.recycle(this.mTouchEvent);
		}
	}
}
