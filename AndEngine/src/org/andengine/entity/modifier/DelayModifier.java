package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:55:13 - 19.03.2010
 */
public class DelayModifier extends DurationEntityModifier {
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
	public DelayModifier deepCopy(){
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
