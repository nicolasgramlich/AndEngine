package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:10:16 - 04.05.2010
 */
public abstract class BaseSingleValueSpanParticleModifier<T extends IEntity> implements IParticleModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mFromTime;
	private float mToTime;
	private float mDuration;

	private float mFromValue;
	private float mValueSpan;

	private final IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueSpanParticleModifier(final float pFromTime, final float pToTime, final float pFromValue, final float pToValue) {
		this(pFromTime, pToTime, pFromValue, pToValue, EaseLinear.getInstance());
	}

	public BaseSingleValueSpanParticleModifier(final float pFromTime, final float pToTime, final float pFromValue, final float pToValue, final IEaseFunction pEaseFunction) {
		this.mFromTime = pFromTime;
		this.mToTime = pToTime;
		this.mDuration = pToTime - pFromTime;

		this.mFromValue = pFromValue;
		this.mValueSpan = pToValue - pFromValue;

		this.mEaseFunction = pEaseFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValue(final Particle<T> pParticle, final float pValue);
	protected abstract void onSetValue(final Particle<T> pParticle, final float pPercentageDone, final float pValue);

	@Override
	public void onInitializeParticle(final Particle<T> pParticle) {
		this.onSetInitialValue(pParticle, this.mFromValue);
	}

	@Override
	public void onUpdateParticle(final Particle<T> pParticle) {
		final float lifeTime = pParticle.getLifeTime();
		if(lifeTime > this.mFromTime && lifeTime < this.mToTime) {
			final float percentageDone = this.mEaseFunction.getPercentage((lifeTime - this.mFromTime), this.mDuration);
			this.onSetValue(pParticle, percentageDone, this.mFromValue + percentageDone * this.mValueSpan);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset(final float pFromValue, final float pToValue, final float pFromTime, final float pToTime) {
		this.mFromValue = pFromValue;
		this.mFromTime = pFromTime;
		this.mToTime = pToTime;

		this.mValueSpan = pToValue - pFromValue;
		this.mDuration = pToTime - pFromTime;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
