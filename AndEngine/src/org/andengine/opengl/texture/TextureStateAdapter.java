package org.andengine.opengl.texture;

import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:28:20 - 05.04.2012
 */
public class TextureStateAdapter<T extends ITextureAtlasSource> implements ITextureStateListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadedToHardware(final ITexture pTexture) {

	}

	@Override
	public void onUnloadedFromHardware(final ITexture pTexture) {

	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
