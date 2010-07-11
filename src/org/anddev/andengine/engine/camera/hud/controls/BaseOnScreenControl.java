package org.anddev.andengine.engine.camera.hud.controls;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
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

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseOnScreenControl(final int pX, final int pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final OnScreenControlListener pAnalogOnScreenControlListener) {
		this.setCamera(pCamera);

		this.mOnScreenControlListener = pAnalogOnScreenControlListener;

		this.mControlBase = new Sprite(pX, pY, pControlBaseTextureRegion) {
			@Override
			public boolean onAreaTouched(final MotionEvent pSceneMotionEvent) {
				switch(pSceneMotionEvent.getAction()) {
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						BaseOnScreenControl.this.onSetKnobToDefaultPosition();
						break;
					default:
						BaseOnScreenControl.this.updateControKnob(pSceneMotionEvent);
				}
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
		this.onSetKnobToDefaultPosition();

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
	public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
		this.onSetKnobToDefaultPosition();
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void onSetKnobToDefaultPosition() {
		this.onUpdateControlKnob(0, 0);
	}

	private void updateControKnob(final MotionEvent pSceneMotionEvent) {
		final float sceneMotionEventX = pSceneMotionEvent.getX();
		final float sceneMotionEventY = pSceneMotionEvent.getY();

		this.onUpdateControlKnob((sceneMotionEventX - this.mControlBaseCenterX) / this.mControlBaseWidthHalf, (sceneMotionEventY - this.mControlBaseCenterY) / this.mControlBaseHeightHalf);
	}

	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
		final Sprite controlKnob = this.getControlKnob();

		this.mControlValueX = pRelativeX;
		this.mControlValueY = pRelativeY;

		final float x = this.mControlBaseCenterX - controlKnob.getWidth() / 2 + pRelativeX * this.mControlBaseWidthHalf;
		final float y = this.mControlBaseCenterY - controlKnob.getHeight() / 2 + pRelativeY * this.mControlBaseHeightHalf;

		controlKnob.setPosition(x, y);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface OnScreenControlListener {
		public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY);
	}
}
