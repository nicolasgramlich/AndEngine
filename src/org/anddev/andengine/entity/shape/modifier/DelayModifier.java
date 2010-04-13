package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 22:55:13 - 19.03.2010
 */
public class DelayModifier extends BaseModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DelayModifier(final float pDuration, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
	}

	public DelayModifier(final float pDuration) {
		super(pDuration);
	}

	public DelayModifier(final DelayModifier pDelayModifier) {
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
	protected void onManagedInitializeShape(final Shape pShape) {

	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final Shape pShape) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
