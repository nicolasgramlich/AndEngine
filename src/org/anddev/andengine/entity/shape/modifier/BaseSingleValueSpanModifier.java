package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.engine.easying.Easing;
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

	private final float mFromValue;
	private final float mToValue;
	protected final Easing mEasyingFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final Easing pEasyingFunction) {
		this(pDuration, pFromValue, pToValue, null, pEasyingFunction);
	}

	public BaseSingleValueSpanModifier(final float pDuration, final float pFromValue, final float pToValue, final IShapeModifierListener pShapeModiferListener, final Easing pEasyingFunction) {
		super(pDuration, pShapeModiferListener);
		this.mEasyingFunction = pEasyingFunction;
		this.mToValue = pToValue;
		this.mFromValue = pFromValue;
	}
	
	protected BaseSingleValueSpanModifier(final BaseSingleValueSpanModifier pBaseSingleValueSpanModifier) {
		super(pBaseSingleValueSpanModifier);
		this.mEasyingFunction = pBaseSingleValueSpanModifier.mEasyingFunction;
		this.mToValue = pBaseSingleValueSpanModifier.mToValue;
		this.mFromValue = pBaseSingleValueSpanModifier.mFromValue;
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
		float ratio = mEasyingFunction.calc(this.getTotalSecondsElapsed(), 0, 1, mDuration);
		
		this.onSetValue(pShape, ratio*this.mToValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
