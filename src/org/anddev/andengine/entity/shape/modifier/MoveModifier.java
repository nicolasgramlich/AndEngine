package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.engine.easying.Easing;
import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class MoveModifier extends BaseDoubleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final Easing pEasyingFunction) {
		this(pDuration, pFromX, pToX, pFromY, pToY, null, pEasyingFunction);
	}

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IShapeModifierListener pShapeModiferListener, final Easing pEasyingFunction) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pShapeModiferListener, pEasyingFunction);
	}

	protected MoveModifier(final MoveModifier pMoveModifier) {
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
	protected void onSetInitialValues(final IShape pShape, final float pX, final float pY) {
		pShape.setPosition(pX, pY);
	}

	@Override
	protected void onSetValues(final IShape pShape, final float pX, final float pY) {
		pShape.setPosition(pX, pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
