package org.anddev.andengine.opengl.texture.compressed.etc1;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.opengl.ETC1Util;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:32:01 - 13.07.2011
 */
public class ETC1Texture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ETC1Texture() {
		this(TextureOptions.DEFAULT, null);
	}

	public ETC1Texture(final ITextureStateListener pTextureStateListener) {
		this(TextureOptions.DEFAULT, pTextureStateListener);
	}

	public ETC1Texture(final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pTextureOptions, null);
	}

	public ETC1Texture(final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		super(PixelFormat.RGB_565, pTextureOptions, pTextureStateListener);

		this.mWidth = 0; // TODO Extract header
		this.mHeight = 0; // TODO Extract header
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
		final InputStream inputStream = null;  // TODO
		ETC1Util.loadTexture(GL10.GL_TEXTURE_2D, 0, 0, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), inputStream);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
