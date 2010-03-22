package org.anddev.andengine.physics;

import org.anddev.andengine.entity.StaticEntity;

/**
 * @author Nicolas Gramlich
 * @since 13:40:18 - 22.03.2010
 */
public class StaticPhysicsBody extends BasePhysicsBody {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final StaticEntity mStaticEntity;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StaticPhysicsBody(final StaticEntity pStaticEntity, final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape) {
		super(pMass, pFricition, pElasticity, pPhysicsShape);
		this.mStaticEntity = pStaticEntity;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public StaticEntity getEntity() {
		return this.mStaticEntity;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
