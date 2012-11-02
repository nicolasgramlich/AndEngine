package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;

/**
 * An {@code IParticleInitializer} is used to modify a {@link Particle} when
 * it is created. A newly created {@code Particle} will be passed to all
 * {@code IParticleInitializers}, that has been added to the 
 * {@link org.andengine.entity.particle.ParticleSystem}, that emitted that particle.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @see org.andengine.entity.particle.ParticleSystem#addParticleInitializer(org.andengine.entity.particle.initializer.IParticleInitializer) 
 * 
 * @author Nicolas Gramlich
 * @since 10:12:09 - 29.06.2010
 */
public interface IParticleInitializer<T extends IEntity> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * The engine will call this method, for each created {@link Particle}.
	 * The implementation should apply its modifications to the {@code Particle}.
	 * 
	 * @param pParticle A newly created {@link Particle}.
	 */
	public void onInitializeParticle(final Particle<T> pParticle);
}
