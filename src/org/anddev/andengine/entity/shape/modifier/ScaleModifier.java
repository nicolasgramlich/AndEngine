package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public class ScaleModifier extends BaseDoubleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale) {
		this(pDuration, pFromScale, pToScale, null);
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IShapeModifierListener pShapeModiferListener) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pShapeModiferListener);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, null);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pShapeModiferListener);
	}

	public ScaleModifier(final ScaleModifier pScaleModifier) {
		super(pScaleModifier);
	}

	@Override
	public ScaleModifier clone(){
		return new ScaleModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final IShape pShape, final float pScaleA, final float pScaleB) {
		pShape.setScale(pScaleA, pScaleB);
	}

	@Override
	protected void onSetValues(final IShape pShape, final float pScaleA, final float pScaleB) {
		pShape.setScale(pScaleA, pScaleB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
