package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 23:13:01 - 19.03.2010
 */
public class AlphaModifier extends SingleValueSpanShapeModifier {
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
		this(pDuration, pFromAlpha, pToAlpha, null, IEaseFunction.DEFAULT);
	}

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromAlpha, pToAlpha, null, pEaseFunction);
	}

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromAlpha, pToAlpha, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromAlpha, pToAlpha, pShapeModiferListener, pEaseFunction);
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
	protected void onSetValue(final IShape pShape, final float pPercentageDone, final float pAlpha) {
		pShape.setAlpha(pAlpha);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
