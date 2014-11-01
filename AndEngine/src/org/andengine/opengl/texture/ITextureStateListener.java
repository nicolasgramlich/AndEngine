package org.andengine.opengl.texture;


/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:25:58 - 05.04.2012
 */
public interface ITextureStateListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onLoadedToHardware(final ITexture pTexture);
	public void onUnloadedFromHardware(final ITexture pTexture);
}