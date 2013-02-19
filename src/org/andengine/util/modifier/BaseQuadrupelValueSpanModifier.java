package org.andengine.util.modifier;

import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 10:58:32 - 02.08.2011
 * @param <T>
 */
public abstract class BaseQuadrupelValueSpanModifier<T> extends BaseTripleValueSpanModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mFromValueD;
	private float mValueSpanD;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseQuadrupelValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final float pFromValueD, final float pToValueD, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, pFromValueD, pToValueD, null, pEaseFunction);
	}

	public BaseQuadrupelValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final float pFromValueD, final float pToValueD, final IModifierListener<T> pModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, pModifierListener, pEaseFunction);

		this.mFromValueD = pFromValueD;
		this.mValueSpanD = pToValueD - pFromValueD;
	}

	protected BaseQuadrupelValueSpanModifier(final BaseQuadrupelValueSpanModifier<T> pBaseTripleValueSpanModifier) {
		super(pBaseTripleValueSpanModifier);

		this.mFromValueD = pBaseTripleValueSpanModifier.mFromValueD;
		this.mValueSpanD = pBaseTripleValueSpanModifier.mValueSpanD;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final T pItem, final float pValueA, final float pValueB, final float pValueC, final float pValueD);
	protected abstract void onSetValues(final T pItem, final float pPerctentageDone, final float pValueA, final float pValueB, final float pValueC, final float pValueD);

	@Override
	protected void onSetInitialValues(final T pItem, final float pValueA, final float pValueB, final float pValueC) {
		this.onSetInitialValues(pItem, pValueA, pValueB, pValueC, this.mFromValueD);
	}

	@Override
	protected void onSetValues(final T pItem, final float pPercentageDone, final float pValueA, final float pValueB, final float pValueC) {
		this.onSetValues(pItem, pPercentageDone, pValueA, pValueB, pValueC, this.mFromValueD + pPercentageDone * this.mValueSpanD);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	@Deprecated
	public void reset(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC) {
		super.reset(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC);
	}

	public void reset(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final float pFromValueD, final float pToValueD) {
		super.reset(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC);

		this.mFromValueD = pFromValueD;
		this.mValueSpanD = pToValueD - pFromValueD;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
