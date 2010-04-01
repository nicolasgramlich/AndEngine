package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 23:13:01 - 19.03.2010
 */
public class AlphaModifier extends BaseFromToModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha) {
		this(pDuration, pFromAlpha, pToAlpha, null);
	}

	public AlphaModifier(final float pDuration, final float pFromAlpha, final float pToAlpha, final IModifierListener pModiferListener) {
		super(pDuration, pFromAlpha, pToAlpha, pModiferListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final float pAlpha, final BaseSprite pBaseSprite) {
		pBaseSprite.setAlpha(pAlpha);
	}

	@Override
	protected void onSetValue(final float pAlpha, final BaseSprite pBaseSprite) {
		pBaseSprite.setAlpha(pAlpha);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
