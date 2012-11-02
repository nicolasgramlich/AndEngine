package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;

/**
 * An {@link IParticleInitializer} that initializes each particle with a specific
 * rotation or a random rotation within a specific range.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:17:42 - 29.06.2010
 */
public class RotationParticleInitializer<T extends IEntity> extends BaseSingleValueParticleInitializer<T> {
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
	 * Creates a new {@code RotationParticleInitializer} that sets the rotation of each
	 * {@link Particle} to a specific value.
	 * 
	 * @param pAlpha The rotation value (0.0 to 360.0) for each particle.
	 */
	public RotationParticleInitializer(final float pRotation) {
		this(pRotation, pRotation);
	}

	/**
	 * Creates a new {@code RotationParticleInitializer} that sets the rotation of each
	 * {@link Particle} to a random value within the given range.
	 * 
	 * @param pMinRotation The minimum rotation (0.0 to 360.0) for each particle.
	 * @param pMaxRotation The maximum rotation (0.0 to 360.0) for each particle.
	 */
	public RotationParticleInitializer(final float pMinRotation, final float pMaxRotation) {
		super(pMinRotation, pMaxRotation);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinRotation() {
		return this.mMinValue;
	}

	public float getMaxRotation() {
		return this.mMaxValue;
	}

	public void setRotation(final float pRotation) {
		this.mMinValue = pRotation;
		this.mMaxValue = pRotation;
	}

	public void setRotation(final float pMinRotation, final float pMaxRotation) {
		this.mMinValue = pMinRotation;
		this.mMaxValue = pMaxRotation;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Will be called by the engine to set the rotation of a new particle.
	 */
	@Override
	public void onInitializeParticle(final Particle<T> pParticle, final float pRotation) {
		pParticle.getEntity().setRotation(pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
