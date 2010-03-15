package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.util.constants.TimeConstants;

/**
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AlphaModifier implements IParticleModifier, TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromAlpha;
	private final float mToAlpha;
	private final long mFromNanoSeconds;
	private final long mToNanoSeconds;

	private final long mDurationNanoSeconds;
	private final float mSpanAlpha;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public AlphaModifier(final float pFromAlpha, final float pToAlpha, final float pFromSeconds, final float pToSeconds) {
		this.mFromAlpha = pFromAlpha;
		this.mToAlpha = pToAlpha;
		this.mFromNanoSeconds = (long)(pFromSeconds * NANOSECONDSPERSECOND);
		this.mToNanoSeconds = (long)(pToSeconds * NANOSECONDSPERSECOND);
		
		this.mSpanAlpha = this.mToAlpha - this.mFromAlpha;
		this.mDurationNanoSeconds = this.mToNanoSeconds - this.mFromNanoSeconds;
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
		final long now = System.nanoTime();
		final long birthTime = pParticle.getBirthTime();
		if(now > birthTime + this.mFromNanoSeconds && now < birthTime + this.mToNanoSeconds) {
			final long percent = (now - this.mFromNanoSeconds) / this.mDurationNanoSeconds;
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
