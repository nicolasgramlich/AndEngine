package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.BaseDoubleValueSpanModifier;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class DoubleValueSpanShapeModifier extends BaseDoubleValueSpanModifier<IEntity> implements IEntityModifier {
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

	public DoubleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pEntityModifierListener);
	}

	public DoubleValueSpanShapeModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pEntityModifierListener, pEaseFunction);
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
