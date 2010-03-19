package org.anddev.andengine.entity.sprite;

/**
 * @author Nicolas Gramlich
 * @since 18:37:22 - 19.03.2010
 */
public interface IModificationListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void onFinished(final ISpriteModifier pSpriteModifier, final BaseSprite pBaseSprite);
}
