package org.anddev.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.anddev.andengine.opengl.texture.TextureOptions;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:31:23 - 15.07.2011
 */
public abstract class PVRGZTexture extends PVRTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureStateListener);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureOptions);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureOptions, pTextureStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract InputStream getInputStream() throws IOException;

	@Override
	protected final InputStream onGetInputStream() throws IOException {
		return new GZIPInputStream(this.getInputStream());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
