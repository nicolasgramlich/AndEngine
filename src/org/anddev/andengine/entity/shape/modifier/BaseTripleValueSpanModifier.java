package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;

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

	private final float mFromValueC;
	private final float mValueSpanC;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTripleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, null, pEaseFunction);
	}

	public BaseTripleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pShapeModiferListener, pEaseFunction);
		this.mFromValueC = pFromValueC;
		this.mValueSpanC = pToValueC - pFromValueC;
	}

	protected BaseTripleValueSpanModifier(final BaseTripleValueSpanModifier pBaseTripleValueSpanModifier) {
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

	protected abstract void onSetInitialValues(final IShape pShape, final float pValueA, final float pValueB, final float pValueC);
	protected abstract void onSetValues(final IShape pShape, final float pPerctentageDone, final float pValueA, final float pValueB, final float pValueC);

	@Override
	protected void onSetInitialValues(final IShape pShape, final float pValueA, final float pValueB) {
		this.onSetInitialValues(pShape, pValueA, pValueB, this.mFromValueC);
	}

	@Override
	protected void onSetValues(final IShape pShape, final float pPercentageDone, final float pValueA, final float pValueB) {
		this.onSetValues(pShape, pPercentageDone, pValueA, pValueB, this.mFromValueC + pPercentageDone * this.mValueSpanC);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
