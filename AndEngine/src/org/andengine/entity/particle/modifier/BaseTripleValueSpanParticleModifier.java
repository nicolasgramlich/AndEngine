package org.andengine.entity.particle.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:19:46 - 29.06.2010
 */
public abstract class BaseTripleValueSpanParticleModifier<T extends IEntity> extends BaseDoubleValueSpanParticleModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mFromValueC;
	private float mValueSpanC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueSpanParticleModifier(final float pFromTime, final float pToTime, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC) {
		this(pFromTime, pToTime, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, EaseLinear.getInstance());
	}

	public BaseTripleValueSpanParticleModifier(final float pFromTime, final float pToTime, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IEaseFunction pEaseFunction) {
		super(pFromTime, pToTime, pFromValueA, pToValueA, pFromValueB, pToValueB, pEaseFunction);

		this.mFromValueC = pFromValueC;
		this.mValueSpanC = pToValueC - pFromValueC;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final Particle<T> pParticle, final float pValueA, final float pValueB, final float pValueC);
	protected abstract void onSetValues(final Particle<T> pParticle, final float pPercentageDone, final float pValueA, final float pValueB, final float pValueC);

	@Override
	public void onSetInitialValues(final Particle<T> pParticle, final float pValueA, final float pValueB) {
		this.onSetInitialValues(pParticle, pValueA, pValueB, this.mFromValueC);
	}

	@Override
	protected void onSetValues(final Particle<T> pParticle, final float pPercentageDone, final float pValueA, final float pValueB) {
		this.onSetValues(pParticle, pPercentageDone, pValueA, pValueB, this.mFromValueC + pPercentageDone * this.mValueSpanC);
	}

	@Override
	@Deprecated
	public void reset(float pFromValueA, float pToValueA, float pFromValueB, float pToValueB, float pFromTime, float pToTime) {
		super.reset(pFromValueA, pToValueA, pFromValueB, pToValueB, pFromTime, pToTime);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset(final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final float pFromTime, final float pToTime) {
		super.reset(pFromValueA, pToValueA, pFromValueB, pToValueB, pFromTime, pToTime);

		this.mFromValueC = pFromValueC;
		this.mValueSpanC = pToValueC - pFromValueC;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
