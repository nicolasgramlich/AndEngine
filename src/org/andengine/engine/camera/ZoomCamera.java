package org.andengine.engine.camera;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;



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

		if (this.mBoundsEnabled) {
			this.ensureInBounds();
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getXMin() {
		if (this.mZoomFactor == 1.0f) {
			return super.getXMin();
		} else {
			final float centerX = this.getCenterX();
			return centerX - (centerX - super.getXMin()) / this.mZoomFactor;
		}
	}

	@Override
	public float getXMax() {
		if (this.mZoomFactor == 1.0f) {
			return super.getXMax();
		} else {
			final float centerX = this.getCenterX();
			return centerX + (super.getXMax() - centerX) / this.mZoomFactor;
		}
	}

	@Override
	public float getYMin() {
		if (this.mZoomFactor == 1.0f) {
			return super.getYMin();
		} else {
			final float centerY = this.getCenterY();
			return centerY - (centerY - super.getYMin()) / this.mZoomFactor;
		}
	}

	@Override
	public float getYMax() {
		if (this.mZoomFactor == 1.0f) {
			return super.getYMax();
		} else {
			final float centerY = this.getCenterY();
			return centerY + (super.getYMax() - centerY) / this.mZoomFactor;
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
		if (zoomFactor != 1) {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.scaleAroundCenter(Camera.VERTICES_TMP, zoomFactor, zoomFactor, this.getCenterX(), this.getCenterY()); // TODO Use a Transformation object instead!?!

			pSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}

		super.applySceneToCameraSceneOffset(pSceneTouchEvent);
	}

	@Override
	protected void applySceneToCameraSceneOffset(final float[] pSceneCoordinates) {
		final float zoomFactor = this.mZoomFactor;
		if (zoomFactor != 1) {
			MathUtils.scaleAroundCenter(pSceneCoordinates, zoomFactor, zoomFactor, this.getCenterX(), this.getCenterY()); // TODO Use a Transformation object instead!?!
		}

		super.applySceneToCameraSceneOffset(pSceneCoordinates);
	}

	@Override
	protected void unapplySceneToCameraSceneOffset(final TouchEvent pCameraSceneTouchEvent) {
		super.unapplySceneToCameraSceneOffset(pCameraSceneTouchEvent);

		final float zoomFactor = this.mZoomFactor;
		if (zoomFactor != 1) {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pCameraSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pCameraSceneTouchEvent.getY();

			MathUtils.revertScaleAroundCenter(Camera.VERTICES_TMP, zoomFactor, zoomFactor, this.getCenterX(), this.getCenterY()); // TODO Use a Transformation object instead!?!

			pCameraSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}
	}

	@Override
	protected void unapplySceneToCameraSceneOffset(final float[] pCameraSceneCoordinates) {
		super.unapplySceneToCameraSceneOffset(pCameraSceneCoordinates);

		final float zoomFactor = this.mZoomFactor;
		if (zoomFactor != 1) {
			MathUtils.revertScaleAroundCenter(pCameraSceneCoordinates, zoomFactor, zoomFactor, this.getCenterX(), this.getCenterY()); // TODO Use a Transformation object instead!?!
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
