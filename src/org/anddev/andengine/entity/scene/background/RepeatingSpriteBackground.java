package org.anddev.andengine.entity.scene.background;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.TextureFormat;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	private BitmapTexture mBitmapTexture;
	private final float mScale;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pCameraWidth
	 * @param pCameraHeight
	 * @param pTextureManager
	 * @param pBitmapTextureSource needs to be a power of two as otherwise the <code>repeating</code> feature doesn't work.
	 */
	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final IBitmapTextureSource pBitmapTextureSource) throws IllegalArgumentException {
		this(pCameraWidth, pCameraHeight, pTextureManager, pBitmapTextureSource, 1);
	}

	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final IBitmapTextureSource pBitmapTextureSource, final float pScale) throws IllegalArgumentException {
		super(null);
		this.mScale = pScale;
		this.mEntity = this.loadSprite(pCameraWidth, pCameraHeight, pTextureManager, pBitmapTextureSource);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BitmapTexture getTexture() {
		return this.mBitmapTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private Sprite loadSprite(final float pCameraWidth, final float pCameraHeight, final TextureManager pTextureManager, final IBitmapTextureSource pBitmapTextureSource) throws IllegalArgumentException {
		this.mBitmapTexture = new BitmapTexture(pBitmapTextureSource.getWidth(), pBitmapTextureSource.getHeight(), TextureFormat.RGBA_8888, TextureOptions.REPEATING_NEAREST_PREMULTIPLYALPHA);
		final TextureRegion textureRegion = BitmapTextureRegionFactory.createFromSource(this.mBitmapTexture, pBitmapTextureSource, 0, 0);

		final int width = Math.round(pCameraWidth / this.mScale);
		final int height = Math.round(pCameraHeight / this.mScale);

		textureRegion.setWidth(width);
		textureRegion.setHeight(height);

		pTextureManager.loadTexture(this.mBitmapTexture);

		final Sprite sprite = new Sprite(0, 0, width, height, textureRegion);
		sprite.setScaleCenter(0, 0);
		sprite.setScale(this.mScale);
		return sprite;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
