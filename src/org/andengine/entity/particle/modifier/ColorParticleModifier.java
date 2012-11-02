package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * An {@link IParticleModifier} that changes the color of each particle
 * during their lifetime.
 * See the documentation of the {@link IParticleModifier} for usage hints.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @see IParticleModifier
 * 
 * @author Nicolas Gramlich
 * @since 15:22:26 - 29.06.2010
 */
public class ColorParticleModifier<T extends IEntity> extends BaseTripleValueSpanParticleModifier<T> {
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
	 * Creates a new {@code ColorParticleModifier} that changes the color of particles
	 * during their lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the rotation value will be done linear.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromRed Colors red component at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToRed Colors red component at {@code pToTime}. (0.0 to 1.0)
	 * @param pFromGreen Colors green component at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToGreen Colors green component at {@code pToTime}. (0.0 to 1.0)
	 * @param pFromBlue Colors blue component at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToBlue Colors blue component at {@code pToTime}. (0.0 to 1.0)
	 */
	public ColorParticleModifier(final float pFromTime, final float pToTime, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue) {
		this(pFromTime, pToTime, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, EaseLinear.getInstance());
	}

	/**
	 * Creates a new {@code ColorParticleModifier} that changes the color of particles
	 * during their lifetime. You can set a start end end time of the transformation. The 
	 * interpolation of the scale value is controlled by the specified {@link IEaseFunction}.
	 * 
	 * @param pFromTime Start time of transformation in seconds (0.0s = particle birth)
	 * @param pToTime End time of transformation in seconds (0.0s = particle birth)
	 * @param pFromRed Colors red component at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToRed Colors red component at {@code pToTime}. (0.0 to 1.0)
	 * @param pFromGreen Colors green component at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToGreen Colors green component at {@code pToTime}. (0.0 to 1.0)
	 * @param pFromBlue Colors blue component at {@code pFromTime}. (0.0 to 1.0)
	 * @param pToBlue Colors blue component at {@code pToTime}. (0.0 to 1.0)
	 * @param pEaseFunction The {@link IEaseFunction} to use for interpolation of the values.
	 */
	public ColorParticleModifier(final float pFromTime, final float pToTime, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, pEaseFunction);
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
	protected void onSetInitialValues(final Particle<T> pParticle, final float pRed, final float pGreen, final float pBlue) {
		pParticle.getEntity().setColor(pRed, pGreen, pBlue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetValues(final Particle<T> pParticle, final float pPercentageDone, final float pRed, final float pGreen, final float pBlue) {
		pParticle.getEntity().setColor(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
