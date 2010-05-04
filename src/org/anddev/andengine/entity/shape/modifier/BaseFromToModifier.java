package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class BaseFromToModifier extends BaseModifier {
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

	public BaseFromToModifier(final float pDuration, final float pFromValue, final float pToValue) {
		this(pDuration, pFromValue, pToValue, null);
	}

	public BaseFromToModifier(final float pDuration, final float pFromValue, final float pToValue, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
		this.mFromValue = pFromValue;
		this.mValuePerSecond = (pToValue - pFromValue) / pDuration;
	}
	
	public BaseFromToModifier(final BaseFromToModifier pBaseFromToModifier) {
		super(pBaseFromToModifier);
		this.mFromValue = pBaseFromToModifier.mFromValue;
		this.mValuePerSecond = pBaseFromToModifier.mValuePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValue(final Shape pShape, final float pValue);
	protected abstract void onSetValue(final Shape pShape, final float pValue);

	@Override
	protected void onManagedInitializeShape(final Shape pShape) {
		this.onSetInitialValue(pShape, this.mFromValue);
	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final Shape pShape) {
		this.onSetValue(pShape, this.mFromValue + this.getTotalSecondsElapsed() * this.mValuePerSecond);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
