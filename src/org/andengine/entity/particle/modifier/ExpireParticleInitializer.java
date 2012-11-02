package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.util.math.MathUtils;

/**
 * An {@link IParticleInitializer} that thats the lifetime of a new particle
 * within a specific range or to a specific value. Each particle will be deleted
 * after it has expired its lifetime.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class ExpireParticleInitializer<T extends IEntity> implements IParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mMinLifeTime;
	private float mMaxLifeTime;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Create a new {@code ExpireParticleInitializer} that sets a fixed lifetime
	 * for every particle.
	 * 
	 * @param pLifeTime The lifetime of a particle in seconds.
	 */
	public ExpireParticleInitializer(final float pLifeTime) {
		this(pLifeTime, pLifeTime);
	}

	/**
	 * Create a new {@code ExpireParticleInitializer} that sets a random lifetime
	 * inside a given range for each particle.
	 * 
	 * @param pMinLifeTime The minimum lifetime a particle should have in seconds.
	 * @param pMaxLifeTime The maximum lifetime a particle should have in seconds.
	 */
	public ExpireParticleInitializer(final float pMinLifeTime, final float pMaxLifeTime) {
		this.mMinLifeTime = pMinLifeTime;
		this.mMaxLifeTime = pMaxLifeTime;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinLifeTime() {
		return this.mMinLifeTime;
	}

	public float getMaxLifeTime() {
		return this.mMaxLifeTime;
	}

	public void setLifeTime(final float pLifeTime) {
		this.mMinLifeTime = pLifeTime;
		this.mMaxLifeTime = pLifeTime;
	}

	public void setLifeTime(final float pMinLifeTime, final float pMaxLifeTime) {
		this.mMinLifeTime = pMinLifeTime;
		this.mMaxLifeTime = pMaxLifeTime;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInitializeParticle(final Particle<T> pParticle) {
		pParticle.setExpireTime((MathUtils.RANDOM.nextFloat() * (this.mMaxLifeTime - this.mMinLifeTime) + this.mMinLifeTime));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
