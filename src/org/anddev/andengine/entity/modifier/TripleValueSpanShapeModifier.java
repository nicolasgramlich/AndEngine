package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.BaseTripleValueSpanModifier;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 15:35:18 - 29.06.2010
 */
public abstract class TripleValueSpanShapeModifier extends BaseTripleValueSpanModifier<IEntity> implements IEntityModifier {
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

	public TripleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final float pFromValueC, final float pToValueC, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pFromValueC, pToValueC, pEntityModifierListener, pEaseFunction);
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
