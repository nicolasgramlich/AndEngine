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

	private Texture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pCameraWidth
	 * @param pCameraHeight
	 * @param pTextureManager
	 * @param pTextureSource needs to be a power of two as otherwise the <code>repeating</code> feature doesn't work.
	 */
	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource) throws IllegalArgumentException {
		super(null);
		this.mEntity = this.loadSprite(pCameraWidth, pCameraHeight, pTextureManager, pTextureSource);
	}

	/**
	 * @param pRed
	 * @param pGreen
	 * @param pBlue
	 * @param pCameraWidth
	 * @param pCameraHeight
	 * @param pTextureManager
	 * @param pTextureSource needs to be a power of two as otherwise the <code>repeating</code> feature doesn't work.
	 */
	public RepeatingSpriteBackground(final float pRed, final float pGreen, final float pBlue, final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource) throws IllegalArgumentException {
		super(pRed, pGreen, pBlue, null);
		this.mEntity = this.loadSprite(pCameraWidth, pCameraHeight, pTextureManager, pTextureSource);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Texture getTexture() {
		return this.mTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private Sprite loadSprite(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource) throws IllegalArgumentException {
		this.mTexture = new Texture(pTextureSource.getWidth(), pTextureSource.getHeight(), TextureOptions.REPEATING);
		final TextureRegion textureRegion = TextureRegionFactory.createFromSource(this.mTexture, pTextureSource, 0, 0);

		final int width = Math.round(pCameraWidth);
		final int height = Math.round(pCameraHeight);

		textureRegion.setWidth(width);
		textureRegion.setHeight(height);

		pTextureManager.loadTexture(this.mTexture);

		return new Sprite(0, 0, width, height, textureRegion);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
