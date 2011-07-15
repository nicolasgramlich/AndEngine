package org.anddev.andengine.util.modifier;

import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:52:31 - 03.09.2010
 * @param <T>
 */
public abstract class BaseTripleValueSpanModifier<T> extends BaseDoubleValueSpanModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromValueC;
	private final float mValueSpanC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, null, pEaseFunction);
	}

	public BaseTripleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IModifierListener<T> pModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pModifierListener, pEaseFunction);
		this.mFromValueC = pFromValueC;
		this.mValueSpanC = pToValueC - pFromValueC;
	}

	protected BaseTripleValueSpanModifier(final BaseTripleValueSpanModifier<T> pBaseTripleValueSpanModifier) {
		super(pBaseTripleValueSpanModifier);
		this.mFromValueC = pBaseTripleValueSpanModifier.mFromValueC;
		this.mValueSpanC = pBaseTripleValueSpanModifier.mValueSpanC;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final T pItem, final float pValueA, final float pValueB, final float pValueC);
	protected abstract void onSetValues(final T pItem, final float pPerctentageDone, final float pValueA, final float pValueB, final float pValueC);

	@Override
	protected void onSetInitialValues(final T pItem, final float pValueA, final float pValueB) {
		this.onSetInitialValues(pItem, pValueA, pValueB, this.mFromValueC);
	}

	@Override
	protected void onSetValues(final T pItem, final float pPercentageDone, final float pValueA, final float pValueB) {
		this.onSetValues(pItem, pPercentageDone, pValueA, pValueB, this.mFromValueC + pPercentageDone * this.mValueSpanC);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
