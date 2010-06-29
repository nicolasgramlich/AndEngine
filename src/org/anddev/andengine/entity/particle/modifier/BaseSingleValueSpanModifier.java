package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 16:10:16 - 04.05.2010
 */
public abstract class BaseSingleValueSpanModifier implements IParticleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromValue;
	private final float mToValue;
	private final float mFromTime;
	private final float mToTime;

	private final float mDuration;
	private final float mSpanValue;

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

	protected abstract void onSetInitialValue(final Particle pParticle, final float pValue);
	protected abstract void onSetValue(final Particle pParticle, final float pValue);

	@Override
	public void onInitializeParticle(final Particle pParticle) {
		this.onSetInitialValue(pParticle, this.mFromValue);
	}

	@Override
	public void onUpdateParticle(final Particle pParticle) {
		final float lifeTime = pParticle.getLifeTime();
		if(lifeTime > this.mFromTime && lifeTime < this.mToTime) {
			final float percent = (lifeTime - this.mFromTime) / this.mDuration;
			final float value = this.mFromValue + this.mSpanValue * percent;

			this.onSetValue(pParticle, value);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
