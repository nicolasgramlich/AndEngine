package org.anddev.andengine.entity.shape;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier;

/**
 * @author Nicolas Gramlich
 * @since 13:32:52 - 07.07.2010
 */
public interface IShape extends IEntity, ITouchArea {
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
	public abstract void setPosition(final IShape pOtherShape);
	public abstract void setPosition(final float pX, final float pY);

	public abstract float getVelocityX();
	public abstract float getVelocityY();
	public abstract void setVelocityX(final float pVelocityX);
	public abstract void setVelocityY(final float pVelocityY);
	public abstract void setVelocity(final float pVelocity);
	public abstract void setVelocity(final float pVelocityX, final float pVelocityY);

	public abstract float getAccelerationX();
	public abstract float getAccelerationY();
	public abstract void setAccelerationX(final float pAccelerationX);
	public abstract void setAccelerationY(final float pAccelerationY);
	public abstract void setAcceleration(final float pAcceleration);
	public abstract void setAcceleration(final float pAccelerationX, final float pAccelerationY);
	public abstract void accelerate(final float pAccelerationX, final float pAccelerationY);

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

	public float getWidth();
	public float getHeight();

	public float getBaseWidth();
	public float getBaseHeight();

	public float getWidthScaled();
	public float getHeightScaled();

	public void addShapeModifier(final IShapeModifier pShapeModifier);
	public void removeShapeModifier(final IShapeModifier pShapeModifier);

	public boolean collidesWith(final IShape pOtherShape);

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction);
}