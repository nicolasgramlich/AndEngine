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
	
	/**
	 * @param pCheckEntity
	 * @param pTargetStaticEntity
	 * @return <code>true</code> to proceed, <code>false</code> to stop further collosion-checks.
	 */
	public boolean onCollision(final StaticEntity pCheckEntity, final StaticEntity pTargetStaticEntity);
}
