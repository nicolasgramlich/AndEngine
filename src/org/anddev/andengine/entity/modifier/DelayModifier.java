package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;

/**
 * @author Nicolas Gramlich
 * @since 22:55:13 - 19.03.2010
 */
public class DelayModifier extends DurationShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DelayModifier(final float pDuration, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pEntityModifierListener);
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
	protected void onManagedInitialize(final IEntity pEntity) {

	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed, final IEntity pEntity) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
