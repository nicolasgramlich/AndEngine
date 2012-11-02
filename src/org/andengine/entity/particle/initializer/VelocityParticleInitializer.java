package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;

/**
 * An {@link IParticleInitializer} that sets the velocity of each new particle to
 * a specific value or a random value within a given range.
 * <p>
 * If you want your particles to move away in every direction from their starting 
 * point, you can use this initializer:
 * <p>
 * {@code
 * new VelocityParticleInitializer(-10, 10, -10, 10);
 * }
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class VelocityParticleInitializer<T extends IEntity> extends BaseDoubleValueParticleInitializer<T> {
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
	 * Create a new {@code VelocityParticleInitializer} that sets the x-velocity
	 * and y-velocity of each particle to the specific value. So the particle
	 * will move diagonal.
	 * 
	 * @param pVelocity The x- and y-velocity to set.
	 */
	public VelocityParticleInitializer(final float pVelocity) {
		this(pVelocity, pVelocity, pVelocity, pVelocity);
	}

	/**
	 * Create a new {@code VelocityParticleInitializer} that sets the x-velocity
	 * and the y-velocity of each particle to a specific value.
	 * 
	 * @param pVelocityX The x-velocity of each particle.
	 * @param pVelocityY The y-velocity of each particle.
	 */
	public VelocityParticleInitializer(final float pVelocityX, final float pVelocityY) {
		this(pVelocityX, pVelocityX, pVelocityY, pVelocityY);
	}

	/**
	 * Create a new {@code VelocityParticleInitializer} that sets the x-velocity
	 * and y-velocity of each particle to a random value inside the specific range.
	 * 
	 * @param pMinVelocityX The minimum x-velocity.
	 * @param pMaxVelocityX The maximum x-velocity.
	 * @param pMinVelocityY The minimum y-velocity.
	 * @param pMaxVelocityY The maximum y-velocity.
	 */
	public VelocityParticleInitializer(final float pMinVelocityX, final float pMaxVelocityX, final float pMinVelocityY, final float pMaxVelocityY) {
		super(pMinVelocityX, pMaxVelocityX, pMinVelocityY, pMaxVelocityY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinVelocityX() {
		return this.mMinValue;
	}

	public float getMaxVelocityX() {
		return this.mMaxValue;
	}

	public float getMinVelocityY() {
		return this.mMinValueB;
	}

	public float getMaxVelocityY() {
		return this.mMaxValueB;
	}

	public void setVelocityX(final float pVelocityX) {
		this.mMinValue = pVelocityX;
		this.mMaxValue = pVelocityX;
	}

	public void setVelocityY(final float pVelocityY) {
		this.mMinValueB = pVelocityY;
		this.mMaxValueB = pVelocityY;
	}

	public void setVelocityX(final float pMinVelocityX, final float pMaxVelocityX) {
		this.mMinValue = pMinVelocityX;
		this.mMaxValue = pMaxVelocityX;
	}

	public void setVelocityY(final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinValueB = pMinVelocityY;
		this.mMaxValueB = pMaxVelocityY;
	}

	public void setVelocity(final float pMinVelocityX, final float pMaxVelocityX, final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinValue = pMinVelocityX;
		this.mMaxValue = pMaxVelocityX;
		this.mMinValueB = pMinVelocityY;
		this.mMaxValueB = pMaxVelocityY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Will be called by the engine to set a velocity for a new particle.
	 */
	@Override
	public void onInitializeParticle(final Particle<T> pParticle, final float pVelocityX, final float pVelocityY) {
		pParticle.getPhysicsHandler().setVelocity(pVelocityX, pVelocityY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
