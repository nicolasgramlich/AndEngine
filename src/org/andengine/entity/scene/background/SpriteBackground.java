package org.andengine.entity.scene.background;

import org.andengine.entity.sprite.Sprite;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:01:43 - 19.07.2010
 */
public class SpriteBackground extends EntityBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBackground(final Sprite pSprite) {
		super(pSprite);
	}

	public SpriteBackground(final float pRed, final float pGreen, final float pBlue, final Sprite pSprite) {
		super(pRed, pGreen, pBlue, pSprite);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Sprite getSprite() {
		return (Sprite) this.mEntity;
	}

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
