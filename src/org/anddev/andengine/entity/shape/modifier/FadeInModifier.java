package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.util.modifier.ease.IEaseFunction;


/**
 * @author Nicolas Gramlich
 * @since 19:03:12 - 08.06.2010
 */
public class FadeInModifier extends AlphaModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FadeInModifier(final float pDuration) {
		super(pDuration, 0.0f, 1.0f, IEaseFunction.DEFAULT);
	}

	public FadeInModifier(final float pDuration, final IEaseFunction pEaseFunction) {
		super(pDuration, 0.0f, 1.0f, pEaseFunction);
	}

	public FadeInModifier(final float pDuration, final IShapeModifierListener pShapeModifierListener) {
		super(pDuration, 0.0f, 1.0f, pShapeModifierListener, IEaseFunction.DEFAULT);
	}

	public FadeInModifier(final float pDuration, final IShapeModifierListener pShapeModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, 0.0f, 1.0f, pShapeModifierListener, pEaseFunction);
	}

	protected FadeInModifier(final FadeInModifier pFadeInModifier) {
		super(pFadeInModifier);
	}

	@Override
	public FadeInModifier clone() {
		return new FadeInModifier(this);
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
