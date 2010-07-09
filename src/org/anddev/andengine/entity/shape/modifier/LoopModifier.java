package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 22:55:13 - 19.03.2010
 */
public class LoopModifier extends BaseModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pShapeModiferListener);
	}

	public LoopModifier(final float pDuration) {
		super(pDuration);
	}

	public LoopModifier(final LoopModifier pDelayModifier) {
		super(pDelayModifier);
	}
	
	@Override
	public LoopModifier clone(){
		return new LoopModifier(this);
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
