package org.anddev.andengine.physics;

import org.anddev.andengine.entity.DynamicEntity;
import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.StaticEntity;

/**
 * @author Nicolas Gramlich
 * @since 10:48:22 - 21.03.2010
 */
public interface IPhysicsSpace extends IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void createWorld(final float pX, final float pY, final float pWidth, final float pHeight);
	
	public void addStaticEntity(final StaticEntity pStaticEntity, final PhysicsData pPhysicsData);
	
	public void addDynamicEntity(final DynamicEntity pDynamicEntity, final PhysicsData pPhysicsData);

	public void setGravity(final float pGravityX, final float pGravityY);
	
	public void setVelocity(final DynamicEntity pDynamicEntity, final float pVelocityX, final float pVelocityY);
	
	// TODO Collision-Handling/Callbacks
}
