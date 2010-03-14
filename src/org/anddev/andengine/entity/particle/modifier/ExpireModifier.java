package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.util.constants.TimeConstants;

/**
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class ExpireModifier implements IParticleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final long mMinLifeTime;
	private final long mMaxLifeTime;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExpireModifier(final float pLifeTime) {
		this(pLifeTime, pLifeTime);
	}

	public ExpireModifier(final float pMinLifeTime, final float pMaxLifeTime) {
		this.mMinLifeTime = (long)(pMinLifeTime * TimeConstants.NANOSECONDSPERSECOND);
		this.mMaxLifeTime = (long)(pMaxLifeTime * TimeConstants.NANOSECONDSPERSECOND);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle pParticle) {
		pParticle.setDeathTime(pParticle.getBirthTime() + (long)((float)Math.random() * (this.mMaxLifeTime - this.mMinLifeTime) + this.mMinLifeTime));
	}

	@Override
	public void onUpdateParticle(final Particle pParticle) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
