package org.anddev.andengine.entity;


/**
 * @author Nicolas Gramlich
 * @since 14:22:22 - 10.03.2010
 */
public abstract class DynamicEntity extends StaticEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mAccelerationX = 0;
	private float mAccelerationY = 0;
	private float mVelocityX = 0;
	private float mVelocityY = 0;
	
	private float mRotationAngleClockwise = 0;
	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicEntity(final float pX, final float pY, final int pWidth, final int pHeight) {
		super(pX, pY, pWidth, pHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getVelocityX() {
		return this.mVelocityX;
	}

	public float getVelocityY() {
		return this.mVelocityY;
	}
	
	public void setVelocity(final float pVelocityX, final float pVelocityY) {
		this.mVelocityX = pVelocityX;
		this.mVelocityY = pVelocityY;
	}

	public float getAccelerationX() {
		return this.mAccelerationX;
	}

	public float getAccelerationY() {
		return this.mAccelerationY;
	}

	public void setAcceleration(final float pAccelerationX, final float pAccelerationY) {
		this.mAccelerationX = pAccelerationX;
		this.mAccelerationY = pAccelerationY;
	}
	
	public float getRotationAngleClockwise() {
		return this.mRotationAngleClockwise;
	}
	
	public void setRotationAngleClockwise(final float pRotationAngleClockwise) {
		this.mRotationAngleClockwise = pRotationAngleClockwise;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mAccelerationX != 0 || this.mAccelerationY != 0 || this.mVelocityX != 0 || this.mVelocityY != 0) {
			if(this.mAccelerationX != 0 || this.mAccelerationY != 0) {
				this.mVelocityX += this.mAccelerationX * pSecondsElapsed;
				this.mVelocityY += this.mAccelerationY * pSecondsElapsed;
			}
			this.mX += this.mVelocityX * pSecondsElapsed;
			this.mY += this.mVelocityY * pSecondsElapsed;
			onPositionChanged();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void accelerate(final float pAccelerationX, final float pAccelerationY) {
		this.mAccelerationX += pAccelerationX;
		this.mAccelerationY += pAccelerationY;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
