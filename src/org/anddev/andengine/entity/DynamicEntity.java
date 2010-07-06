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

	protected float mAccelerationX = 0;
	protected float mAccelerationY = 0;
	
	protected float mVelocityX = 0;
	protected float mVelocityY = 0;

	protected float mRotation = 0;

	protected float mScaleX = 1;
	protected float mScaleY = 1;

	protected float mScaleCenterX = 0;
	protected float mScaleCenterY = 0;

	protected float mRotationCenterX = 0;
	protected float mRotationCenterY = 0;

	private boolean mUpdatePhysicsSelf = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicEntity(final float pX, final float pY) {
		super(pX, pY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setPosition(final StaticEntity pOtherStaticEntity) {
		this.setPosition(pOtherStaticEntity.getX(), pOtherStaticEntity.getY());
	}

	public void setPosition(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;
		this.onPositionChanged();
	}

	public void setBasePosition() {
		this.mX = this.mBaseX;
		this.mY = this.mBaseY;
		this.onPositionChanged();
	}

	public float getVelocityX() {
		return this.mVelocityX;
	}

	public float getVelocityY() {
		return this.mVelocityY;
	}

	public void setVelocity(final float pVelocity) {
		this.mVelocityX = pVelocity;
		this.mVelocityY = pVelocity;
	}

	public void setVelocity(final float pVelocityX, final float pVelocityY) {
		this.mVelocityX = pVelocityX;
		this.mVelocityY = pVelocityY;
	}

	public float getScaleX() {
		return this.mScaleX;
	}

	public float getScaleY() {
		return this.mScaleY;
	}

	public void setScaleX(final float pScaleX) {
		this.mScaleX = pScaleX;
	}

	public void setScaleY(final float pScaleY) {
		this.mScaleY = pScaleY;
	}

	public void setScale(final float pScale) {
		this.mScaleX = pScale;
		this.mScaleY = pScale;
	}

	public void setScale(final float pScaleX, final float pScaleY) {
		this.mScaleX = pScaleX;
		this.mScaleY = pScaleY;
	}
	
	public float getScaleCenterX() {
		return this.mScaleCenterX;
	}

	public float getScaleCenterY() {
		return this.mScaleCenterY;
	}

	public void setScaleCenterX(final float pScaleCenterX) {
		this.mScaleCenterX = pScaleCenterX;
	}

	public void setScaleCenterY(final float pScaleCenterY) {
		this.mScaleCenterY = pScaleCenterY;
	}
	
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
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

	public float getRotation() {
		return this.mRotation;
	}

	public void setRotation(final float pRotation) {
		this.mRotation = pRotation;
	}

	public float getRotationCenterX() {
		return this.mRotationCenterX;
	}

	public float getRotationCenterY() {
		return this.mRotationCenterY;
	}

	public void setRotationCenterX(final float pRotationCenterX) {
		this.mRotationCenterX = pRotationCenterX;
	}

	public void setRotationCenterY(final float pRotationCenterY) {
		this.mRotationCenterY = pRotationCenterY;
	}
	
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;
	}

	public boolean isUpdatePhysicsSelf() {
		return this.mUpdatePhysicsSelf;
	}

	public void setUpdatePhysicsSelf(final boolean pUpdatePhysicsSelf) {
		this.mUpdatePhysicsSelf = pUpdatePhysicsSelf;
	}

	// ===========================================================
		// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mUpdatePhysicsSelf) {
			final float accelerationX = this.mAccelerationX;
			final float accelerationY = this.mAccelerationY;
			if(accelerationX != 0 || accelerationY != 0) {
				this.mVelocityX += accelerationX * pSecondsElapsed;
				this.mVelocityY += accelerationY * pSecondsElapsed;
			}
			final float velocityX = this.mVelocityX;
			final float velocityY = this.mVelocityY;
			if(velocityX != 0 || velocityY != 0) {
				this.mX += velocityX * pSecondsElapsed;
				this.mY += velocityY * pSecondsElapsed;
				this.onPositionChanged();
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void reset() {
		super.reset();
		this.mX = this.mBaseX;
		this.mY = this.mBaseY;
		this.mAccelerationX = 0;
		this.mAccelerationY = 0;
		this.mVelocityX = 0;
		this.mVelocityY = 0;
		this.mRotation = 0;
		
		this.onPositionChanged();
	}

	public void accelerate(final float pAccelerationX, final float pAccelerationY) {
		this.mAccelerationX += pAccelerationX;
		this.mAccelerationY += pAccelerationY;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
