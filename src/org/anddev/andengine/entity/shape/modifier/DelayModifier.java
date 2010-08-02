package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 22:55:13 - 19.03.2010
 */
public class DelayModifier extends BaseShapeDurationModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DelayModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pShapeModiferListener);
	}

	public DelayModifier(final float pDuration) {
		super(pDuration);
	}

	protected DelayModifier(final DelayModifier pDelayModifier) {
		super(pDelayModifier);
	}

	@Override
	public DelayModifier clone(){
		return new DelayModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedInitializeShape(final IShape pShape) {

	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final IShape pShape) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
