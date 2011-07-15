package org.anddev.andengine.entity.particle.initializer;

import static org.anddev.andengine.util.MathUtils.RANDOM;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:58:29 - 04.05.2010
 */
public abstract class BaseDoubleValueInitializer extends BaseSingleValueInitializer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mMinValueB;
	protected float mMaxValueB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDoubleValueInitializer(final float pMinValueA, final float pMaxValueA, final float pMinValueB, final float pMaxValueB) {
		super(pMinValueA, pMaxValueA);
		this.mMinValueB = pMinValueB;
		this.mMaxValueB = pMaxValueB;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInitializeParticle(final Particle pParticle, final float pValueA, final float pValueB);

	@Override
	protected final void onInitializeParticle(final Particle pParticle, final float pValueA) {
		this.onInitializeParticle(pParticle, pValueA, this.getRandomValueB());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private final float getRandomValueB() {
		if(this.mMinValueB == this.mMaxValueB) {
			return this.mMaxValueB;
		} else {
			return RANDOM.nextFloat() * (this.mMaxValueB - this.mMinValueB) + this.mMinValueB;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
