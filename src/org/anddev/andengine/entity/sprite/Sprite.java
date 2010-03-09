package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.Texture;

/**
 * @author Nicolas Gramlich
 * @since 19:22:38 - 09.03.2010
 */
public class Sprite extends BaseSprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sprite(final int pX, final int pY, final Texture pTexture) {
		super(pX, pY, pTexture);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return super.mTexture.getWidth();
	}
	
	@Override
	public int getHeight() {
		return this.mTexture.getHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
