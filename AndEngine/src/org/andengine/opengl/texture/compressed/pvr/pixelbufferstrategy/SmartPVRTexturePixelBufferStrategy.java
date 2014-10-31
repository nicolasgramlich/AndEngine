package org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.compressed.pvr.PVRTexture;
import org.andengine.opengl.texture.compressed.pvr.PVRTexture.PVRTextureHeader;
import org.andengine.util.StreamUtils;
import org.andengine.util.exception.AndEngineRuntimeException;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:26:07 - 27.09.2011
 */
public class SmartPVRTexturePixelBufferStrategy implements IPVRTexturePixelBufferStrategy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mAllocationSizeMaximum;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SmartPVRTexturePixelBufferStrategy(final int pAllocationSizeMaximum) {
		this.mAllocationSizeMaximum = pAllocationSizeMaximum;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IPVRTexturePixelBufferStrategyBufferManager newPVRTexturePixelBufferStrategyManager(final PVRTexture pPVRTexture) throws IOException {
		return new SmartPVRTexturePixelBufferStrategyBufferManager(pPVRTexture);
	}

	@Override
	public void loadPVRTextureData(final IPVRTexturePixelBufferStrategyBufferManager pPVRTexturePixelBufferStrategyManager, final int pWidth, final int pHeight, final int pBytesPerPixel, final PixelFormat pPixelFormat, final int pLevel, final int pCurrentPixelDataOffset, final int pCurrentPixelDataSize) throws IOException {
		final int glFormat = pPixelFormat.getGLFormat();
		final int glType = pPixelFormat.getGLType();

		/* Create the texture with the required parameters but without data. */
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, pLevel, pPixelFormat.getGLInternalFormat(), pWidth, pHeight, 0, glFormat, glType, null);

		final int bytesPerRow = pWidth * pBytesPerPixel;
		final int stripeHeight = Math.max(1, this.mAllocationSizeMaximum / bytesPerRow);

		/* Load stripes. */
		int currentStripePixelDataOffset = pCurrentPixelDataOffset;
		int currentStripeOffsetY = 0;
		while(currentStripeOffsetY < pHeight) {
			final int currentStripeHeight = Math.min(pHeight - currentStripeOffsetY, stripeHeight);
			final int currentStripePixelDataSize = currentStripeHeight * bytesPerRow;

			/* Adjust buffer. */
			final Buffer pixelBuffer = pPVRTexturePixelBufferStrategyManager.getPixelBuffer(PVRTextureHeader.SIZE + currentStripePixelDataOffset, currentStripePixelDataSize);

			/* Send to hardware. */
			GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, pLevel, 0, currentStripeOffsetY, pWidth, currentStripeHeight, glFormat, glType, pixelBuffer);

			currentStripePixelDataOffset += currentStripePixelDataSize;
			currentStripeOffsetY += currentStripeHeight;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class SmartPVRTexturePixelBufferStrategyBufferManager implements IPVRTexturePixelBufferStrategyBufferManager {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final InputStream mInputStream;
		private int mInputStreamPosition;

		private byte[] mData;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SmartPVRTexturePixelBufferStrategyBufferManager(final PVRTexture pPVRTexture) throws IOException {
			this.mInputStream = pPVRTexture.getInputStream();
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public ByteBuffer getPixelBuffer(final int pStart, final int pByteCount) throws IOException {
			if(pStart < this.mInputStreamPosition) {
				throw new AndEngineRuntimeException("Cannot read data that has been read already. (pStart: '" + pStart + "', this.mInputStreamPosition: '" + this.mInputStreamPosition + "')");
			}

			/* Ensure data buffer is bug enough. */
			if(this.mData == null || this.mData.length < pByteCount) {
				this.mData = new byte[pByteCount];
			}

			/* If needed, skip bytes up to where the data was requested. */
			if(this.mInputStreamPosition < pStart) {
				final int bytesToSkip = pStart - this.mInputStreamPosition;
				final long skipped = this.mInputStream.skip(bytesToSkip);

				this.mInputStreamPosition += skipped;

				if(bytesToSkip != skipped) {
					throw new AndEngineRuntimeException("Skipped: '" + skipped + "' instead of '" + bytesToSkip + "'.");
				}
			}

			/* Read the requested data. */
			final int bytesToRead = pStart + pByteCount - this.mInputStreamPosition;
			StreamUtils.streamToBytes(this.mInputStream, bytesToRead, this.mData);
			this.mInputStreamPosition += bytesToRead;

			/* Return as a buffer. */
			return ByteBuffer.wrap(this.mData, 0, pByteCount);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}