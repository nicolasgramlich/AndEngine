package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 12:03:22 - 30.08.2010
 */
public class MoveXModifier extends SingleValueSpanShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MoveXModifier(final float pDuration, final float pFromX, final float pToX) {
		this(pDuration, pFromX, pToX, null, IEaseFunction.DEFAULT);
	}

	public MoveXModifier(final float pDuration, final float pFromX, final float pToX, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromX, pToX, null, pEaseFunction);
	}

	public MoveXModifier(final float pDuration, final float pFromX, final float pToX, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromX, pToX, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public MoveXModifier(final float pDuration, final float pFromX, final float pToX, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromX, pToX, pShapeModiferListener, pEaseFunction);
	}

	protected MoveXModifier(final MoveXModifier pMoveXModifier) {
		super(pMoveXModifier);
	}

	@Override
	public MoveXModifier clone(){
		return new MoveXModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final IShape pShape, final float pX) {
		pShape.setPosition(pX, pShape.getY());
	}

	@Override
	protected void onSetValue(final IShape pShape, final float pPercentageDone, final float pX) {
		pShape.setPosition(pX, pShape.getY());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
