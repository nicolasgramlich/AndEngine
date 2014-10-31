package org.andengine.util.modifier;

import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class BaseSingleValueSpanModifier<T> extends BaseDurationModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mFromValue;
	private float mValueSpan;

	protected final IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue) {
		this(pDuration, pFromValue, pToValue, null, EaseLinear.getInstance());
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValue, pToValue, null, pEaseFunction);
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IModifierListener<T> pModifierListener) {
		this(pDuration, pFromValue, pToValue, pModifierListener, EaseLinear.getInstance());
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IModifierListener<T> pModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pModifierListener);

		this.mFromValue = pFromValue;
		this.mValueSpan = pToValue - pFromValue;
		this.mEaseFunction = pEaseFunction;
	}

	protected BaseSingleValueSpanModifier(final BaseSingleValueSpanModifier<T> pBaseSingleValueSpanModifier) {
		super(pBaseSingleValueSpanModifier);

		this.mFromValue = pBaseSingleValueSpanModifier.mFromValue;
		this.mValueSpan = pBaseSingleValueSpanModifier.mValueSpan;
		this.mEaseFunction = pBaseSingleValueSpanModifier.mEaseFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getFromValue() {
		return this.mFromValue;
	}

	public float getToValue() {
		return this.mFromValue + this.mValueSpan;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValue(final T pItem, final float pValue);
	protected abstract void onSetValue(final T pItem, final float pPercentageDone, final float pValue);

	@Override
	protected void onManagedInitialize(final T pItem) {
		this.onSetInitialValue(pItem, this.mFromValue);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed, final T pItem) {
		final float percentageDone = this.mEaseFunction.getPercentage(this.getSecondsElapsed(), this.mDuration);

		this.onSetValue(pItem, percentageDone, this.mFromValue + percentageDone * this.mValueSpan);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void reset(final float pDuration, final float pFromValue, final float pToValue) {
		super.reset();
		
		this.mDuration = pDuration;
		this.mFromValue = pFromValue;
		this.mValueSpan = pToValue - pFromValue;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
