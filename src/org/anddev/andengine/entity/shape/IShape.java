package org.anddev.andengine.entity.shape;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;

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

	public float getVelocityX(); // TODO The Physics stuff should be an IUpdateHandler not a part of the Shape class
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

	public float getAngularVelocity();
	public void setAngularVelocity(final float pAngularVelocity);

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

	public boolean collidesWith(final IShape pOtherShape);

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction);
}