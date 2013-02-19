package org.andengine.util.modifier;

import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

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

	private float mFromValueB;
	private float mValueSpanB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, null, EaseLinear.getInstance());
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, null, pEaseFunction);
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IModifierListener<T> pModifierListener) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pModifierListener, EaseLinear.getInstance());
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

	@Deprecated
	public float getFromValue() {
		return super.getFromValue();
	}

	@Deprecated
	public float getToValue() {
		return super.getToValue();
	}

	public float getFromValueA() {
		return super.getFromValue();
	}

	public float getToValueA() {
		return super.getToValue();
	}

	public float getFromValueB() {
		return this.mFromValueB;
	}

	public float getToValueB() {
		return this.mFromValueB + this.mValueSpanB;
	}

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

	@Override
	@Deprecated
	public void reset(final float pDuration, final float pFromValue, final float pToValue) {
		super.reset(pDuration, pFromValue, pToValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB) {
		super.reset(pDuration, pFromValueA, pToValueA);

		this.mFromValueB = pFromValueB;
		this.mValueSpanB = pToValueB - pFromValueB;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
