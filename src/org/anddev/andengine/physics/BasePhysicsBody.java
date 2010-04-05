package org.anddev.andengine.physics;

import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 19:58:01 - 20.03.2010
 */
public abstract class BasePhysicsBody {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final float mMass;
	public final float mFricition;
	public final float mElasticity;
	public final PhysicsShape mPhysicsShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BasePhysicsBody(final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape) {
		this.mMass = pMass;
		this.mFricition = pFricition;
		this.mElasticity = pElasticity;
		this.mPhysicsShape = pPhysicsShape;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public abstract Shape getShape();

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
