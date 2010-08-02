package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotationModifier extends BaseSingleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation) {
		this(pDuration, pFromRotation, pToRotation, null, IEaseFunction.DEFAULT);
	}

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromRotation, pToRotation, null, pEaseFunction);
	}

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromRotation, pToRotation, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromRotation, pToRotation, pShapeModiferListener, pEaseFunction);
	}

	protected RotationModifier(final RotationModifier pRotationModifier) {
		super(pRotationModifier);
	}

	@Override
	public RotationModifier clone(){
		return new RotationModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final IShape pShape, final float pRotation) {
		pShape.setRotation(pRotation);
	}

	@Override
	protected void onSetValue(final IShape pShape, final float pPercentageDone, final float pRotation) {
		pShape.setRotation(pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
