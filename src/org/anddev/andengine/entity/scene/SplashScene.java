package org.anddev.andengine.entity.scene;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 09:45:02 - 03.05.2010
 */
public class SplashScene extends Scene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SplashScene(final Camera pCamera, final TextureRegion pTextureRegion) {
		this(pCamera, pTextureRegion, -1, 1, 1);
	}

	public SplashScene(final Camera pCamera, final TextureRegion pTextureRegion, final float pDuration, final float pScaleFrom, final float pScaleTo) {
		final Sprite loadingScreenSprite = new Sprite(pCamera.getMinX(), pCamera.getMinY(), pCamera.getWidth(), pCamera.getHeight(), pTextureRegion);
		if(pScaleFrom != 1 || pScaleTo != 1) {
			loadingScreenSprite.setScale(pScaleFrom);
			loadingScreenSprite.registerEntityModifier(new ScaleModifier(pDuration, pScaleFrom, pScaleTo, IEaseFunction.DEFAULT));
		}

		this.attachChild(loadingScreenSprite);
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
