package org.anddev.andengine.entity.shape;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.util.modifier.IModifier;

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

	public float getX();
	public float getY();

	public float getBaseX();
	public float getBaseY();

	public float[] getSceneCenterCoordinates();

	public void setBasePosition();
	public void setPosition(final IShape pOtherShape);
	public void setPosition(final float pX, final float pY);

	public float getVelocityX();
	public float getVelocityY();
	public void setVelocityX(final float pVelocityX);
	public void setVelocityY(final float pVelocityY);
	public void setVelocity(final float pVelocity);
	public void setVelocity(final float pVelocityX, final float pVelocityY);

	public float getAccelerationX();
	public float getAccelerationY();
	public void setAccelerationX(final float pAccelerationX);
	public void setAccelerationY(final float pAccelerationY);
	public void setAcceleration(final float pAcceleration);
	public void setAcceleration(final float pAccelerationX, final float pAccelerationY);
	public void accelerate(final float pAccelerationX, final float pAccelerationY);

	public float getRotation();
	public void setRotation(final float pRotation);

	public float getAngularVelocity();
	public void setAngularVelocity(final float pAngularVelocity);

	public float getRotationCenterX();
	public float getRotationCenterY();
	public void setRotationCenterX(final float pRotationCenterX);
	public void setRotationCenterY(final float pRotationCenterY);
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY);

	public boolean isScaled();
	public float getScaleX();
	public float getScaleY();
	public void setScaleX(final float pScaleX);
	public void setScaleY(final float pScaleY);
	public void setScale(final float pScale);
	public void setScale(final float pScaleX, final float pScaleY);

	public float getScaleCenterX();
	public float getScaleCenterY();
	public void setScaleCenterX(final float pScaleCenterX);
	public void setScaleCenterY(final float pScaleCenterY);
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY);

	public boolean isUpdatePhysics();
	public void setUpdatePhysics(final boolean pUpdatePhysics);

	public boolean isCullingEnabled();
	public void setCullingEnabled(final boolean pCullingEnabled);

	public float getWidth();
	public float getHeight();

	public float getBaseWidth();
	public float getBaseHeight();

	public float getWidthScaled();
	public float getHeightScaled();

	public void addShapeModifier(final IModifier<IShape> pShapeModifier);
	public boolean removeShapeModifier(final IModifier<IShape> pShapeModifier);
	public void clearShapeModifiers();

	public boolean collidesWith(final IShape pOtherShape);

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction);
}