package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.BaseSingleValueChangeModifier;

/**
 * @author Nicolas Gramlich
 * @since 15:34:35 - 17.06.2010
 */
public abstract class SingleValueChangeShapeModifier extends BaseSingleValueChangeModifier<IEntity> implements IEntityModifier {
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

	public SingleValueChangeShapeModifier(final float pDuration, final float pValueChange, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pValueChange, pEntityModifierListener);
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
