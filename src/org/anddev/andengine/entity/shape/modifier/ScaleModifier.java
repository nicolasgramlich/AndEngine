package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public class ScaleModifier extends DoubleValueSpanShapeModifier {
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
		this(pDuration, pFromScale, pToScale, null, IEaseFunction.DEFAULT);
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, null, pEaseFunction);
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IShapeModifierListener pShapeModifierListener) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pShapeModifierListener, IEaseFunction.DEFAULT);
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IShapeModifierListener pShapeModifierListener, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pShapeModifierListener, pEaseFunction);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, null, IEaseFunction.DEFAULT);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, null, pEaseFunction);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IShapeModifierListener pShapeModifierListener) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pShapeModifierListener, IEaseFunction.DEFAULT);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IShapeModifierListener pShapeModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pShapeModifierListener, pEaseFunction);
	}

	protected ScaleModifier(final ScaleModifier pScaleModifier) {
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
	protected void onSetValues(final IShape pShape, final float pPercentageDone, final float pScaleA, final float pScaleB) {
		pShape.setScale(pScaleA, pScaleB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
