package org.anddev.andengine.entity.scene.background;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.Texture.TextureFormat;
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
	private final float mScale;

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
		this(pCameraWidth, pCameraHeight, pTextureManager, pTextureSource, 1);
	}

	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final ITextureSource pTextureSource, final float pScale) throws IllegalArgumentException {
		super(null);
		this.mScale = pScale;
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
		this.mTexture = new Texture(pTextureSource.getWidth(), pTextureSource.getHeight(), TextureFormat.RGBA_8888, TextureOptions.REPEATING_NEAREST_PREMULTIPLYALPHA);
		final TextureRegion textureRegion = TextureRegionFactory.createFromSource(this.mTexture, pTextureSource, 0, 0);

		final int width = Math.round(pCameraWidth / this.mScale);
		final int height = Math.round(pCameraHeight / this.mScale);

		textureRegion.setWidth(width);
		textureRegion.setHeight(height);

		pTextureManager.loadTexture(this.mTexture);

		final Sprite sprite = new Sprite(0, 0, width, height, textureRegion);
		sprite.setScaleCenter(0, 0);
		sprite.setScale(this.mScale);
		return sprite;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
