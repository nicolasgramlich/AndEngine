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

	private float mAngle = 0;

	private float mScale = 1;
	private boolean mUpdatePhysicsSelf;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicEntity(final float pX, final float pY) {
		super(pX, pY);
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

	public float getScale() {
		return this.mScale;
	}

	public void setScale(final float pScale) {
		this.mScale = pScale;
	}

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

	public float getAngle() {
		return this.mAngle;
	}

	public void setAngle(final float pAngle) {
		this.mAngle = pAngle;
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
		if(this.isUpdatePhysicsSelf()) {
			if(this.mAccelerationX != 0 || this.mAccelerationY != 0 || this.mVelocityX != 0 || this.mVelocityY != 0) {
				if(this.mAccelerationX != 0 || this.mAccelerationY != 0) {
					this.mVelocityX += this.mAccelerationX * pSecondsElapsed;
					this.mVelocityY += this.mAccelerationY * pSecondsElapsed;
				}
				this.mX += this.mVelocityX * pSecondsElapsed;
				this.mY += this.mVelocityY * pSecondsElapsed;
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
		this.setBasePosition();
		this.mAccelerationX = 0;
		this.mAccelerationY = 0;
		this.mVelocityX = 0;
		this.mVelocityY = 0;
		this.mAngle = 0;
	}

	public void accelerate(final float pAccelerationX, final float pAccelerationY) {
		this.mAccelerationX += pAccelerationX;
		this.mAccelerationY += pAccelerationY;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
