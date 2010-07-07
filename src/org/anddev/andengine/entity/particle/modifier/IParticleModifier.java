package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 20:06:05 - 14.03.2010
 */
public interface IParticleModifier extends IParticleInitializer {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateParticle(final Particle pParticle);
}
