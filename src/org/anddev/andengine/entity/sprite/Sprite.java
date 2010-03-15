package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.texture.TextureRegion;

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

	public Sprite(final float pX, final float pY, final TextureRegion pTextureRegion) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion);
	}
	
	public Sprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
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
