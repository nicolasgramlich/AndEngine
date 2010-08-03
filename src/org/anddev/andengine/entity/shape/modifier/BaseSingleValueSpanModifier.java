package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class BaseSingleValueSpanModifier extends BaseShapeDurationModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mFromValue;
	private final float mValueSpan;

	protected final IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue) {
		this(pDuration, pFromValue, pToValue, null, IEaseFunction.DEFAULT);
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromValue, pToValue, null, pEaseFunction);
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IShapeModifierListener pShapeModiferListener) {
		this(pDuration, pFromValue, pToValue, IEaseFunction.DEFAULT);
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pShapeModiferListener);
		this.mFromValue = pFromValue;
		this.mValueSpan = pToValue - pFromValue;
		this.mEaseFunction = pEaseFunction;
	}

	protected BaseSingleValueSpanModifier(final BaseSingleValueSpanModifier pBaseSingleValueSpanModifier) {
		super(pBaseSingleValueSpanModifier);
		this.mFromValue = pBaseSingleValueSpanModifier.mFromValue;
		this.mValueSpan = pBaseSingleValueSpanModifier.mValueSpan;
		this.mEaseFunction = pBaseSingleValueSpanModifier.mEaseFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValue(final IShape pShape, final float pValue);
	protected abstract void onSetValue(final IShape pShape, final float pPercentageDone, final float pValue);

	@Override
	protected void onManagedInitializeShape(final IShape pShape) {
		this.onSetInitialValue(pShape, this.mFromValue);
	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final IShape pShape) {
		final float percentageDone = this.mEaseFunction.getPercentageDone(this.getTotalSecondsElapsed(), this.mDuration, 0, 1);

		this.onSetValue(pShape, percentageDone, this.mFromValue + percentageDone * this.mValueSpan);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
