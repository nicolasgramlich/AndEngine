package org.anddev.andengine.util.modifier;

import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:51:46 - 03.09.2010
 * @param <T>
 */
public abstract class BaseDoubleValueSpanModifier<T> extends BaseSingleValueSpanModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromValueB;
	private final float mValueSpanB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, null, IEaseFunction.DEFAULT);
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, null, pEaseFunction);
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IModifierListener<T> pModifierListener) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pModifierListener, IEaseFunction.DEFAULT);
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IModifierListener<T> pModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pModifierListener, pEaseFunction);
		this.mFromValueB = pFromValueB;
		this.mValueSpanB = pToValueB - pFromValueB;
	}

	protected BaseDoubleValueSpanModifier(final BaseDoubleValueSpanModifier<T> pBaseDoubleValueSpanModifier) {
		super(pBaseDoubleValueSpanModifier);
		this.mFromValueB = pBaseDoubleValueSpanModifier.mFromValueB;
		this.mValueSpanB = pBaseDoubleValueSpanModifier.mValueSpanB;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final T pItem, final float pValueA, final float pValueB);
	protected abstract void onSetValues(final T pItem, final float pPercentageDone, final float pValueA, final float pValueB);

	@Override
	protected void onSetInitialValue(final T pItem, final float pValueA) {
		this.onSetInitialValues(pItem, pValueA, this.mFromValueB);
	}

	@Override
	protected void onSetValue(final T pItem, final float pPercentageDone, final float pValueA) {
		this.onSetValues(pItem, pPercentageDone, pValueA, this.mFromValueB + pPercentageDone * this.mValueSpanB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
