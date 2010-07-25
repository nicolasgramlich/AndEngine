package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.engine.easying.Easing;


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

	public FadeInModifier(final float pDuration, final Easing pEasyingFunction) {
		super(pDuration, 0.0f, 1.0f, pEasyingFunction);
	}

	public FadeInModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener, final Easing pEasyingFunction) {
		super(pDuration, 0.0f, 1.0f, pShapeModiferListener, pEasyingFunction);
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
