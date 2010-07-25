package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.engine.easying.Easing;


/**
 * @author Nicolas Gramlich
 * @since 19:03:12 - 08.06.2010
 */
public class FadeOutModifier extends AlphaModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FadeOutModifier(final float pDuration, final Easing pEasyingFunction) {
		super(pDuration, 1.0f, 0.0f, pEasyingFunction);
	}

	public FadeOutModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener, final Easing pEasyingFunction) {
		super(pDuration, 1.0f, 0.0f, pShapeModiferListener, pEasyingFunction);
	}

	protected FadeOutModifier(final FadeOutModifier pFadeOutModifier) {
		super(pFadeOutModifier);
	}

	@Override
	public FadeOutModifier clone() {
		return new FadeOutModifier(this);
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
