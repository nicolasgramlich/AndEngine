package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;

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

	public MoveByModifier(final float pDuration, final float pX, final float pY) {
		super(pDuration, pX, pY);
	}

	public MoveByModifier(final float pDuration, final float pX, final float pY, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pX, pY, pEntityModifierListener);
	}

	protected MoveByModifier(final DoubleValueChangeEntityModifier pDoubleValueChangeEntityModifier) {
		super(pDoubleValueChangeEntityModifier);
	}

	@Override
	public MoveByModifier deepCopy() {
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
