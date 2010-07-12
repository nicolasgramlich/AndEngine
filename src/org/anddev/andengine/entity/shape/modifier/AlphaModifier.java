package org.anddev.andengine.entity.shape.modifier;

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

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha) {
		this(pDuration, pFromAlpha, pToAlpha, null);
	}

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromAlpha, pToAlpha, pShapeModiferListener);
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
