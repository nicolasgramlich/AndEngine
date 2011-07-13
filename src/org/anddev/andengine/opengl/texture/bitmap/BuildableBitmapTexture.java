package org.anddev.andengine.opengl.texture.bitmap;

import org.anddev.andengine.opengl.texture.BaseTexture.ITextureStateListener;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.TextureFormat;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.buildable.BuildableTexture;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:51:46 - 12.07.2011
 */
public class BuildableBitmapTexture extends BuildableTexture<IBitmapTextureSource, BitmapTexture> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, TextureFormat.RGBA_8888);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link BitmapTexture}s with transparency and {@link TextureFormat#RGB_565} for {@link BitmapTexture}s without transparency.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat) {
		this(pWidth, pHeight, pTextureFormat, TextureOptions.DEFAULT, null);
	}

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link BitmapTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) {
		this(pWidth, pHeight, TextureFormat.RGBA_8888, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link BitmapTexture}s with transparency and {@link TextureFormat#RGB_565} for {@link BitmapTexture}s without transparency.
	 * @param pTextureStateListener to be informed when this {@link BitmapTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) {
		this(pWidth, pHeight, pTextureFormat, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, TextureFormat.RGBA_8888, pTextureOptions, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link BitmapTexture}s with transparency and {@link TextureFormat#RGB_565} for {@link BitmapTexture}s without transparency.
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, pTextureFormat, pTextureOptions, null);
	}

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 * @param pTextureStateListener to be informed when this {@link BitmapTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) throws IllegalArgumentException {
		this(pWidth, pHeight, TextureFormat.RGBA_8888, pTextureOptions, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link BitmapTexture}s with transparency and {@link TextureFormat#RGB_565} for {@link BitmapTexture}s without transparency.
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 * @param pTextureStateListener to be informed when this {@link BitmapTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BuildableBitmapTexture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) throws IllegalArgumentException {
		super(new BitmapTexture(pWidth, pHeight, pTextureFormat, pTextureOptions, pTextureStateListener));
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
