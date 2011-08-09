package org.anddev.andengine.opengl.texture;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:00:09 - 05.04.2010
 */
public class TextureOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final TextureOptions NEAREST = new TextureOptions(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, false);
	public static final TextureOptions BILINEAR = new TextureOptions(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, false);
	public static final TextureOptions REPEATING_NEAREST = new TextureOptions(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT, false);
	public static final TextureOptions REPEATING_BILINEAR = new TextureOptions(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT, false);

	public static final TextureOptions NEAREST_PREMULTIPLYALPHA = new TextureOptions(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, true);
	public static final TextureOptions BILINEAR_PREMULTIPLYALPHA = new TextureOptions(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, true);
	public static final TextureOptions REPEATING_NEAREST_PREMULTIPLYALPHA = new TextureOptions(GLES20.GL_NEAREST, GLES20.GL_NEAREST, GLES20.GL_REPEAT, GLES20.GL_REPEAT, true);
	public static final TextureOptions REPEATING_BILINEAR_PREMULTIPLYALPHA = new TextureOptions(GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_REPEAT, GLES20.GL_REPEAT, true);

	public static final TextureOptions DEFAULT = NEAREST_PREMULTIPLYALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	public final int mMagFilter;
	public final int mMinFilter;
	public final float mWrapT;
	public final float mWrapS;
	public final boolean mPreMultipyAlpha;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureOptions(final int pMinFilter, final int pMagFilter, final int pWrapT, final int pWrapS, final boolean pPreMultiplyAlpha) {
		this.mMinFilter = pMinFilter;
		this.mMagFilter = pMagFilter;
		this.mWrapT = pWrapT;
		this.mWrapS = pWrapS;
		this.mPreMultipyAlpha = pPreMultiplyAlpha;
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

	public void apply() {
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, this.mMinFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, this.mMagFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, this.mWrapS);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, this.mWrapT);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
