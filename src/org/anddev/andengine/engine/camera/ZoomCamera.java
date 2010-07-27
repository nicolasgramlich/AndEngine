package org.anddev.andengine.engine.camera;

import org.anddev.andengine.input.touch.TouchEvent;


/**
 * @author Nicolas Gramlich
 * @since 15:48:11 - 24.06.2010
 * TODO min/max(X/Y) values could be cached and only updated once the zoomfactor/center changed.
 */
public class ZoomCamera extends BoundCamera {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mZoomFactor = 1.0f;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ZoomCamera(final float pX, final float pY, final float pWidth, final float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getZoomFactor() {
		return this.mZoomFactor;
	}

	public void setZoomFactor(final float pZoomFactor) {
		this.mZoomFactor = pZoomFactor;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getMinX() {
		if(this.mZoomFactor == 1.0f) {
			return super.getMinX();
		} else {
			final float centerX = this.getCenterX();
			return centerX - (centerX - super.getMinX()) / this.mZoomFactor;
		}
	}

	@Override
	public float getMaxX() {
		if(this.mZoomFactor == 1.0f) {
			return super.getMaxX();
		} else {
			final float centerX = this.getCenterX();
			return centerX + (super.getMaxX() - centerX) / this.mZoomFactor;
		}
	}

	@Override
	public float getMinY() {
		if(this.mZoomFactor == 1.0f) {
			return super.getMinY();
		} else {
			final float centerY = this.getCenterY();
			return centerY - (centerY - super.getMinY()) / this.mZoomFactor;
		}
	}

	@Override
	public float getMaxY() {
		if(this.mZoomFactor == 1.0f) {
			return super.getMaxY();
		} else {
			final float centerY = this.getCenterY();
			return centerY + (super.getMaxY() - centerY) / this.mZoomFactor;
		}
	}

	@Override
	public float getWidth() {
		return super.getWidth() / this.mZoomFactor;
	}

	@Override
	public float getHeight() {
		return super.getHeight() / this.mZoomFactor;
	}

	@Override
	public void convertSceneToHUDTouchEvent(final TouchEvent pSceneTouchEvent) {
		final float x = (pSceneTouchEvent.getX() - this.getMinX()) * this.getZoomFactor();
		final float y = (pSceneTouchEvent.getY() - this.getMinY()) * this.getZoomFactor();
		pSceneTouchEvent.set(x, y);
	}

	@Override
	public void convertHUDToSceneTouchEvent(final TouchEvent pHUDTouchEvent) {
		final float x = pHUDTouchEvent.getX() / this.getZoomFactor() + this.getMinX();
		final float y = pHUDTouchEvent.getY() / this.getZoomFactor() + this.getMinY();
		pHUDTouchEvent.set(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
