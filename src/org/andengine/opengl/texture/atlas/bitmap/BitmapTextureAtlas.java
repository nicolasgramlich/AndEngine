package org.andengine.opengl.texture.atlas.bitmap;

import java.util.ArrayList;

import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.ExtrudingBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.ExtrudingBitmapTextureAtlasSource.ExtrusionDirection;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.util.GLState;
import org.andengine.util.exception.NullBitmapException;
import org.andengine.util.math.MathUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public class BitmapTextureAtlas extends TextureAtlas<IBitmapTextureAtlasSource> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final BitmapTextureFormat mBitmapTextureFormat;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight) {
		this(pTextureManager, pWidth, pHeight, BitmapTextureFormat.RGBA_8888);
	}

	/**
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat) {
		this(pTextureManager, pWidth, pHeight, pBitmapTextureFormat, TextureOptions.DEFAULT, null);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) {
		this(pTextureManager, pWidth, pHeight, BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT, pTextureAtlasStateListener);
	}

	/**
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) {
		this(pTextureManager, pWidth, pHeight, pBitmapTextureFormat, TextureOptions.DEFAULT, pTextureAtlasStateListener);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pTextureManager, pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, null);
	}

	/**
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
	 * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pTextureManager, pWidth, pHeight, pBitmapTextureFormat, pTextureOptions, null);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) throws IllegalArgumentException {
		this(pTextureManager, pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTextureAtlasStateListener);
	}

	/**
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} or {@link BitmapTextureFormat#RGBA_4444} for a {@link BitmapTextureAtlas} with transparency and {@link BitmapTextureFormat#RGB_565} for a {@link BitmapTextureAtlas} without transparency.
	 * @param pTextureOptions the (quality) settings of this {@link BitmapTextureAtlas}.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) throws IllegalArgumentException {
		super(pTextureManager, pWidth, pHeight, pBitmapTextureFormat.getPixelFormat(), pTextureOptions, pTextureAtlasStateListener);

		this.mBitmapTextureFormat = pBitmapTextureFormat;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BitmapTextureFormat getBitmapTextureFormat() {
		return this.mBitmapTextureFormat;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void addExtrudingTextureAtlasSource(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight, final ITextureAtlasSource pExtrusionSource, final ExtrusionDirection pExtrusionDirection, final int pExtrusionSize) {
		this.addTextureAtlasSource(new ExtrudingBitmapTextureAtlasSource(pWidth, pHeight, pExtrusionSource, pExtrusionDirection, pExtrusionSize), pTextureX, pTextureY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GLState pGLState) {
		final PixelFormat pixelFormat = this.mBitmapTextureFormat.getPixelFormat();
		final int glInternalFormat = pixelFormat.getGLInternalFormat();
		final int glFormat = pixelFormat.getGLFormat();
		final int glType = pixelFormat.getGLType();

		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, glInternalFormat, this.mWidth, this.mHeight, 0, glFormat, glType, null);

		final boolean preMultipyAlpha = this.mTextureOptions.mPreMultiplyAlpha;
		/* Non alpha premultiplied bitmaps are loaded with ARGB_8888 and converted down manually. */
		final Config bitmapConfig = (preMultipyAlpha) ? this.mBitmapTextureFormat.getBitmapConfig() : Config.ARGB_8888;

		final ArrayList<IBitmapTextureAtlasSource> textureSources = this.mTextureAtlasSources;
		final int textureSourceCount = textureSources.size();

		final ITextureAtlasStateListener<IBitmapTextureAtlasSource> textureStateListener = this.getTextureAtlasStateListener();
		for(int i = 0; i < textureSourceCount; i++) {
			final IBitmapTextureAtlasSource bitmapTextureAtlasSource = textureSources.get(i);
			try {
				final Bitmap bitmap = bitmapTextureAtlasSource.onLoadBitmap(bitmapConfig);
				if(bitmap == null) {
					throw new NullBitmapException("Caused by: " + bitmapTextureAtlasSource.getClass().toString() + " --> " + bitmapTextureAtlasSource.toString() + " returned a null Bitmap.");
				}

				/*
				 * Handle special case of extruding edges of a Bitmap.
				 * ExtrudingBitmapTextureAtlasSource expects to be fed with a
				 * Bitmap before it can return correct bitmap via onLoadBitmap()
				 */
				if (false == (bitmapTextureAtlasSource instanceof ExtrudingBitmapTextureAtlasSource)) {
					// There can be 4 extrusions made for a single texture region
					for(int j = (i + 1); j <= (i + 4); j++) {
						if ((j < textureSources.size()) && textureSources.get(j) instanceof ExtrudingBitmapTextureAtlasSource) {
							ExtrudingBitmapTextureAtlasSource extruder = (ExtrudingBitmapTextureAtlasSource) textureSources.get(j);
							if (extruder.getExtrusionSource() == bitmapTextureAtlasSource) { 
								extruder.createExtrusionBitmap(bitmap);
							}
						}
					}
				}

				final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(bitmap.getWidth()) && MathUtils.isPowerOfTwo(bitmap.getHeight()) && pixelFormat == PixelFormat.RGBA_8888;
				if(!useDefaultAlignment) {
					/* Adjust unpack alignment. */
					GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
				}

				if(preMultipyAlpha) {
					GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTextureAtlasSource.getTextureX(), bitmapTextureAtlasSource.getTextureY(), bitmap, glFormat, glType);
				} else {
					pGLState.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTextureAtlasSource.getTextureX(), bitmapTextureAtlasSource.getTextureY(), bitmap, this.mPixelFormat);
				}

				if(!useDefaultAlignment) {
					/* Restore default unpack alignment. */
					GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
				}

				bitmap.recycle();

				if(textureStateListener != null) {
					textureStateListener.onTextureAtlasSourceLoaded(this, bitmapTextureAtlasSource);
				}
			} catch (final NullBitmapException e) {
				if(textureStateListener != null) {
					textureStateListener.onTextureAtlasSourceLoadExeption(this, bitmapTextureAtlasSource, e);
				} else {
					throw e;
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
