package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotateByModifier extends BaseSingleValueChangeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotateByModifier(final float pDuration, final float pRotation) {
		super(pDuration, pRotation);
	}

	public RotateByModifier(final RotateByModifier pRotateByModifier) {
		super(pRotateByModifier);
	}

	@Override
	public RotateByModifier clone(){
		return new RotateByModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onChangeValue(final Shape pShape, final float pValue) {
		pShape.setRotation(pShape.getRotation() + pValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
