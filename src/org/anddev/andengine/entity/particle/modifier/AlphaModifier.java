package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AlphaModifier implements IParticleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromAlpha;
	private final float mToAlpha;
	private final float mFromTime;
	private final float mToTime;

	private final float mDuration;
	private final float mSpanAlpha;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AlphaModifier(final float pFromAlpha, final float pToAlpha, final float pFromTime, final float pToTime) {
		this.mFromAlpha = pFromAlpha;
		this.mToAlpha = pToAlpha;
		this.mFromTime = pFromTime;
		this.mToTime = pToTime;

		this.mSpanAlpha = this.mToAlpha - this.mFromAlpha;
		this.mDuration = this.mToTime - this.mFromTime;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle pParticle) {
		pParticle.setAlpha(this.mFromAlpha);
	}

	@Override
	public void onUpdateParticle(final Particle pParticle) {
		final float lifeTime = pParticle.getLifeTime();
		if(lifeTime > this.mFromTime && lifeTime < this.mToTime) {
			final float percent = (lifeTime - this.mFromTime) / this.mDuration;
			final float alpha = this.mFromAlpha + this.mSpanAlpha * percent;

			pParticle.setAlpha(alpha);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
