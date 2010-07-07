package org.anddev.andengine.entity;

import org.anddev.andengine.entity.Scene.ITouchArea;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 12:06:43 - 11.03.2010
 */
public abstract class StaticEntity extends BaseEntity implements ITouchArea {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mBaseX;
	protected final float mBaseY;

	protected float mX;
	protected float mY;

	protected float mOffsetX = 0;
	protected float mOffsetY = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StaticEntity(final float pX, final float pY) {
		this.mBaseX = pX;
		this.mBaseY = pY;

		this.mX = pX;
		this.mY = pY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getX() {
		return this.mX;
	}

	public float getY() {
		return this.mY;
	}

	public float getBaseX() {
		return this.mBaseX;
	}

	public float getBaseY() {
		return this.mBaseY;
	}

	public abstract float getCenterX();

	public abstract float getCenterY();

	public float getOffsetX() {
		return this.mOffsetX;
	}

	public float getOffsetY() {
		return this.mOffsetY;
	}

	public void setOffsetX(final float pOffsetX) {
		this.mOffsetX = pOffsetX;
		this.onPositionChanged();
	}

	public void setOffsetY(final float pOffsetY) {
		this.mOffsetY = pOffsetY;
		this.onPositionChanged();
	}

	public void setOffset(final float pOffsetX, final float pOffsetY) {
		this.mOffsetX = pOffsetX;
		this.mOffsetY = pOffsetY;
		this.onPositionChanged();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onPositionChanged(){
		
	}
	
	@Override
	public boolean onAreaTouched(final MotionEvent pSceneMotionEvent) {
		return false;
	}

	@Override
	public void reset() {
		super.reset();

		this.mOffsetX = 0;
		this.mOffsetY = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
