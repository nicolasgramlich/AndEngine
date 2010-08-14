package org.anddev.andengine.engine.camera.hud.controls;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.constants.TimeConstants;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 00:21:55 - 11.07.2010
 */
public class AnalogOnScreenControl extends BaseOnScreenControl implements TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long ONCONTROLCLICK_MAXIMUM_MILLISECONDS_DEFAULT = 200;

	// ===========================================================
	// Fields
	// ===========================================================

	private long mDownTimeMilliseconds = Long.MIN_VALUE;

	private boolean mOnControlClickEnabled;
	private long mOnControlClickMaximumMilliseconds = ONCONTROLCLICK_MAXIMUM_MILLISECONDS_DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnalogOnScreenControl(final int pX, final int pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final IAnalogOnScreenControlListener pAnalogOnScreenControlListener) {
		super(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, pAnalogOnScreenControlListener);
		this.mOnControlClickEnabled = false;
	}

	public AnalogOnScreenControl(final int pX, final int pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final long pOnControlClickMaximumMilliseconds, final IAnalogOnScreenControlListener pAnalogOnScreenControlListener) {
		super(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, pAnalogOnScreenControlListener);
		this.mOnControlClickMaximumMilliseconds = pOnControlClickMaximumMilliseconds;
		this.mOnControlClickEnabled = true;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public IAnalogOnScreenControlListener getOnScreenControlListener() {
		return (IAnalogOnScreenControlListener)super.getOnScreenControlListener();
	}

	public void setOnControlClickEnabled(final boolean pOnControlClickEnabled) {
		this.mOnControlClickEnabled = pOnControlClickEnabled;
	}

	public void setOnControlClickMaximumMilliseconds(final long pOnControlClickMaximumMilliseconds) {
		this.mOnControlClickMaximumMilliseconds = pOnControlClickMaximumMilliseconds;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected boolean onHandleControlBaseTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(this.mOnControlClickEnabled) {
			switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					this.mDownTimeMilliseconds = pSceneTouchEvent.getMotionEvent().getDownTime();
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					final long upTimeMilliseconds = pSceneTouchEvent.getMotionEvent().getEventTime();

					if(upTimeMilliseconds - this.mDownTimeMilliseconds <= this.mOnControlClickMaximumMilliseconds) {
						this.mDownTimeMilliseconds = Long.MIN_VALUE;
						this.getOnScreenControlListener().onControlClick(this);
					}
					break;
			}
		}
		return super.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface IAnalogOnScreenControlListener extends IOnScreenControlListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl);
	}
}
