package org.anddev.andengine.physics;

import org.anddev.andengine.entity.IUpdateHandler;

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

	public void addStaticBody(final StaticPhysicsBody pStaticPhysicsBody);

	public void addDynamicBody(final DynamicPhysicsBody pDynamicPhysicsBody);

	public void setGravity(final float pGravityX, final float pGravityY);

	public void setVelocity(final DynamicPhysicsBody pDynamicPhysicsBody, final float pVelocityX, final float pVelocityY);
}
