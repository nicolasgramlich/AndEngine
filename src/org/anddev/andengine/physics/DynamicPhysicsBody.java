package org.anddev.andengine.physics;

import org.anddev.andengine.entity.handler.collision.ICollisionCallback;
import org.anddev.andengine.entity.shape.Shape;

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

	private final Shape mShape;
	private final ICollisionCallback mCollisionCallback;
	private final boolean mFixedRotation;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicPhysicsBody(final Shape pShape, final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape, final boolean pFixedRotation) {
		this(pShape, pMass, pFricition, pElasticity, pPhysicsShape, pFixedRotation, null);
	}

	public DynamicPhysicsBody(final Shape pShape, final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape, final boolean pFixedRotation, final ICollisionCallback pCollisionCallback) {
		super(pMass, pFricition, pElasticity, pPhysicsShape);
		this.mShape = pShape;
		this.mFixedRotation = pFixedRotation;
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

	public boolean isFixedRotation() {
		return this.mFixedRotation;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Shape getShape() {
		return this.mShape;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
