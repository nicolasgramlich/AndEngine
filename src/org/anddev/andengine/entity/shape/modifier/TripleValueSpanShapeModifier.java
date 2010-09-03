package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;
import org.anddev.andengine.util.modifier.BaseTripleValueSpanModifier;

/**
 * @author Nicolas Gramlich
 * @since 15:35:18 - 29.06.2010
 */
public abstract class TripleValueSpanShapeModifier extends BaseTripleValueSpanModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TripleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, pEaseFunction);
	}

	public TripleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, pShapeModiferListener, pEaseFunction);
	}

	protected TripleValueSpanShapeModifier(final TripleValueSpanShapeModifier pTripleValueSpanModifier) {
		super(pTripleValueSpanModifier);
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
