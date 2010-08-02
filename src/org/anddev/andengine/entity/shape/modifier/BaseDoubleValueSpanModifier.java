package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;

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

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IShapeModifierListener pShapeModiferListener) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public BaseDoubleValueSpanModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pShapeModiferListener, pEaseFunction);
		this.mFromValueB = pFromValueB;
		this.mValueSpanB = pToValueB - pFromValueB;
	}

	protected BaseDoubleValueSpanModifier(final BaseDoubleValueSpanModifier pBaseDoubleValueSpanModifier) {
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

	protected abstract void onSetInitialValues(final IShape pShape, final float pValueA, final float pValueB);
	protected abstract void onSetValues(final IShape pShape, final float pPercentageDone, final float pValueA, final float pValueB);

	@Override
	protected void onSetInitialValue(final IShape pShape, final float pValueA) {
		this.onSetInitialValues(pShape, pValueA, this.mFromValueB);
	}

	@Override
	protected void onSetValue(final IShape pShape, final float pPercentageDone, final float pValueA) {
		this.onSetValues(pShape, pPercentageDone, pValueA, this.mFromValueB + pPercentageDone * this.mValueSpanB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
