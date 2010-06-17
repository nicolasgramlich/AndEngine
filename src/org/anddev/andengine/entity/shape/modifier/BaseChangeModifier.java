package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 15:34:35 - 17.06.2010
 */
public abstract class BaseChangeModifier extends BaseModifier {
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

	public BaseChangeModifier(final float pDuration, final float pValueChange) {
		this(pDuration, pValueChange, null);
	}

	public BaseChangeModifier(final float pDuration, final float pValueChange, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
		this.mValueChangePerSecond = pValueChange / pDuration;
	}

	public BaseChangeModifier(final BaseChangeModifier pByModifier) {
		super(pByModifier);
		this.mValueChangePerSecond = pByModifier.mValueChangePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onChangeValue(final Shape pShape, final float pValue);

	@Override
	protected void onManagedInitializeShape(final Shape pShape) {

	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final Shape pShape) {
		this.onChangeValue(pShape, this.mValueChangePerSecond * pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
