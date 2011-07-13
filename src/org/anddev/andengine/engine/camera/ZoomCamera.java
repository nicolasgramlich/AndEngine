package org.anddev.andengine.engine.camera;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.MathUtils;



/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	protected float mZoomFactor = 1.0f;

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

		if(this.mBoundsEnabled) {
			this.ensureInBounds();
		}
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
	protected void applySceneToCameraSceneOffset(final TouchEvent pSceneTouchEvent) {
		final float zoomFactor = this.mZoomFactor;
		if(zoomFactor != 1) {
			final float scaleCenterX = this.getCenterX();
			final float scaleCenterY = this.getCenterY();

			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.scaleAroundCenter(VERTICES_TOUCH_TMP, zoomFactor, zoomFactor, scaleCenterX, scaleCenterY); // TODO Use a Transformation object instead!?! 

			pSceneTouchEvent.set(VERTICES_TOUCH_TMP[VERTEX_INDEX_X], VERTICES_TOUCH_TMP[VERTEX_INDEX_Y]);
		}
		super.applySceneToCameraSceneOffset(pSceneTouchEvent);
	}

	@Override
	protected void unapplySceneToCameraSceneOffset(final TouchEvent pCameraSceneTouchEvent) {
		super.unapplySceneToCameraSceneOffset(pCameraSceneTouchEvent);

		final float zoomFactor = this.mZoomFactor;
		if(zoomFactor != 1) {
			final float scaleCenterX = this.getCenterX();
			final float scaleCenterY = this.getCenterY();

			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pCameraSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pCameraSceneTouchEvent.getY();

			MathUtils.revertScaleAroundCenter(VERTICES_TOUCH_TMP, zoomFactor, zoomFactor, scaleCenterX, scaleCenterY);

			pCameraSceneTouchEvent.set(VERTICES_TOUCH_TMP[VERTEX_INDEX_X], VERTICES_TOUCH_TMP[VERTEX_INDEX_Y]);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
