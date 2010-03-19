package org.anddev.andengine.entity.sprite;

/**
 * @author Nicolas Gramlich
 * @since 18:37:22 - 19.03.2010
 */
public interface IModifierListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void onModifierFinished(final ISpriteModifier pSpriteModifier, final BaseSprite pBaseSprite);
}
