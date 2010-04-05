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

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final float pAngle, final Shape pShape) {
		pShape.setAngle(pAngle);
	}

	@Override
	protected void onSetValue(final float pAngle, final Shape pShape) {
		pShape.setAngle(pAngle);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
