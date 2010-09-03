package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;
import org.anddev.andengine.util.modifier.BaseSingleValueSpanModifier;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class SingleValueSpanShapeModifier extends BaseSingleValueSpanModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SingleValueSpanShapeModifier(final float pDuration, final float pFromValue, final float pToValue) {
		super(pDuration, pFromValue, pToValue);
	}

	public SingleValueSpanShapeModifier(final float pDuration, final float pFromValue, final float pToValue, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValue, pToValue, pEaseFunction);
	}

	public SingleValueSpanShapeModifier(final float pDuration, final float pFromValue, final float pToValue, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromValue, pToValue, pShapeModiferListener);
	}

	public SingleValueSpanShapeModifier(final float pDuration, final float pFromValue, final float pToValue, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValue, pToValue, pShapeModiferListener, pEaseFunction);
	}

	protected SingleValueSpanShapeModifier(final SingleValueSpanShapeModifier pSingleValueSpanShapeModifier) {
		super(pSingleValueSpanShapeModifier);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
