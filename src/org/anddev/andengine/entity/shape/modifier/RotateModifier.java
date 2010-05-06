package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotateModifier extends BaseFromToModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotateModifier(final float pDuration, final float pFromAngle, final float pToAngle) {
		this(pDuration, pFromAngle, pToAngle, null);
	}

	public RotateModifier(final float pDuration, final float pFromAngle, final float pToAngle, final IModifierListener pModiferListener) {
		super(pDuration, pFromAngle, pToAngle, pModiferListener);
	}

	public RotateModifier(final RotateModifier pRotateModifier) {
		super(pRotateModifier);
	}
	
	@Override
	public RotateModifier clone(){
		return new RotateModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final Shape pShape, final float pAngle) {
		pShape.setAngle(pAngle);
	}

	@Override
	protected void onSetValue(final Shape pShape, final float pAngle) {
		pShape.setAngle(pAngle);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
