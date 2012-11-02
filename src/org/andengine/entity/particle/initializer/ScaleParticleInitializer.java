package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;


/**
 * An {@link IParticleInitializer} that sets the scale of each particle to a
 * specific value or a random value inside a specific range.
 * <p>
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:03:29 - 19.11.2011
 */
public class ScaleParticleInitializer<T extends IEntity> extends BaseSingleValueParticleInitializer<T> {
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
	 * Create a new {@code ScaleParticleInitializer} that scales each particle
	 * to the given scale.
	 * 
	 * @param pScale How much to scale each particle. (1.0 equals 100% size)
	 */
	public ScaleParticleInitializer(final float pScale) {
		super(pScale, pScale);
	}

	/**
	 * Create a new {@code ScaleParticleInitializer} that scales each particle
	 * to a random scale inside the given range.
	 * 
	 * @param pMinScale The minimum scale for each particle. (1.0 equals 100% size)
	 * @param pMaxScale The maximum scale for each particle. (1.0 equals 100% size)
	 */
	public ScaleParticleInitializer(final float pMinScale, final float pMaxScale) {
		super(pMinScale, pMaxScale);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Will be called by the engine to scale a new particle.
	 */
	@Override
	protected void onInitializeParticle(final Particle<T> pParticle, final float pScale) {
		pParticle.getEntity().setScale(pScale);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
