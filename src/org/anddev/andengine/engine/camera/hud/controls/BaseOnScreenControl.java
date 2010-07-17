package org.anddev.andengine.engine.camera.hud.controls;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 10:43:09 - 11.07.2010
 */
public abstract class BaseOnScreenControl extends HUD implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int INVALID_POINTER_ID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Sprite mControlBase;
	private final Sprite mControlKnob;

	protected final float mControlBaseCenterX;
	protected final float mControlBaseCenterY;

	private float mControlValueX;
	private float mControlValueY;

	private final float mControlBaseWidthHalf;
	private final float mControlBaseHeightHalf;

	private final OnScreenControlListener mOnScreenControlListener;

	private int mActivePointerID = INVALID_POINTER_ID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseOnScreenControl(final int pX, final int pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final OnScreenControlListener pAnalogOnScreenControlListener) {
		this.setCamera(pCamera);

		this.mOnScreenControlListener = pAnalogOnScreenControlListener;

		this.mControlBase = new Sprite(pX, pY, pControlBaseTextureRegion) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent) {
				BaseOnScreenControl.this.onHandleControlBaseTouched(pSceneTouchEvent);
				return true;
			}
		};

		/* Cache some variables of the ControlBase. */
		final Sprite controlBase = this.mControlBase;

		this.mControlBaseWidthHalf = controlBase.getWidth() * 0.5f;
		this.mControlBaseHeightHalf = controlBase.getHeight() * 0.5f;

		this.mControlBaseCenterX = controlBase.getCenterX();
		this.mControlBaseCenterY = controlBase.getCenterY();

		/* Create the knob. */
		this.mControlKnob = new Sprite(0, 0, pControlKnobTextureRegion);
		this.onHandleControlKnobReleased();

		/* Register listeners and add objects to this HUD. */
		this.setOnSceneTouchListener(this);
		this.registerTouchArea(controlBase);
		this.registerPreFrameHandler(new TimerHandler(pTimeBetweenUpdates, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				pTimerHandler.reset();
				BaseOnScreenControl.this.mOnScreenControlListener.onControlChange(BaseOnScreenControl.this, BaseOnScreenControl.this.mControlValueX, BaseOnScreenControl.this.mControlValueY);
			}
		}));

		final ILayer bottomLayer = this.getBottomLayer();
		bottomLayer.addEntity(controlBase);
		bottomLayer.addEntity(this.mControlKnob);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Sprite getControlBase() {
		return this.mControlBase;
	}

	public Sprite getControlKnob() {
		return this.mControlKnob;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		final int pointerID = pSceneTouchEvent.getPointerID();
		if(pointerID == this.mActivePointerID) {
			this.onHandleControlBaseLeft();

			switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					this.mActivePointerID = INVALID_POINTER_ID;
			}
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 *  When the touch happened outside of the bounds of this OnScreenControl. 
	 * */
	protected void onHandleControlBaseLeft() {
		this.onUpdateControlKnob(0, 0);
	}

	/**
	 * When the OnScreenControl was released.
	 */
	protected void onHandleControlKnobReleased() {
		this.onUpdateControlKnob(0, 0);
	}	

	private void updateControlKnob(final TouchEvent pSceneTouchEvent) {
		final float sceneTouchEventX = pSceneTouchEvent.getX();
		final float sceneTouchEventY = pSceneTouchEvent.getY();

		this.onUpdateControlKnob((sceneTouchEventX - this.mControlBaseCenterX) / this.mControlBaseWidthHalf, (sceneTouchEventY - this.mControlBaseCenterY) / this.mControlBaseHeightHalf);
	}

	/**
	 * @param pRelativeX from <code>-1</code> (left) to <code>1</code> (right).
	 * @param pRelativeY from <code>-1</code> (top) to <code>1</code> (bottom).
	 */
	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
		final Sprite controlKnob = this.getControlKnob();

		this.mControlValueX = pRelativeX;
		this.mControlValueY = pRelativeY;

		final float x = this.mControlBaseCenterX - controlKnob.getWidth() / 2 + pRelativeX * this.mControlBaseWidthHalf;
		final float y = this.mControlBaseCenterY - controlKnob.getHeight() / 2 + pRelativeY * this.mControlBaseHeightHalf;

		controlKnob.setPosition(x, y);
	}

	protected boolean onHandleControlBaseTouched(final TouchEvent pSceneTouchEvent) {
		final int pointerID = pSceneTouchEvent.getPointerID();

		switch(pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(this.mActivePointerID == INVALID_POINTER_ID) {
					this.mActivePointerID = pointerID;
					BaseOnScreenControl.this.updateControlKnob(pSceneTouchEvent);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(this.mActivePointerID == pointerID) {
					this.mActivePointerID = INVALID_POINTER_ID;
					BaseOnScreenControl.this.onHandleControlKnobReleased();
					return true;
				}
				break;
			default:
				if(this.mActivePointerID == pointerID) {
					BaseOnScreenControl.this.updateControlKnob(pSceneTouchEvent);
					return true;
				}
				break;
		}
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface OnScreenControlListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		/**
		 * @param pBaseOnScreenControl
		 * @param pValueX between <code>-1</code> (left) to <code>1</code> (right).
		 * @param pValueY between <code>-1</code> (up) to <code>1</code> (down).
		 */
		public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY);
	}
}
