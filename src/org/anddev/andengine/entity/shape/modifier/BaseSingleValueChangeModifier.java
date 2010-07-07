package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 15:34:35 - 17.06.2010
 */
public abstract class BaseSingleValueChangeModifier extends BaseModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mValueChangePerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSingleValueChangeModifier(final float pDuration, final float pValueChange) {
		this(pDuration, pValueChange, null);
	}

	public BaseSingleValueChangeModifier(final float pDuration, final float pValueChange, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pShapeModiferListener);
		this.mValueChangePerSecond = pValueChange / pDuration;
	}

	public BaseSingleValueChangeModifier(final BaseSingleValueChangeModifier pByModifier) {
		super(pByModifier);
		this.mValueChangePerSecond = pByModifier.mValueChangePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onChangeValue(final IShape pShape, final float pValue);

	@Override
	protected void onManagedInitializeShape(final IShape pShape) {

	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final IShape pShape) {
		this.onChangeValue(pShape, this.mValueChangePerSecond * pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
