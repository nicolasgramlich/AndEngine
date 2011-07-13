package org.anddev.andengine.opengl.texture.compressed.etc1;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.compressed.CompressedTexture;
import org.anddev.andengine.opengl.texture.compressed.etc1.source.IETC1TextureSource;

import android.opengl.ETC1Util;

/**
 * TODO Currently supports only a single IETC1TextureSource that needs to be the same size as the Texture!
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:32:01 - 13.07.2011
 */
public class ETC1Texture extends CompressedTexture<IETC1TextureSource> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ETC1Texture(final int pWidth, final int pHeight) {
		super(pWidth, pHeight);
	}

	public ETC1Texture(final int pWidth, final int pHeight, final ITextureStateListener<IETC1TextureSource> pTextureStateListener) {
		super(pWidth, pHeight, pTextureStateListener);
	}

	public ETC1Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions);
	}

	public ETC1Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener<IETC1TextureSource> pTextureStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions, pTextureStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
		final InputStream inputStream = this.mTextureSources.get(0).onGetInputStream();
		ETC1Util.loadTexture(GL10.GL_TEXTURE_2D, 0, 0, GL10.GL_RGB, GL10.GL_UNSIGNED_SHORT_5_6_5, inputStream);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
