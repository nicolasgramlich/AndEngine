package org.anddev.andengine.entity.shape.modifier;


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
		super(pDuration, 0.0f, 1.0f);
	}

	public FadeInModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, 0.0f, 1.0f, pShapeModiferListener);
	}

	public FadeInModifier(final FadeInModifier pFadeInModifier) {
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
