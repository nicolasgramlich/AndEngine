package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

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

	private final float mValuePerSecond;
	private final float mFromValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue) {
		this(pDuration, pFromValue, pToValue, null);
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pShapeModiferListener);
		this.mFromValue = pFromValue;
		this.mValuePerSecond = (pToValue - pFromValue) / pDuration;
	}
	
	protected BaseSingleValueSpanModifier(final BaseSingleValueSpanModifier pBaseSingleValueSpanModifier) {
		super(pBaseSingleValueSpanModifier);
		this.mFromValue = pBaseSingleValueSpanModifier.mFromValue;
		this.mValuePerSecond = pBaseSingleValueSpanModifier.mValuePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValue(final IShape pShape, final float pValue);
	protected abstract void onSetValue(final IShape pShape, final float pValue);

	@Override
	protected void onManagedInitializeShape(final IShape pShape) {
		this.onSetInitialValue(pShape, this.mFromValue);
	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final IShape pShape) {
		this.onSetValue(pShape, this.mFromValue + this.getTotalSecondsElapsed() * this.mValuePerSecond);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
