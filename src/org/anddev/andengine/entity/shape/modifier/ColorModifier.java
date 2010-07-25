package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.engine.easying.Easing;
import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 15:39:50 - 29.06.2010
 */
public class ColorModifier extends BaseTripleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorModifier(final float pDuration, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final Easing pEasyingFunction) {
		this(pDuration, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, null, pEasyingFunction);
	}

	public ColorModifier(final float pDuration, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final IShapeModifierListener pShapeModiferListener, final Easing pEasyingFunction) {
		super(pDuration, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, pShapeModiferListener, pEasyingFunction);
	}

	protected ColorModifier(final ColorModifier pColorModifier) {
		super(pColorModifier);
	}

	@Override
	public ColorModifier clone(){
		return new ColorModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final IShape pShape, final float pRed, final float pGreen, final float pBlue) {
		pShape.setColor(pRed, pGreen, pBlue);
	}

	@Override
	protected void onSetValues(final IShape pShape, final float pRed, final float pGreen, final float pBlue) {
		pShape.setColor(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
