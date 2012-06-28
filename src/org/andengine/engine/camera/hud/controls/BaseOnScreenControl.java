package org.andengine.engine.camera.hud.controls;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	private float mControlValueX;
	private float mControlValueY;

	private final IOnScreenControlListener mOnScreenControlListener;

	private int mActivePointerID = INVALID_POINTER_ID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseOnScreenControl(final float pX, final float pY, final Camera pCamera, final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final VertexBufferObjectManager pVertexBufferObjectManager, final IOnScreenControlListener pOnScreenControlListener) {
		this.setCamera(pCamera);

		this.mOnScreenControlListener = pOnScreenControlListener;
		/* Create the control base. */
		this.mControlBase = new Sprite(pX, pY, pControlBaseTextureRegion, pVertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				return BaseOnScreenControl.this.onHandleControlBaseTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		/* Create the control knob. */
		this.mControlKnob = new Sprite(0, 0, pControlKnobTextureRegion, pVertexBufferObjectManager);
		this.onHandleControlKnobReleased();

		/* Register listeners and add objects to this HUD. */
		this.setOnSceneTouchListener(this);
		this.registerTouchArea(this.mControlBase);
		this.registerUpdateHandler(new TimerHandler(pTimeBetweenUpdates, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				BaseOnScreenControl.this.mOnScreenControlListener.onControlChange(BaseOnScreenControl.this, BaseOnScreenControl.this.mControlValueX, BaseOnScreenControl.this.mControlValueY);
			}
		}));

		this.attachChild(this.mControlBase);
		this.attachChild(this.mControlKnob);

		this.setTouchAreaBindingOnActionDownEnabled(true);
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

	public IOnScreenControlListener getOnScreenControlListener() {
		return this.mOnScreenControlListener;
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

	public void refreshControlKnobPosition() {
		this.onUpdateControlKnob(this.mControlValueX * 0.5f, this.mControlValueY * 0.5f);
	}

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

	protected boolean onHandleControlBaseTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final int pointerID = pSceneTouchEvent.getPointerID();

		switch(pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(this.mActivePointerID == INVALID_POINTER_ID) {
					this.mActivePointerID = pointerID;
					this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(this.mActivePointerID == pointerID) {
					this.mActivePointerID = INVALID_POINTER_ID;
					this.onHandleControlKnobReleased();
					return true;
				}
				break;
			default:
				if(this.mActivePointerID == pointerID) {
					this.updateControlKnob(pTouchAreaLocalX, pTouchAreaLocalY);
					return true;
				}
				break;
		}
		return true;
	}

	private void updateControlKnob(final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final Sprite controlBase = this.mControlBase;

		final float relativeX = MathUtils.bringToBounds(0, controlBase.getWidth(), pTouchAreaLocalX) / controlBase.getWidth() - 0.5f;
		final float relativeY = MathUtils.bringToBounds(0, controlBase.getHeight(), pTouchAreaLocalY) / controlBase.getHeight() - 0.5f;

		this.onUpdateControlKnob(relativeX, relativeY);
	}

	/**
	 * @param pRelativeX from <code>-0.5</code> (left) to <code>0.5</code> (right).
	 * @param pRelativeY from <code>-0.5</code> (top) to <code>0.5</code> (bottom).
	 */
	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
		final Sprite controlBase = this.mControlBase;
		final Sprite controlKnob = this.mControlKnob;

		this.mControlValueX = 2 * pRelativeX;
		this.mControlValueY = 2 * pRelativeY;

		final float[] controlBaseSceneCenterCoordinates = controlBase.getSceneCenterCoordinates();
		final float x = controlBaseSceneCenterCoordinates[VERTEX_INDEX_X] - controlKnob.getWidth() * 0.5f + pRelativeX * controlBase.getWidthScaled();
		final float y = controlBaseSceneCenterCoordinates[VERTEX_INDEX_Y] - controlKnob.getHeight() * 0.5f + pRelativeY * controlBase.getHeightScaled();

		controlKnob.setPosition(x, y);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IOnScreenControlListener {
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
