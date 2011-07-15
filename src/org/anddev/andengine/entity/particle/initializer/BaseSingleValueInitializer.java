package org.anddev.andengine.entity.particle.initializer;

import static org.anddev.andengine.util.MathUtils.RANDOM;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:18:06 - 29.06.2010
 */
public abstract class BaseSingleValueInitializer implements IParticleInitializer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mMinValue;
	protected float mMaxValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueInitializer(final float pMinValue, final float pMaxValue) {
		this.mMinValue = pMinValue;
		this.mMaxValue = pMaxValue;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInitializeParticle(final Particle pParticle, final float pValue);

	@Override
	public final void onInitializeParticle(final Particle pParticle) {
		this.onInitializeParticle(pParticle, this.getRandomValue());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private final float getRandomValue() {
		if(this.mMinValue == this.mMaxValue) {
			return this.mMaxValue;
		} else {
			return RANDOM.nextFloat() * (this.mMaxValue - this.mMinValue) + this.mMinValue;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
