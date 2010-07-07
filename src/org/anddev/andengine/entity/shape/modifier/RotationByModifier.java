package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotationByModifier extends BaseSingleValueChangeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotationByModifier(final float pDuration, final float pRotation) {
		super(pDuration, pRotation);
	}

	public RotationByModifier(final RotationByModifier pRotationByModifier) {
		super(pRotationByModifier);
	}

	@Override
	public RotationByModifier clone(){
		return new RotationByModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onChangeValue(final IShape pShape, final float pValue) {
		pShape.setRotation(pShape.getRotation() + pValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
