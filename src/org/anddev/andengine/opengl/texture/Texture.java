package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:11:52 - 12.07.2011
 * 
 * @deprecated Use {@link BitmapTexture} instead.
 */
@Deprecated
public class Texture extends BitmapTexture {
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
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight) {
		super(pWidth, pHeight);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat) {
		super(pWidth, pHeight, pTextureFormat);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) {
		super(pWidth, pHeight, pTextureStateListener);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) {
		super(pWidth, pHeight, pTextureFormat, pTextureStateListener);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureFormat, pTextureOptions);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions, pTextureStateListener);
	}

	/**
	 * @deprecated Use {@link BitmapTexture} instead.
	 */
	@Deprecated
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener<IBitmapTextureSource> pTextureStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureFormat, pTextureOptions, pTextureStateListener);
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
