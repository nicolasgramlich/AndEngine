package org.anddev.andengine.entity;

/**
 * @author Nicolas Gramlich
 * @since 13:40:17 - 07.07.2010
 */
public interface IBaseEntity extends IEntity {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getRed();
	public float getGreen();
	public float getBlue();
	public float getAlpha();
	public void setAlpha(final float pAlpha);

	public void setColor(final float pRed, final float pGreen, final float pBlue);
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha);
	
	public abstract float getX();
	public abstract float getY();

	public abstract float getBaseX();
	public abstract float getBaseY();

	public abstract float[] getSceneCenterCoordinates();

	public abstract void setBasePosition();
	public abstract void setPosition(final IBaseEntity pOtherBaseEntity);
	public abstract void setPosition(final float pX, final float pY);

	public abstract float getVelocityX();
	public abstract float getVelocityY();
	public abstract void setVelocity(final float pVelocity);
	public abstract void setVelocity(final float pVelocityX, final float pVelocityY);

	public abstract float getAccelerationX();
	public abstract float getAccelerationY();
	public abstract void accelerate(final float pAccelerationX, final float pAccelerationY);
	public abstract void setAcceleration(final float pAccelerationX, final float pAccelerationY);

	public abstract float getRotation();
	public abstract void setRotation(final float pRotation);

	public abstract float getAngularVelocity();
	public abstract void setAngularVelocity(final float pAngularVelocity);
	
	public abstract float getRotationCenterX();
	public abstract float getRotationCenterY();
	public abstract void setRotationCenterX(final float pRotationCenterX);
	public abstract void setRotationCenterY(final float pRotationCenterY);
	public abstract void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY);

	public abstract float getScaleX();
	public abstract float getScaleY();
	public abstract void setScaleX(final float pScaleX);
	public abstract void setScaleY(final float pScaleY);
	public abstract void setScale(final float pScale);
	public abstract void setScale(final float pScaleX, final float pScaleY);

	public abstract float getScaleCenterX();
	public abstract float getScaleCenterY();
	public abstract void setScaleCenterX(final float pScaleCenterX);
	public abstract void setScaleCenterY(final float pScaleCenterY);
	public abstract void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY);

	public abstract boolean isUpdatePhysics();
	public abstract void setUpdatePhysics(final boolean pUpdatePhysics);

}