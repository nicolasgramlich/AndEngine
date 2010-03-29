package org.anddev.andengine.engine.camera;


/**
 * @author Nicolas Gramlich
 * @since 22:11:17 - 25.03.2010
 */
public class SmoothCamera extends Camera {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mMaxVelocityX;
	private final float mMaxVelocityY;
	
	private float mTargetCenterX;
	private float mTargetCenterY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SmoothCamera(final float pMinX, final float pMaxX, final float pMinY, final float pMaxY, final float pMaxVelocityX, final float pMaxVelocityY) {
		super(pMinX, pMaxX, pMinY, pMaxY);
		this.mMaxVelocityX = pMaxVelocityX;
		this.mMaxVelocityY = pMaxVelocityY;
		
		this.mTargetCenterX = this.getCenterX();
		this.mTargetCenterY = this.getCenterY();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	@Override
	public void setCenter(final float pCenterX, final float pCenterY) {
		this.mTargetCenterX = pCenterX;
		this.mTargetCenterY = pCenterY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final float currentCenterX = this.getCenterX();
		final float currentCenterY = this.getCenterY();

		final float diffX = this.mTargetCenterX - currentCenterX;
		final float dX = this.cutToMaxVelocityX(diffX, pSecondsElapsed);
		
		final float diffY = this.mTargetCenterY - currentCenterY;
		final float dY = this.cutToMaxVelocityY(diffY, pSecondsElapsed);
		
		super.setCenter(currentCenterX + dX, currentCenterY + dY);
	}

	private float cutToMaxVelocityX(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0)
			return Math.min(this.mMaxVelocityX * pSecondsElapsed, pValue);
		else
			return Math.max(-this.mMaxVelocityX * pSecondsElapsed, pValue);
	}

	private float cutToMaxVelocityY(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0)
			return Math.min(this.mMaxVelocityY * pSecondsElapsed, pValue);
		else
			return Math.max(-this.mMaxVelocityY * pSecondsElapsed, pValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
