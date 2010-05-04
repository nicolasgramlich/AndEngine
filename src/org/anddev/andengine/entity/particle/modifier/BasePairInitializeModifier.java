package org.anddev.andengine.entity.particle.modifier;

import static org.anddev.andengine.util.MathUtils.RANDOM;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 15:58:29 - 04.05.2010
 */
public abstract class BasePairInitializeModifier implements IParticleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mMinA;
	private final float mMaxA;
	private final float mMinB;
	private final float mMaxB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BasePairInitializeModifier(final float pMinA, final float pMaxA, final float pMinB, final float pMaxB) {
		this.mMinA = pMinA;
		this.mMaxA = pMaxA;
		this.mMinB = pMinB;
		this.mMaxB = pMaxB;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInitializeParticle(final Particle pParticle, final float pValueA, final float pValueB);

	@Override
	public void onInitializeParticle(final Particle pParticle) {
		this.onInitializeParticle(pParticle, this.determineA(), this.determineB());
	}

	@Override
	public void onUpdateParticle(final Particle pParticle) { }

	// ===========================================================
	// Methods
	// ===========================================================

	private float determineA() {
		if(this.mMinA == this.mMaxA) {
			return this.mMaxA;
		} else {
			return RANDOM.nextFloat() * (this.mMaxA - this.mMinA) + this.mMinA;
		}
	}

	private float determineB() {
		if(this.mMinB == this.mMaxB) {
			return this.mMaxB;
		} else {
			return RANDOM.nextFloat() * (this.mMaxB - this.mMinB) + this.mMinB;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
