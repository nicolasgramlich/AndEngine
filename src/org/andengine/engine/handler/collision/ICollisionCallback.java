package org.andengine.engine.handler.collision;

import org.andengine.entity.shape.IShape;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:05:39 - 11.03.2010
 */
public interface ICollisionCallback {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pCheckShape
	 * @param pTargetShape
	 * @return <code>true</code> to proceed, <code>false</code> to stop further collosion-checks.
	 */
	public boolean onCollision(final IShape pCheckShape, final IShape pTargetShape);
}
