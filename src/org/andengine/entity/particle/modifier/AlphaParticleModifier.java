package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * An {@link IParticleModifier} that changes the alpha value of each particle during
 * its lifetime.
 * See the documentation of the {@link IParticleModifier} for usage hints.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @see IParticleModifier
 * 
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AlphaParticleModifier<T extends IEntity> extends BaseSingleValueSpanParticleModifier<T> {
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
	 * Creates a new {@code AlphaParticleModifier} that changes alpha value of a 
	 * particle during its lifetime. You can set a start end end time of the 
	 * transformation. The interpolation of the alpha value will be done linear.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromAlpha Alpha value at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToAlpha Alpha value at {@code pToTime}. (0.0 to 1.0)
	 */
	public AlphaParticleModifier(final float pFromTime, final float pToTime, final float pFromAlpha, final float pToAlpha) {
		this(pFromTime, pToTime, pFromAlpha, pToAlpha, EaseLinear.getInstance());
	}

	/**
	 * Creates a new {@code AlphaParticleModifier} that changes alpha value of a 
	 * particle during its lifetime. You can set a start end end time of the 
	 * transformation. The interpolation of the scale value is controlled by the 
	 * specified {@link IEaseFunction}.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromAlpha Alpha value at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToAlpha Alpha value at {@code pToTime}. (0.0 to 1.0)
	 * @param pEaseFunction The {@link IEaseFunction} to use for interpolation of the alpha value.
	 */
	public AlphaParticleModifier(final float pFromTime, final float pToTime, final float pFromAlpha, final float pToAlpha, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromAlpha, pToAlpha, pEaseFunction);
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
	protected void onSetInitialValue(final Particle<T> pParticle, final float pAlpha) {
		pParticle.getEntity().setAlpha(pAlpha);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	protected void onSetValue(final Particle<T> pParticle, final float pPercentageDone, final float pAlpha) {
		pParticle.getEntity().setAlpha(pAlpha);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
