package org.anddev.andengine.physics;

/**
 * @author Nicolas Gramlich
 * @since 19:58:01 - 20.03.2010
 */
public class PhysicsData {
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

	public PhysicsData(final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape) {
		this.mMass = pMass;
		this.mFricition = pFricition;
		this.mElasticity = pElasticity;
		this.mPhysicsShape = pPhysicsShape;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
