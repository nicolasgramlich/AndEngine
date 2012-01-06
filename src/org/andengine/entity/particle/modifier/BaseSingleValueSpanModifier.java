package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:10:16 - 04.05.2010
 */
public abstract class BaseSingleValueSpanModifier<T extends IEntity> implements IParticleModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mFromValue;
	private float mToValue;

	private float mFromTime;
	private float mToTime;

	private float mDuration;
	private float mSpanValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueSpanModifier(final float pFromValue, final float pToValue, final float pFromTime, final float pToTime) {
		this.mFromValue = pFromValue;
		this.mToValue = pToValue;
		this.mFromTime = pFromTime;
		this.mToTime = pToTime;

		this.mSpanValue = this.mToValue - this.mFromValue;
		this.mDuration = this.mToTime - this.mFromTime;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValue(final Particle<T> pParticle, final float pValue);
	protected abstract void onSetValue(final Particle<T> pParticle, final float pValue);

	@Override
	public void onInitializeParticle(final Particle<T> pParticle) {
		this.onSetInitialValue(pParticle, this.mFromValue);
	}

	@Override
	public void onUpdateParticle(final Particle<T> pParticle) {
		final float lifeTime = pParticle.getLifeTime();
		if(lifeTime > this.mFromTime && lifeTime < this.mToTime) {
			final float percent = (lifeTime - this.mFromTime) / this.mDuration;
			this.onSetValueInternal(pParticle, percent);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onSetValueInternal(final Particle<T> pParticle, final float pPercent) {
		this.onSetValue(pParticle, this.calculateValue(pPercent));
	}

	protected float calculateValue(final float pPercent) {
		return this.mFromValue + this.mSpanValue * pPercent;
	}

	public void reset(final float pFromValue, final float pToValue, final float pFromTime, final float pToTime) {
		this.mFromValue = pFromValue;
		this.mToValue = pToValue;
		this.mFromTime = pFromTime;
		this.mToTime = pToTime;

		this.mSpanValue = this.mToValue - this.mFromValue;
		this.mDuration = this.mToTime - this.mFromTime;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
