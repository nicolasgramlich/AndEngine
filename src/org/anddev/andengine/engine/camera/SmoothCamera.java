package org.anddev.andengine.engine.camera;


/**
 * @author Nicolas Gramlich
 * @since 22:11:17 - 25.03.2010
 */
public class SmoothCamera extends ZoomCamera {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mMaxVelocityX;
	private float mMaxVelocityY;
	private float mMaxZoomFactorChange;

	private float mTargetCenterX;
	private float mTargetCenterY;

	private float mTargetZoomFactor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SmoothCamera(final float pX, final float pY, final float pWidth, final float pHeight, final float pMaxVelocityX, final float pMaxVelocityY, final float pMaxZoomFactorChange) {
		super(pX, pY, pWidth, pHeight);
		this.mMaxVelocityX = pMaxVelocityX;
		this.mMaxVelocityY = pMaxVelocityY;
		this.mMaxZoomFactorChange = pMaxZoomFactorChange;

		this.mTargetCenterX = this.getCenterX();
		this.mTargetCenterY = this.getCenterY();

		this.mTargetZoomFactor = 1.0f;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public void setCenter(final float pCenterX, final float pCenterY) {
		this.mTargetCenterX = pCenterX;
		this.mTargetCenterY = pCenterY;
	}

	public void setCenterDirect(final float pCenterX, final float pCenterY) {
		super.setCenter(pCenterX, pCenterY);
		this.mTargetCenterX = pCenterX;
		this.mTargetCenterY = pCenterY;
	}

	@Override
	public void setZoomFactor(final float pZoomFactor) {
		this.mTargetZoomFactor = pZoomFactor;
	}

	public void setMaxVelocityX(final float pMaxVelocityX) {
		this.mMaxVelocityX = pMaxVelocityX;
	}

	public void setMaxVelocityY(final float pMaxVelocityY) {
		this.mMaxVelocityY = pMaxVelocityY;
	}

	public void setMaxVelocityX(final float pMaxVelocityX, final float pMaxVelocityY) {
		this.mMaxVelocityX = pMaxVelocityX;
		this.mMaxVelocityY = pMaxVelocityY;
	}

	public void setMaxZoomFactorChange(final float pMaxZoomFactorChange) {
		this.mMaxZoomFactorChange = pMaxZoomFactorChange;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		/* Update center. */
		final float currentCenterX = this.getCenterX();
		final float currentCenterY = this.getCenterY();

		final float targetCenterX = this.mTargetCenterX;
		final float targetCenterY = this.mTargetCenterY;

		if(currentCenterX != targetCenterX || currentCenterY != targetCenterY) {
			final float diffX = targetCenterX - currentCenterX;
			final float dX = this.cutToMaxVelocityX(diffX, pSecondsElapsed);

			final float diffY = targetCenterY - currentCenterY;
			final float dY = this.cutToMaxVelocityY(diffY, pSecondsElapsed);

			super.setCenter(currentCenterX + dX, currentCenterY + dY);
		}

		/* Update zoom. */
		final float currentZoom = this.getZoomFactor();

		final float targetZoomFactor = this.mTargetZoomFactor;

		if(currentZoom != targetZoomFactor) {
			final float diffZoom = targetZoomFactor - currentZoom;
			final float dZoom = this.cutToMaxZoomFactorChange(diffZoom, pSecondsElapsed);
			super.setZoomFactor(currentZoom + dZoom);
		}
	}

	private float cutToMaxVelocityX(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0) {
			return Math.min(pValue, this.mMaxVelocityX * pSecondsElapsed);
		} else {
			return Math.max(pValue, -this.mMaxVelocityX * pSecondsElapsed);
		}
	}

	private float cutToMaxVelocityY(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0) {
			return Math.min(pValue, this.mMaxVelocityY * pSecondsElapsed);
		} else {
			return Math.max(pValue, -this.mMaxVelocityY * pSecondsElapsed);
		}
	}

	private float cutToMaxZoomFactorChange(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0) {
			return Math.min(pValue, this.mMaxZoomFactorChange * pSecondsElapsed);
		} else {
			return Math.max(pValue, -this.mMaxZoomFactorChange * pSecondsElapsed);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
