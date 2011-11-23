package org.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy.IPVRTexturePixelBufferStrategy;

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

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pPVRTexturePixelBufferStrategy);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureStateListener);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pPVRTexturePixelBufferStrategy, pTextureStateListener);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureOptions);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pPVRTexturePixelBufferStrategy, pTextureOptions);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureOptions, pTextureStateListener);
	}

	public PVRGZTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pPVRTexturePixelBufferStrategy, pTextureOptions, pTextureStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public GZIPInputStream getInputStream() throws IOException {
		return new GZIPInputStream(this.onGetInputStream());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
