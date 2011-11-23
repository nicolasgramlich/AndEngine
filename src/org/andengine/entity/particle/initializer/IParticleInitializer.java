package org.andengine.entity.particle.initializer;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:12:09 - 29.06.2010
 */
public interface IParticleInitializer<T extends Entity> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onInitializeParticle(final Particle<T> pParticle);
}
