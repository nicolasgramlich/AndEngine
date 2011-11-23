package org.andengine.entity.particle.modifier;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:36:18 - 29.06.2010
 */
public class RotationModifier<T extends Entity> extends BaseSingleValueSpanModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotationModifier(final float pFromRotation, final float pToRotation, final float pFromTime, final float pToTime) {
		super(pFromRotation, pToRotation, pFromTime, pToTime);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final Particle<T> pParticle, final float pRotation) {
		pParticle.getEntity().setRotation(pRotation);
	}

	@Override
	protected void onSetValue(final Particle<T> pParticle, final float pRotation) {
		pParticle.getEntity().setRotation(pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
