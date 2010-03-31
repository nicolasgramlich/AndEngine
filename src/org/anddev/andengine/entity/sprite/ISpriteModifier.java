package org.anddev.andengine.entity.sprite;


/**
 * @author Nicolas Gramlich
 * @since 11:17:50 - 19.03.2010
 */
public interface ISpriteModifier {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite);

	public IModifierListener getModiferListener();
	public void setModiferListener(final IModifierListener pModiferListener);
}
