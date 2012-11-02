package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * An {@link IParticleModifier} that rotates each particle during its lifetime.
 * See the documentation of the {@link IParticleModifier} for usage hints.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @see IParticleModifier
 * 
 * @author Nicolas Gramlich
 * @since 10:36:18 - 29.06.2010
 */
public class RotationParticleModifier<T extends IEntity> extends BaseSingleValueSpanParticleModifier<T> {
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
	 * Creates a new {@code RotationParticleModifier} that rotates a particle during its
	 * lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the rotation value will be done linear.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromScale Rotation angle at {@code pFromTime} in degrees.
	 * @param pToScale Rotation angle at {@code pToTime} in degrees.
	 */
	public RotationParticleModifier(final float pFromTime, final float pToTime, final float pFromRotation, final float pToRotation) {
		this(pFromTime, pToTime, pFromRotation, pToRotation, EaseLinear.getInstance());
	}

	/**
	 * Creates a new {@code RotationParticleModifier} that rotates a particle during its
	 * lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the rotation value will be done linear.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromScale Rotation angle at {@code pFromTime} in degrees.
	 * @param pToScale Rotation angle at {@code pToTime} in degrees.
	 * @param pEaseFunction The {@link IEaseFunction} to use for interpolation of the degree.
	 */
	public RotationParticleModifier(final float pFromTime, final float pToTime, final float pFromRotation, final float pToRotation, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromRotation, pToRotation, pEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetInitialValue(final Particle<T> pParticle, final float pRotation) {
		pParticle.getEntity().setRotation(pRotation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetValue(final Particle<T> pParticle, final float pPercentageDone, final float pRotation) {
		pParticle.getEntity().setRotation(pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
