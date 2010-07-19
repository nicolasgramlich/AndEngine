package org.anddev.andengine.entity.scene.background;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.sprite.Sprite;

/**
 * @author Nicolas Gramlich
 * @since 14:01:43 - 19.07.2010
 */
public class SpriteBackground extends ColorBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Sprite mSprite;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBackground(final Sprite pSprite) {
		super();
		this.mSprite = pSprite;
	}

	public SpriteBackground(final float pRed, final float pGreen, final float pBlue, final Sprite pSprite) {
		super(pRed, pGreen, pBlue);
		this.mSprite = pSprite;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GL10 pGL, final Camera pCamera) {
		this.mSprite.onDraw(pGL);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
