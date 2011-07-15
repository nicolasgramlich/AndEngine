package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:19:46 - 29.06.2010
 */
public abstract class BaseTripleValueSpanModifier extends BaseDoubleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromValueC;
	private final float mToValueC;

	private final float mSpanValueC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueSpanModifier(final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final float pFromTime, final float pToTime) {
		super(pFromValueA, pToValueA, pFromValueB, pToValueB, pFromTime, pToTime);
		this.mFromValueC = pFromValueC;
		this.mToValueC = pToValueC;

		this.mSpanValueC = this.mToValueC - this.mFromValueC;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final Particle pParticle, final float pValueA, final float pValueB, final float pValueC);
	protected abstract void onSetValues(final Particle pParticle, final float pValueA, final float pValueB, final float pValueC);

	@Override
	@Deprecated
	protected void onSetValues(final Particle pParticle, final float pValueA, final float pValueB) { }

	@Override
	public void onSetInitialValues(final Particle pParticle, final float pValueA, final float pValueB) {
		this.onSetInitialValues(pParticle, pValueA, pValueB, this.mFromValueC);
	}

	@Override
	protected void onSetValueInternal(final Particle pParticle, final float pPercent) {
		this.onSetValues(pParticle, super.calculateValue(pPercent), super.calculateValueB(pPercent), this.calculateValueC(pPercent));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private final float calculateValueC(final float pPercent) {
		return this.mFromValueC + this.mSpanValueC * pPercent;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
