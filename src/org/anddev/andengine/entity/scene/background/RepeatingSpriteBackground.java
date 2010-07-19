package org.anddev.andengine.entity.scene.background;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 15:11:10 - 19.07.2010
 */
public class RepeatingSpriteBackground extends SpriteBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource) throws IllegalArgumentException {
		super(loadSprite(pCameraWidth, pCameraHeight, pTextureManager, pTextureSource));
	}

	public RepeatingSpriteBackground(final float pRed, final float pGreen, final float pBlue, final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource) throws IllegalArgumentException {
		super(pRed, pGreen, pBlue, loadSprite(pCameraWidth, pCameraHeight, pTextureManager, pTextureSource));
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

	private static Sprite loadSprite(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource) throws IllegalArgumentException {
		final Texture texture = new Texture(pTextureSource.getWidth(), pTextureSource.getHeight(), TextureOptions.REPEATING);
		final TextureRegion textureRegion = TextureRegionFactory.createFromSource(texture, pTextureSource, 0, 0);

		final int width = Math.round(pCameraWidth);
		final int height = Math.round(pCameraHeight);

		textureRegion.setWidth(width);
		textureRegion.setHeight(height);

		pTextureManager.loadTexture(texture);

		return new Sprite(0, 0, width, height, textureRegion);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
