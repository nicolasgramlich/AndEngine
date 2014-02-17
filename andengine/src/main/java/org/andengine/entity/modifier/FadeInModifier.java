package org.andengine.entity.modifier;

import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
		super(pDuration, 0.0f, 1.0f, EaseLinear.getInstance());
	}

	public FadeInModifier(final float pDuration, final IEaseFunction pEaseFunction) {
		super(pDuration, 0.0f, 1.0f, pEaseFunction);
	}

	public FadeInModifier(final float pDuration, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, 0.0f, 1.0f, pEntityModifierListener, EaseLinear.getInstance());
	}

	public FadeInModifier(final float pDuration, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, 0.0f, 1.0f, pEntityModifierListener, pEaseFunction);
	}

	protected FadeInModifier(final FadeInModifier pFadeInModifier) {
		super(pFadeInModifier);
	}

	@Override
	public FadeInModifier deepCopy() {
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
