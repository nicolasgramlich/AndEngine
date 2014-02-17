package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotationByModifier extends SingleValueChangeEntityModifier {
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

	public RotationByModifier(final float pDuration, final float pRotation, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pRotation, pEntityModifierListener);
	}

	protected RotationByModifier(final RotationByModifier pRotationByModifier) {
		super(pRotationByModifier);
	}

	@Override
	public RotationByModifier deepCopy(){
		return new RotationByModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onChangeValue(final float pSecondsElapsed, final IEntity pEntity, final float pRotation) {
		pEntity.setRotation(pEntity.getRotation() + pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
