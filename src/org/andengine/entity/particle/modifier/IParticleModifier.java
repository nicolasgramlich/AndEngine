package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.initializer.IParticleInitializer;

/**
 * An {@code IParticleModifier} can be used in a {@link ParticleSystem}
 * to modify a {@link Particle} during its lifetime.
 * <p>
 * You can add a particle modifier to the {@code ParticleSystem} by using its
 * {@link ParticleSystem#addParticleModifier(org.andengine.entity.particle.modifier.IParticleModifier)} 
 * method and remove it with {@link ParticleSystem#removeParticleModifier(org.andengine.entity.particle.modifier.IParticleModifier)}.
 * <p>
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

	/**
	 * The implementation of this method should modify the {@link Particle}.
	 * To make time depending modifications use the {@link Particle#getLifeTime()}
	 * method.
	 * 
	 * @param pParticle The {@link Particle} to modify.
	 */
	public void onUpdateParticle(final Particle<T> pParticle);
}
