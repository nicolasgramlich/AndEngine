package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public class ScaleModifier extends BaseFromToModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale) {
		this(pDuration, pFromScale, pToScale, null);
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IModifierListener pModiferListener) {
		super(pDuration, pFromScale, pToScale, pModiferListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final float pValue, final BaseSprite pBaseSprite) {
		pBaseSprite.setScale(pValue);
	}

	@Override
	protected void onSetValue(final float pValue, final BaseSprite pBaseSprite) {
		pBaseSprite.setScale(pValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
