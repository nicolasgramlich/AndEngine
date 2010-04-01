package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class MoveModifier extends BasePairFromToModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY) {
		this(pDuration, pFromX, pToX, pFromY, pToY, null);
	}

	public MoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IModifierListener pModiferListener) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pModiferListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final float pX, final float pY, final BaseSprite pBaseSprite) {
		pBaseSprite.setPosition(pX, pY);
	}

	@Override
	protected void onSetValues(final float pX, final float pY, final BaseSprite pBaseSprite) {
		pBaseSprite.setPosition(pX, pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
