package org.anddev.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.compressed.CompressedTexture;
import org.anddev.andengine.opengl.texture.compressed.pvr.source.IPVRTextureSource;

/**
 * TODO Currently supports only a single IPVRTextureSource that needs to be the same size as the Texture!
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:18:10 - 13.07.2011
 */
public class PVRTexture extends CompressedTexture<IPVRTextureSource> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PVRTexture(final int pWidth, final int pHeight) {
		super(pWidth, pHeight);
	}

	public PVRTexture(final int pWidth, final int pHeight, final ITextureStateListener<IPVRTextureSource> pTextureStateListener) {
		super(pWidth, pHeight, pTextureStateListener);
	}

	public PVRTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		super(pWidth, pHeight, pTextureOptions);
	}

	public PVRTexture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener<IPVRTextureSource> pTextureStateListener) throws IllegalArgumentException {
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
		
		// TODO....
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
