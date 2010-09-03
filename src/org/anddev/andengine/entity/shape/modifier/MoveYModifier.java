package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 12:04:21 - 30.08.2010
 */
public class MoveYModifier extends SingleValueSpanShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MoveYModifier(final float pDuration, final float pFromY, final float pToY) {
		this(pDuration, pFromY, pToY, null, IEaseFunction.DEFAULT);
	}

	public MoveYModifier(final float pDuration, final float pFromY, final float pToY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromY, pToY, null, pEaseFunction);
	}

	public MoveYModifier(final float pDuration, final float pFromY, final float pToY, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromY, pToY, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public MoveYModifier(final float pDuration, final float pFromY, final float pToY, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromY, pToY, pShapeModiferListener, pEaseFunction);
	}

	protected MoveYModifier(final MoveYModifier pMoveYModifier) {
		super(pMoveYModifier);
	}

	@Override
	public MoveYModifier clone(){
		return new MoveYModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final IShape pShape, final float pY) {
		pShape.setPosition(pShape.getX(), pY);
	}

	@Override
	protected void onSetValue(final IShape pShape, final float pPercentageDone, final float pY) {
		pShape.setPosition(pShape.getX(), pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
