package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;

/**
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:15:52 - 10.08.2011
 */
public class MoveByModifier extends DoubleValueChangeEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MoveByModifier(final float pDuration, final float pValueChangeA, final float pValueChangeB) {
		super(pDuration, pValueChangeA, pValueChangeB);
	}

	public MoveByModifier(final float pDuration, final float pValueChangeA, final float pValueChangeB, final IEntityModifierListener pModifierListener) {
		super(pDuration, pValueChangeA, pValueChangeB, pModifierListener);
	}

	protected MoveByModifier(final DoubleValueChangeEntityModifier pDoubleValueChangeEntityModifier) {
		super(pDoubleValueChangeEntityModifier);
	}

	@Override
	public MoveByModifier clone(){
		return new MoveByModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onChangeValues(final float pSecondsElapsed, final IEntity pEntity, final float pX, final float pY) {
		pEntity.setPosition(pEntity.getX() + pX, pEntity.getY() + pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
