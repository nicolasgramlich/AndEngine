package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.engine.easying.Easing;
import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 23:13:01 - 19.03.2010
 */
public class AlphaModifier extends BaseSingleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final Easing pEasyingFunction) {
		this(pDuration, pFromAlpha, pToAlpha, null, pEasyingFunction);
	}

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final IShapeModifierListener pShapeModiferListener, final Easing pEasyingFunction) {
		super(pDuration, pFromAlpha, pToAlpha, pShapeModiferListener, pEasyingFunction);
	}

	protected AlphaModifier(final AlphaModifier pAlphaModifier) {
		super(pAlphaModifier);
	}
	
	@Override
	public AlphaModifier clone(){
		return new AlphaModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final IShape pShape, final float pAlpha) {
		pShape.setAlpha(pAlpha);
	}

	@Override
	protected void onSetValue(final IShape pShape, final float pAlpha) {
		pShape.setAlpha(pAlpha);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
