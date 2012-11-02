package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;


/**
 * An {@link IParticleInitializer} that initializes each particle with a specific
 * alpha value or a random alpha value inside a specific range.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:53:41 - 02.10.2010
 */
public class AlphaParticleInitializer<T extends IEntity> extends BaseSingleValueParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Creates a new {@code AlphaParticleInitializer} that sets the alpha of each
	 * {@link Particle} to a specific value.
	 * 
	 * @param pAlpha The alpha value (0.0 to 1.0) for each particle.
	 */
	public AlphaParticleInitializer(final float pAlpha) {
		super(pAlpha, pAlpha);
	}

	/**
	 * Creates a new {@code AlphaParticleInitializer} that sets the alpha of each
	 * {@link Particle} to a random value in the specific range.
	 * 
	 * @param pMinAlpha The minimum alpha value (0.0 to 1.0) for each particle.
	 * @param pMaxAlpha The maximum alpha value (0.0 to 1.0) for each particle.
	 */
	public AlphaParticleInitializer(final float pMinAlpha, final float pMaxAlpha) {
		super(pMinAlpha, pMaxAlpha);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Will be called by the engine, to set the alpha for a new particle.
	 */
	@Override
	protected void onInitializeParticle(final Particle<T> pParticle, final float pAlpha) {
		pParticle.getEntity().setAlpha(pAlpha);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
