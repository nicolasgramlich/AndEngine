package org.andengine.opengl.texture;

import java.io.IOException;

import org.andengine.opengl.util.GLState;

import android.opengl.GLES20;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 3:07:48 PM - Apr 24, 2012
 */
public class EmptyTexture extends Texture{
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
	// ==========================================================

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight) {
		this(pTextureManager, pWidth, pHeight, (ITextureStateListener) null);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final ITextureStateListener pTextureStateListener) {
		this(pTextureManager, pWidth, pHeight, PixelFormat.RGBA_8888, pTextureStateListener);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final PixelFormat pPixelFormat) {
		this(pTextureManager, pWidth, pHeight, pPixelFormat, (ITextureStateListener) null);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final PixelFormat pPixelFormat, final ITextureStateListener pTextureStateListener) {
		this(pTextureManager, pWidth, pHeight, pPixelFormat, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final TextureOptions pTextureOptions) {
		this(pTextureManager, pWidth, pHeight, pTextureOptions, (ITextureStateListener) null);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) {
		this(pTextureManager, pWidth, pHeight, PixelFormat.RGBA_8888, pTextureOptions, pTextureStateListener);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final PixelFormat pPixelFormat, final TextureOptions pTextureOptions) {
		this(pTextureManager, pWidth, pHeight, pPixelFormat, pTextureOptions, (ITextureStateListener) null);
	}

	public EmptyTexture(final TextureManager pTextureManager, final int pWidth, final int pHeight, final PixelFormat pPixelFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) {
		super(pTextureManager, pPixelFormat, pTextureOptions, pTextureStateListener);

		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	protected void writeTextureToHardware(final GLState pGLState) throws IOException {
		final PixelFormat pixelFormat = this.mPixelFormat;
		final int glInternalFormat = pixelFormat.getGLInternalFormat();
		final int glFormat = pixelFormat.getGLFormat();
		final int glType = pixelFormat.getGLType();

		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, glInternalFormat, this.mWidth, this.mHeight, 0, glFormat, glType, null);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
