package org.anddev.andengine.entity;


/**
 * @author Nicolas Gramlich
 * @since 14:22:22 - 10.03.2010
 */
public abstract class DynamicEntity extends BaseEntity {
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
	
	private float mAngleClockwise = 0;

	private float mX = 0;
	private float mY = 0;
	
	private float mOffsetX = 0;
	private float mOffsetY = 0;

	private final int mWidth;
	private final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicEntity(final float pX, final float pY, final int pWidth, final int pHeight) {
		this.mX = pX;
		this.mY = pY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
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
	
	public float getOffsetX() {
		return this.mOffsetX;
	}
	
	public float getOffsetY() {
		return this.mOffsetY;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

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
	
	public float getAngleClockwise() {
		return this.mAngleClockwise;
	}
	
	public void setAngleClockwise(final float pAngleClockwise) {
		this.mAngleClockwise = pAngleClockwise;
	}
	
	public void setOffsetX(final float pOffsetX) {
		this.mOffsetX = pOffsetX;
	}
	
	public void setOffsetY(final float pOffsetY) {
		this.mOffsetY = pOffsetY;
	}
	
	public void setOffset(final float pOffsetX, final float pOffsetY) {
		this.mOffsetX = pOffsetX;
		this.mOffsetY = pOffsetY;
		onPositionChanged();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onPositionChanged();

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
