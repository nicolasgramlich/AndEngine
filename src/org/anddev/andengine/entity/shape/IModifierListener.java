package org.anddev.andengine.entity.shape;


/**
 * @author Nicolas Gramlich
 * @since 18:37:22 - 19.03.2010
 */
public interface IModifierListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onModifierFinished(final IShapeModifier pShapeModifier, final Shape pShape);
}
