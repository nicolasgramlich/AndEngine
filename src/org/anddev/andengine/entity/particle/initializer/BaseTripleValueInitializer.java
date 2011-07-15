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
public abstract class BaseTripleValueInitializer extends BaseDoubleValueInitializer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mMinValueC;
	protected float mMaxValueC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueInitializer(final float pMinValueA, final float pMaxValueA, final float pMinValueB, final float pMaxValueB, final float pMinValueC, final float pMaxValueC) {
		super(pMinValueA, pMaxValueA, pMinValueB, pMaxValueB);
		this.mMinValueC = pMinValueC;
		this.mMaxValueC = pMaxValueC;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInitializeParticle(final Particle pParticle, final float pValueA, final float pValueB, final float pValueC);

	@Override
	protected final void onInitializeParticle(final Particle pParticle, final float pValueA, final float pValueB) {
		this.onInitializeParticle(pParticle, pValueA, pValueB, this.getRandomValueC());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private final float getRandomValueC() {
		if(this.mMinValueC == this.mMaxValueC) {
			return this.mMaxValueC;
		} else {
			return RANDOM.nextFloat() * (this.mMaxValueC - this.mMinValueC) + this.mMinValueC;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
