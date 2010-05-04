package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class MoveModifier extends BasePairFromToModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY) {
		this(pDuration, pFromX, pToX, pFromY, pToY, null);
	}

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IModifierListener pModiferListener) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pModiferListener);
	}

	public MoveModifier(final MoveModifier pMoveModifier) {
		super(pMoveModifier);
	}
	
	@Override
	public MoveModifier clone(){
		return new MoveModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final Shape pShape, final float pX, final float pY) {
		pShape.setPosition(pX, pY);
	}

	@Override
	protected void onSetValues(final Shape pShape, final float pX, final float pY) {
		pShape.setPosition(pX, pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
