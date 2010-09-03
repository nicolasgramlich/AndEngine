package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class MoveModifier extends DoubleValueSpanShapeModifier {
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
		this(pDuration, pFromX, pToX, pFromY, pToY, null, IEaseFunction.DEFAULT);
	}

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromX, pToX, pFromY, pToY, null, pEaseFunction);
	}

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pShapeModiferListener, pEaseFunction);
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
	protected void onSetValues(final IShape pShape, final float pPercentageDone, final float pX, final float pY) {
		pShape.setPosition(pX, pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
