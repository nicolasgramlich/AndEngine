package org.anddev.andengine.entity.handler.collision;

import org.anddev.andengine.entity.StaticEntity;

/**
 * @author Nicolas Gramlich
 * @since 12:05:39 - 11.03.2010
 */
public interface ICollisionCallback {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void onCollision(final StaticEntity pCheckEntity, final StaticEntity pTargetStaticEntity);
}
