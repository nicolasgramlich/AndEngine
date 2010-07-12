package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 15:35:18 - 29.06.2010
 */
public abstract class BaseTripleValueSpanModifier extends BaseDoubleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mValuePerSecondC;
	private final float mFromValueC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, null);
	}

	public BaseTripleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pShapeModiferListener);
		this.mFromValueC = pFromValueC;
		this.mValuePerSecondC = (pToValueC - pFromValueC) / pDuration;
	}
	
	protected BaseTripleValueSpanModifier(final BaseTripleValueSpanModifier pBaseTripleValueSpanModifier) {
		super(pBaseTripleValueSpanModifier);
		this.mFromValueC = pBaseTripleValueSpanModifier.mFromValueC;
		this.mValuePerSecondC = pBaseTripleValueSpanModifier.mValuePerSecondC;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final IShape pShape, final float pValueA, final float pValueB, final float pValueC);
	protected abstract void onSetValues(final IShape pShape, final float pValueA, final float pValueB, final float pValueC);

	@Override
	protected void onSetInitialValues(final IShape pShape, final float pValueA, final float pValueB) {
		this.onSetInitialValues(pShape, pValueA, pValueB, this.mFromValueC);
	}

	@Override
	protected void onSetValues(final IShape pShape, final float pValueA, final float pValueB) {
		this.onSetValues(pShape, pValueA, pValueB, this.mFromValueC + this.getTotalSecondsElapsed() * this.mValuePerSecondC);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
