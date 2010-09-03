package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.BaseSingleValueChangeModifier;

/**
 * @author Nicolas Gramlich
 * @since 15:34:35 - 17.06.2010
 */
public abstract class SingleValueChangeShapeModifier extends BaseSingleValueChangeModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SingleValueChangeShapeModifier(final float pDuration, final float pValueChange) {
		super(pDuration, pValueChange);
	}

	public SingleValueChangeShapeModifier(final float pDuration, final float pValueChange, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pValueChange, pShapeModiferListener);
	}

	protected SingleValueChangeShapeModifier(final SingleValueChangeShapeModifier pSingleValueChangeShapeModifier) {
		super(pSingleValueChangeShapeModifier);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
