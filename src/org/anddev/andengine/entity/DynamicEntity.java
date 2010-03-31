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

	public DynamicEntity(final float pX, final float pY, final float pWidth, final float pHeight) {
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

	public float getScale() {
		return this.mScale;
	}

	public void setScale(final float pScale) {
		this.mScale = pScale;
	}

	@Override
	public float getWidth() {
		return super.getWidth() * this.mScale;
	}

	@Override
	public float getHeight() {
		return super.getHeight() * this.mScale;
	}

	public void setWidth(final int pWidth) {
		this.mWidth = pWidth;
		this.onPositionChanged();
	}

	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.onPositionChanged();
	}

	public void setPosition(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;
	}

	public void setPosition(final StaticEntity pOtherStaticEntity) {
		this.mX = pOtherStaticEntity.getX();
		this.mY = pOtherStaticEntity.getY();
	}

	public void setInitialPosition() {
		this.mX = this.mInitialX;
		this.mY = this.mInitialY;
		this.onPositionChanged();
	}

	public void setInitialSize() {
		this.mWidth = this.mInitialWidth;
		this.mHeight = this.mInitialHeight;
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
		this.setInitialPosition();
		this.setInitialSize();
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
