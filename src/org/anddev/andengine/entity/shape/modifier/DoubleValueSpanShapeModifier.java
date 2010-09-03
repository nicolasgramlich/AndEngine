package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;
import org.anddev.andengine.util.modifier.BaseDoubleValueSpanModifier;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class DoubleValueSpanShapeModifier extends BaseDoubleValueSpanModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DoubleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB);
	}

	public DoubleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pEaseFunction);
	}

	public DoubleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pShapeModiferListener);
	}

	public DoubleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pShapeModiferListener, pEaseFunction);
	}

	protected DoubleValueSpanShapeModifier(final DoubleValueSpanShapeModifier pDoubleValueSpanModifier) {
		super(pDoubleValueSpanModifier);
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
