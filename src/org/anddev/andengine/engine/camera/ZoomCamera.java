package org.anddev.andengine.engine.camera;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.MathUtils;


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
		final float rotation = this.mRotation;
		
		if(rotation != 0) {
			VERTICES_TOUCH_TMP[0] = pSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[0 + VERTEX_INDEX_Y] = pSceneTouchEvent.getY();
			
			MathUtils.rotateAroundCenter(VERTICES_TOUCH_TMP, rotation, this.getCenterX(), this.getCenterY());
	
			pSceneTouchEvent.set(VERTICES_TOUCH_TMP[0], VERTICES_TOUCH_TMP[0 + VERTEX_INDEX_Y]);
		}
		
		final float zoomFactor = this.mZoomFactor;
		
		final float x = (pSceneTouchEvent.getX() - this.getMinX()) * zoomFactor;
		final float y = (pSceneTouchEvent.getY() - this.getMinY()) * zoomFactor;
		pSceneTouchEvent.set(x, y);
	}

	@Override
	public void convertHUDToSceneTouchEvent(final TouchEvent pHUDTouchEvent) {
		final float zoomFactor = this.mZoomFactor;
		
		final float x = pHUDTouchEvent.getX() / zoomFactor + this.getMinX();
		final float y = pHUDTouchEvent.getY() / zoomFactor + this.getMinY();
		pHUDTouchEvent.set(x, y);
		
		final float rotation = this.mRotation;
		if(rotation != 0) {
			VERTICES_TOUCH_TMP[0] = pHUDTouchEvent.getX();
			VERTICES_TOUCH_TMP[0 + VERTEX_INDEX_Y] = pHUDTouchEvent.getY();
			
			MathUtils.revertRotateAroundCenter(VERTICES_TOUCH_TMP, rotation, this.getCenterX(), this.getCenterY());
	
			pHUDTouchEvent.set(VERTICES_TOUCH_TMP[0], VERTICES_TOUCH_TMP[0 + VERTEX_INDEX_Y]);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
