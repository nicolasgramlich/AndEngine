package org.anddev.andengine.physics;

import org.anddev.andengine.entity.shape.Shape;

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

	private final Shape mShape;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StaticPhysicsBody(final Shape pShape, final float pMass, final float pFricition, final float pElasticity, final PhysicsShape pPhysicsShape) {
		super(pMass, pFricition, pElasticity, pPhysicsShape);
		this.mShape = pShape;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
