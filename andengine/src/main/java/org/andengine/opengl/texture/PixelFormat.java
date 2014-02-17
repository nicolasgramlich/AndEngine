package org.andengine.opengl.texture;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public enum PixelFormat {
	// ===========================================================
	// Elements
	// ===========================================================

	UNDEFINED(-1, -1, -1, -1),
	RGBA_4444(GLES20.GL_RGBA, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_SHORT_4_4_4_4, 16),
	RGBA_5551(GLES20.GL_RGB, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_SHORT_5_5_5_1, 16),
	RGBA_8888(GLES20.GL_RGBA, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, 32),
	RGB_565(GLES20.GL_RGB, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, 16),
	A_8(GLES20.GL_ALPHA, GLES20.GL_ALPHA, GLES20.GL_UNSIGNED_BYTE, 8),
	I_8(GLES20.GL_LUMINANCE, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, 8),
	AI_88(GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, 16);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mGLInternalFormat;
	private final int mGLFormat;
	private final int mGLType;
	private final int mBitsPerPixel;

	// ===========================================================
	// Constructors
	// ===========================================================

	private PixelFormat(final int pGLInternalFormat, final int pGLFormat, final int pGLType, final int pBitsPerPixel) {
		this.mGLInternalFormat = pGLInternalFormat;
		this.mGLFormat= pGLFormat;
		this.mGLType = pGLType;
		this.mBitsPerPixel = pBitsPerPixel;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getGLInternalFormat() {
		return this.mGLInternalFormat;
	}

	public int getGLFormat() {
		return this.mGLFormat;
	}

	public int getGLType() {
		return this.mGLType;
	}

	public int getBitsPerPixel() {
		return this.mBitsPerPixel;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}