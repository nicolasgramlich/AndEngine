package org.anddev.andengine.entity;


/**
 * @author Nicolas Gramlich
 * @since 14:22:22 - 10.03.2010
 */
public abstract class BaseDynamicEntity extends BaseStaticEntity implements IDynamicEntity {
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

	protected float mAngularVelocity = 0;

	protected float mRotationCenterX = 0;
	protected float mRotationCenterY = 0;

	protected float mScaleX = 1;
	protected float mScaleY = 1;

	protected float mScaleCenterX = 0;
	protected float mScaleCenterY = 0;

	private boolean mUpdatePhysics = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDynamicEntity(final float pX, final float pY) {
		super(pX, pY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public void setPosition(final IStaticEntity pOtherStaticEntity) {
		this.setPosition(pOtherStaticEntity.getX(), pOtherStaticEntity.getY());
	}

	@Override
	public void setPosition(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;
		this.onPositionChanged();
	}

	@Override
	public void setBasePosition() {
		this.mX = this.mBaseX;
		this.mY = this.mBaseY;
		this.onPositionChanged();
	}

	@Override
	public float getVelocityX() {
		return this.mVelocityX;
	}

	@Override
	public float getVelocityY() {
		return this.mVelocityY;
	}

	@Override
	public void setVelocity(final float pVelocity) {
		this.mVelocityX = pVelocity;
		this.mVelocityY = pVelocity;
	}

	@Override
	public void setVelocity(final float pVelocityX, final float pVelocityY) {
		this.mVelocityX = pVelocityX;
		this.mVelocityY = pVelocityY;
	}

	@Override
	public float getAccelerationX() {
		return this.mAccelerationX;
	}

	@Override
	public float getAccelerationY() {
		return this.mAccelerationY;
	}

	@Override
	public void accelerate(final float pAccelerationX, final float pAccelerationY) {
		this.mAccelerationX += pAccelerationX;
		this.mAccelerationY += pAccelerationY;
	}

	@Override
	public void setAcceleration(final float pAccelerationX, final float pAccelerationY) {
		this.mAccelerationX = pAccelerationX;
		this.mAccelerationY = pAccelerationY;
	}

	@Override
	public float getRotation() {
		return this.mRotation;
	}

	@Override
	public void setRotation(final float pRotation) {
		this.mRotation = pRotation;
	}
	
	@Override
	public float getAngularVelocity() {
		return this.mAngularVelocity;
	}
	
	@Override
	public void setAngularVelocity(final float pAngularVelocity) {
		this.mAngularVelocity = pAngularVelocity;
	}

	@Override
	public float getRotationCenterX() {
		return this.mRotationCenterX;
	}

	@Override
	public float getRotationCenterY() {
		return this.mRotationCenterY;
	}

	@Override
	public void setRotationCenterX(final float pRotationCenterX) {
		this.mRotationCenterX = pRotationCenterX;
	}

	@Override
	public void setRotationCenterY(final float pRotationCenterY) {
		this.mRotationCenterY = pRotationCenterY;
	}

	@Override
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;
	}

	@Override
	public float getScaleX() {
		return this.mScaleX;
	}

	@Override
	public float getScaleY() {
		return this.mScaleY;
	}

	@Override
	public void setScaleX(final float pScaleX) {
		this.mScaleX = pScaleX;
	}

	@Override
	public void setScaleY(final float pScaleY) {
		this.mScaleY = pScaleY;
	}

	@Override
	public void setScale(final float pScale) {
		this.mScaleX = pScale;
		this.mScaleY = pScale;
	}

	@Override
	public void setScale(final float pScaleX, final float pScaleY) {
		this.mScaleX = pScaleX;
		this.mScaleY = pScaleY;
	}

	@Override
	public float getScaleCenterX() {
		return this.mScaleCenterX;
	}

	@Override
	public float getScaleCenterY() {
		return this.mScaleCenterY;
	}

	@Override
	public void setScaleCenterX(final float pScaleCenterX) {
		this.mScaleCenterX = pScaleCenterX;
	}

	@Override
	public void setScaleCenterY(final float pScaleCenterY) {
		this.mScaleCenterY = pScaleCenterY;
	}

	@Override
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	@Override
	public boolean isUpdatePhysics() {
		return this.mUpdatePhysics;
	}

	@Override
	public void setUpdatePhysics(final boolean pUpdatePhysicsSelf) {
		this.mUpdatePhysics = pUpdatePhysicsSelf;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mUpdatePhysics) {
			/* Apply linear acceleration. */
			final float accelerationX = this.mAccelerationX;
			final float accelerationY = this.mAccelerationY;
			if(accelerationX != 0 || accelerationY != 0) {
				this.mVelocityX += accelerationX * pSecondsElapsed;
				this.mVelocityY += accelerationY * pSecondsElapsed;
			}

			/* Apply angular velocity. */		
			final float angularVelocity = this.mAngularVelocity;
			if(angularVelocity != 0) {
				this.mRotation += angularVelocity * pSecondsElapsed;
			}

			/* Apply linear velocity. */			
			final float velocityX = this.mVelocityX;
			final float velocityY = this.mVelocityY;
			if(velocityX != 0 || velocityY != 0) {
				this.mX += velocityX * pSecondsElapsed;
				this.mY += velocityY * pSecondsElapsed;
				this.onPositionChanged();
			}
		}
	}

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

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
