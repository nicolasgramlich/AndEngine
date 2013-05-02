package org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.compressed.pvr.PVRTexture;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:26:07 - 27.09.2011
 */
public interface IPVRTexturePixelBufferStrategy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public IPVRTexturePixelBufferStrategyBufferManager newPVRTexturePixelBufferStrategyManager(final PVRTexture pPVRTexture) throws IOException;

	public void loadPVRTextureData(final IPVRTexturePixelBufferStrategyBufferManager pPVRTexturePixelBufferStrategyManager, final int pWidth, final int pHeight, final int pBytesPerPixel, final PixelFormat pPixelFormat, final int pMipmapLevel, final int pCurrentPixelDataOffset, final int pCurrentPixelDataSize) throws IOException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPVRTexturePixelBufferStrategyBufferManager {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public ByteBuffer getPixelBuffer(final int pStart, final int pByteCount) throws IOException;
	}
}

