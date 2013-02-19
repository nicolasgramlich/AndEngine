package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 10:18:06 - 29.06.2010
 */
public abstract class BaseSingleValueParticleInitializer<T extends IEntity> implements IParticleInitializer<T> {
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

	public BaseSingleValueParticleInitializer(final float pMinValue, final float pMaxValue) {
		this.mMinValue = pMinValue;
		this.mMaxValue = pMaxValue;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInitializeParticle(final Particle<T> pParticle, final float pValue);

	@Override
	public final void onInitializeParticle(final Particle<T> pParticle) {
		this.onInitializeParticle(pParticle, this.getRandomValue());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected float getRandomValue() {
		if (this.mMinValue == this.mMaxValue) {
			return this.mMaxValue;
		} else {
			return MathUtils.random(this.mMinValue, this.mMaxValue);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
