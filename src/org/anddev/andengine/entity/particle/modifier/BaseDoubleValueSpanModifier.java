package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:19:46 - 29.06.2010
 */
public abstract class BaseDoubleValueSpanModifier extends BaseSingleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromValueB;
	private final float mToValueB;

	private final float mSpanValueB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDoubleValueSpanModifier(final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromTime, final float pToTime) {
		super(pFromValueA, pToValueA, pFromTime, pToTime);
		this.mFromValueB = pFromValueB;
		this.mToValueB = pToValueB;

		this.mSpanValueB = this.mToValueB - this.mFromValueB;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	@Deprecated
	protected void onSetValue(final Particle pParticle, final float pValue) { }

	protected abstract void onSetInitialValues(final Particle pParticle, final float pValueA, final float pValueB);
	protected abstract void onSetValues(final Particle pParticle, final float pValueA, final float pValueB);

	@Override
	public void onSetInitialValue(final Particle pParticle, final float pValueA) {
		this.onSetInitialValues(pParticle, pValueA, this.mFromValueB);
	}

	@Override
	protected void onSetValueInternal(final Particle pParticle, final float pPercent) {
		this.onSetValues(pParticle, super.calculateValue(pPercent), this.calculateValueB(pPercent));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected final float calculateValueB(final float pPercent) {
		return this.mFromValueB + this.mSpanValueB * pPercent;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
