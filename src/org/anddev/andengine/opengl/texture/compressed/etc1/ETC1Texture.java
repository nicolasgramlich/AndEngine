package org.anddev.andengine.opengl.texture.compressed.etc1;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.ITexture.ITextureStateListener;
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

	// ===========================================================
	// Constructors
	// ===========================================================

	public ETC1Texture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, TextureOptions.DEFAULT, null);
	}

	public ETC1Texture(final int pWidth, final int pHeight, final ITextureStateListener pTextureStateListener) {
		this(pWidth, pHeight, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public ETC1Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, pTextureOptions, null);
	}

	public ETC1Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, TextureFormat.RGB_565, pTextureOptions, pTextureStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
		final InputStream inputStream = null;  // TODO
		ETC1Util.loadTexture(GL10.GL_TEXTURE_2D, 0, 0, this.mTextureFormat.getGLFormat(), this.mTextureFormat.getGLDataType(), inputStream);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
