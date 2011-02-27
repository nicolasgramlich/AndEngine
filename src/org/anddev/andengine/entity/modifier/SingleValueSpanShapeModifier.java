package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.BaseSingleValueSpanModifier;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class SingleValueSpanShapeModifier extends BaseSingleValueSpanModifier<IEntity> implements IEntityModifier {
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

	public SingleValueSpanShapeModifier(final float pDuration, final float pFromValue, final float pToValue, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromValue, pToValue, pEntityModifierListener);
	}

	public SingleValueSpanShapeModifier(final float pDuration, final float pFromValue, final float pToValue, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValue, pToValue, pEntityModifierListener, pEaseFunction);
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
