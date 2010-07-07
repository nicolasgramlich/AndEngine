package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 10:12:09 - 29.06.2010
 */
public interface IParticleInitializer {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onInitializeParticle(final Particle pParticle);
}
