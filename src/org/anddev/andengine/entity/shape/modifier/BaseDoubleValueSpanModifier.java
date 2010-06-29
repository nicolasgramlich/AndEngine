package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class BaseDoubleValueSpanModifier extends BaseSingleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mValuePerSecondB;
	private final float mFromValueB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, null);
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IModifierListener pModiferListener) {
		super(pDuration, pFromValueA, pToValueA, pModiferListener);
		this.mFromValueB = pFromValueB;
		this.mValuePerSecondB = (pToValueB - pFromValueB) / pDuration;
	}
	
	public BaseDoubleValueSpanModifier(final BaseDoubleValueSpanModifier pBaseDoubleValueSpanModifier) {
		super(pBaseDoubleValueSpanModifier);
		this.mFromValueB = pBaseDoubleValueSpanModifier.mFromValueB;
		this.mValuePerSecondB = pBaseDoubleValueSpanModifier.mValuePerSecondB;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final Shape pShape, final float pValueA, final float pValueB);
	protected abstract void onSetValues(final Shape pShape, final float pValueA, final float pValueB);

	@Override
	protected final void onSetInitialValue(final Shape pShape, final float pValueA) {
		this.onSetInitialValues(pShape, pValueA, this.mFromValueB);
	}

	@Override
	protected final void onSetValue(final Shape pShape, final float pValueA) {
		this.onSetValues(pShape, pValueA, this.mFromValueB + this.getTotalSecondsElapsed() * this.mValuePerSecondB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
