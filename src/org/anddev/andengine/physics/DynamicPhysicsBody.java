package org.anddev.andengine.physics;

import org.anddev.andengine.entity.DynamicEntity;
import org.anddev.andengine.entity.handler.collision.ICollisionCallback;

/**
 * @author Nicolas Gramlich
 * @since 13:40:18 - 22.03.2010
 */
public class DynamicPhysicsBody extends BasePhysicsBody {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final DynamicEntity mDynamicEntity;
	private final ICollisionCallback mCollisionCallback;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicPhysicsBody(final DynamicEntity pDynamicEntity, final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape) {
		this(pDynamicEntity, pMass, pFricition, pElasticity, pPhysicsShape, null);
	}
	
	public DynamicPhysicsBody(final DynamicEntity pDynamicEntity, final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape, final ICollisionCallback pCollisionCallback) {
		super(pMass, pFricition, pElasticity, pPhysicsShape);
		this.mDynamicEntity = pDynamicEntity;
		this.mCollisionCallback = pCollisionCallback;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public boolean hasCollisionCallback() {
		return this.mCollisionCallback != null;
	}
	
	public ICollisionCallback getCollisionCallback() {
		return this.mCollisionCallback;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public DynamicEntity getEntity() {
		return this.mDynamicEntity;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
