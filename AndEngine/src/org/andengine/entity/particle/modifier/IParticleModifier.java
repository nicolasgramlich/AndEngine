package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:06:05 - 14.03.2010
 */
public interface IParticleModifier<T extends IEntity> extends IParticleInitializer<T> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateParticle(final Particle<T> pParticle);
}
