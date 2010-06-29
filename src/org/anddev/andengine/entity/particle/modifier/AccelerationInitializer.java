package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AccelerationInitializer extends BasePairInitializer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AccelerationInitializer(final float pAcceleration) {
		this(pAcceleration, pAcceleration);
	}

	public AccelerationInitializer(final float pAccelerationX, final float pAccelerationY) {
		this(pAccelerationX, pAccelerationX, pAccelerationY, pAccelerationY);
	}

	public AccelerationInitializer(final float pMinAccelerationX, final float pMaxAccelerationX, final float pMinAccelerationY, final float pMaxAccelerationY) {
		super(pMinAccelerationX, pMaxAccelerationX, pMinAccelerationY, pMaxAccelerationY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle pParticle, final float pValueA, final float pValueB) {
		pParticle.accelerate(pValueA, pValueB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
