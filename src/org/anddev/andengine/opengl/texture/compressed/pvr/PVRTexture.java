package org.anddev.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRTexture.IPVRTexturePixelBufferStrategy.IPVRTexturePixelBufferStrategyBufferManager;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.ArrayUtils;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;
import org.anddev.andengine.util.data.ByteBufferOutputStream;
import org.anddev.andengine.util.data.DataConstants;
import org.anddev.andengine.util.exception.AndEngineException;
import org.anddev.andengine.util.math.MathUtils;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:18:10 - 13.07.2011
 */
public abstract class PVRTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int FLAG_MIPMAP = (1 << 8); // has mip map levels
	public static final int FLAG_TWIDDLE = (1 << 9); // is twiddled
	public static final int FLAG_BUMPMAP = (1 << 10); // has normals encoded for a bump map
	public static final int FLAG_TILING = (1 << 11); // is bordered for tiled pvr
	public static final int FLAG_CUBEMAP = (1 << 12); // is a cubemap/skybox
	public static final int FLAG_FALSEMIPCOL = (1 << 13); // are there false colored MIP levels
	public static final int FLAG_VOLUME = (1 << 14); // is this a volume texture
	public static final int FLAG_ALPHA = (1 << 15); // v2.1 is there transparency info in the texture
	public static final int FLAG_VERTICALFLIP = (1 << 16); // v2.1 is the texture vertically flipped

	// ===========================================================
	// Fields
	// ===========================================================

	private final PVRTextureHeader mPVRTextureHeader;
	private final IPVRTexturePixelBufferStrategy mPVRTexturePixelBufferStrategy;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), TextureOptions.DEFAULT, null);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, pPVRTexturePixelBufferStrategy, TextureOptions.DEFAULT, null);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), TextureOptions.DEFAULT, pTextureStateListener);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, pPVRTexturePixelBufferStrategy, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), pTextureOptions, null);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, pPVRTexturePixelBufferStrategy, pTextureOptions, null);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), pTextureOptions, pTextureStateListener);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat.getPixelFormat(), pTextureOptions, pTextureStateListener);
		this.mPVRTexturePixelBufferStrategy = pPVRTexturePixelBufferStrategy;

		InputStream inputStream = null;
		try {
			inputStream = this.getInputStream();
			this.mPVRTextureHeader = new PVRTextureHeader(StreamUtils.streamToBytes(inputStream, PVRTextureHeader.SIZE));
		} finally {
			StreamUtils.close(inputStream);
		}

		if(this.mPVRTextureHeader.getPVRTextureFormat().getPixelFormat() != pPVRTextureFormat.getPixelFormat()) {
			throw new IllegalArgumentException("Other PVRTextureFormat: '" + this.mPVRTextureHeader.getPVRTextureFormat().getPixelFormat() + "' found than expected: '" + pPVRTextureFormat.getPixelFormat() + "'.");
		}

		if(this.mPVRTextureHeader.getPVRTextureFormat().isCompressed()) { // TODO && ! GLHELPER_EXTENSION_PVRTC] ) {
			throw new IllegalArgumentException("Invalid PVRTextureFormat: '" + this.mPVRTextureHeader.getPVRTextureFormat() + "'.");
		}

		this.mUpdateOnHardwareNeeded = true;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mPVRTextureHeader.getWidth();
	}

	@Override
	public int getHeight() {
		return this.mPVRTextureHeader.getHeight();
	}

	public PVRTextureHeader getPVRTextureHeader() {
		return this.mPVRTextureHeader;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract InputStream onGetInputStream() throws IOException;

	protected InputStream getInputStream() throws IOException {
		return this.onGetInputStream();
	}

	@Override
	protected void writeTextureToHardware() throws IOException {
		final IPVRTexturePixelBufferStrategyBufferManager pvrTextureLoadStrategyManager = this.mPVRTexturePixelBufferStrategy.newPVRTexturePixelBufferStrategyManager(this);

		int width = this.getWidth();
		int height = this.getHeight();

		final int dataLength = this.mPVRTextureHeader.getDataLength();
		final int glFormat = this.mPixelFormat.getGLFormat();
		final int glType = this.mPixelFormat.getGLType();

		final int bytesPerPixel = this.mPVRTextureHeader.getBitsPerPixel() / DataConstants.BITS_PER_BYTE;

		GLState.clearGLError();

		final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(width) && MathUtils.isPowerOfTwo(height) && this.mPVRTextureHeader.mPVRTextureFormat != PVRTextureFormat.RGBA_4444;
		if(!useDefaultAlignment) {
			GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
		}

		int currentLevel = 0;
		int currentPixelDataOffset = 0;
		while (currentPixelDataOffset < dataLength) {
			if (currentLevel > 0 && (width != height || MathUtils.nextPowerOfTwo(width) != width)) {
				Debug.w(String.format("Mipmap level '%u' is not squared. Width: '%u', height: '%u'. Texture won't render correctly.", currentLevel, width, height));
			}

			final int currentPixelDataSize = height * width * bytesPerPixel;

			/* Load the current level. */
			this.mPVRTexturePixelBufferStrategy.loadPVRTextureData(pvrTextureLoadStrategyManager, width, height, bytesPerPixel, glFormat, glType, currentLevel, currentPixelDataOffset, currentPixelDataSize);

			currentPixelDataOffset += currentPixelDataSize;

			/* Prepare next mipmap level. */
			width = Math.max(width / 2, 1);
			height = Math.max(height / 2, 1);

			currentLevel++;
		}

		/* Restore default alignment. */
		if(!useDefaultAlignment) {
			GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected ByteBuffer getPVRDataBuffer() throws IOException {
		final InputStream inputStream = this.getInputStream();
		try {
			final ByteBufferOutputStream os = new ByteBufferOutputStream(DataConstants.BYTES_PER_KILOBYTE, DataConstants.BYTES_PER_MEGABYTE / 2);
			StreamUtils.copy(inputStream, os);
			return os.toByteBuffer();
		} finally {
			StreamUtils.close(inputStream);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class PVRTextureHeader {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final byte[] MAGIC_IDENTIFIER = {
			(byte)'P',
			(byte)'V',
			(byte)'R',
			(byte)'!'
		};

		public static final int SIZE = 13 * DataConstants.BYTES_PER_INT;
		private static final int FORMAT_FLAG_MASK = 0x0FF;

		// ===========================================================
		// Fields
		// ===========================================================

		private final ByteBuffer mDataByteBuffer;
		private final PVRTextureFormat mPVRTextureFormat;

		// ===========================================================
		// Constructors
		// ===========================================================

		public PVRTextureHeader(final byte[] pData) {
			this.mDataByteBuffer = ByteBuffer.wrap(pData);
			this.mDataByteBuffer.rewind();
			this.mDataByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

			/* Check magic bytes. */
			if(!ArrayUtils.equals(pData, 11 * DataConstants.BYTES_PER_INT, PVRTextureHeader.MAGIC_IDENTIFIER, 0, PVRTextureHeader.MAGIC_IDENTIFIER.length)) {
				throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + "!");
			}

			this.mPVRTextureFormat = PVRTextureFormat.fromID(this.getFlags() & PVRTextureHeader.FORMAT_FLAG_MASK);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public PVRTextureFormat getPVRTextureFormat() {
			return this.mPVRTextureFormat;
		}

		public int headerLength() {
			return this.mDataByteBuffer.getInt(0 * DataConstants.BYTES_PER_INT); // TODO Constants
		}

		public int getHeight() {
			return this.mDataByteBuffer.getInt(1 * DataConstants.BYTES_PER_INT);
		}

		public int getWidth() {
			return this.mDataByteBuffer.getInt(2 * DataConstants.BYTES_PER_INT);
		}

		public int getNumMipmaps() {
			return this.mDataByteBuffer.getInt(3 * DataConstants.BYTES_PER_INT);
		}

		public int getFlags() {
			return this.mDataByteBuffer.getInt(4 * DataConstants.BYTES_PER_INT);
		}

		public int getDataLength() {
			return this.mDataByteBuffer.getInt(5 * DataConstants.BYTES_PER_INT);
		}

		public int getBitsPerPixel() {
			return this.mDataByteBuffer.getInt(6 * DataConstants.BYTES_PER_INT);
		}

		public int getBitmaskRed() {
			return this.mDataByteBuffer.getInt(7 * DataConstants.BYTES_PER_INT);
		}

		public int getBitmaskGreen() {
			return this.mDataByteBuffer.getInt(8 * DataConstants.BYTES_PER_INT);
		}

		public int getBitmaskBlue() {
			return this.mDataByteBuffer.getInt(9 * DataConstants.BYTES_PER_INT);
		}

		public int getBitmaskAlpha() {
			return this.mDataByteBuffer.getInt(10 * DataConstants.BYTES_PER_INT);
		}

		public boolean hasAlpha() {
			return this.getBitmaskAlpha() != 0;
		}

		public int getPVRTag() {
			return this.mDataByteBuffer.getInt(11 * DataConstants.BYTES_PER_INT);
		}

		public int numSurfs() {
			return this.mDataByteBuffer.getInt(12 * DataConstants.BYTES_PER_INT);
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static enum PVRTextureFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		RGBA_4444(0x10, false, PixelFormat.RGBA_4444),
		RGBA_5551(0x11, false, PixelFormat.RGBA_5551),
		RGBA_8888(0x12, false, PixelFormat.RGBA_8888),
		RGB_565(0x13, false, PixelFormat.RGB_565),
		//		RGB_555( 0x14, ...),
		//		RGB_888( 0x15, ...),
		I_8(0x16, false, PixelFormat.I_8),
		AI_88(0x17, false, PixelFormat.AI_88),
		//		PVRTC_2(0x18, GL10.GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG, true, TextureFormat.???),
		//		PVRTC_4(0x19, GL10.GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG, true, TextureFormat.???),
		//		BGRA_8888(0x1A, GL10.GL_RGBA, TextureFormat.???),
		A_8(0x1B, false, PixelFormat.A_8);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mID;
		private final boolean mCompressed;
		private final PixelFormat mPixelFormat;

		// ===========================================================
		// Constructors
		// ===========================================================

		private PVRTextureFormat(final int pID, final boolean pCompressed, final PixelFormat pPixelFormat) {
			this.mID = pID;
			this.mCompressed = pCompressed;
			this.mPixelFormat = pPixelFormat;
		}

		public static PVRTextureFormat fromID(final int pID) {
			final PVRTextureFormat[] pvrTextureFormats = PVRTextureFormat.values();
			final int pvrTextureFormatCount = pvrTextureFormats.length;
			for(int i = 0; i < pvrTextureFormatCount; i++) {
				final PVRTextureFormat pvrTextureFormat = pvrTextureFormats[i];
				if(pvrTextureFormat.mID == pID) {
					return pvrTextureFormat;
				}
			}
			throw new IllegalArgumentException("Unexpected " + PVRTextureFormat.class.getSimpleName() + "-ID: '" + pID + "'.");
		}

		public static PVRTextureFormat fromPixelFormat(final PixelFormat pPixelFormat) throws IllegalArgumentException {
			switch(pPixelFormat) {
				case RGBA_8888:
					return PVRTextureFormat.RGBA_8888;
				case RGBA_4444:
					return PVRTextureFormat.RGBA_4444;
				case RGB_565:
					return PVRTextureFormat.RGB_565;
				default:
					throw new IllegalArgumentException("Unsupported " + PixelFormat.class.getName() + ": '" + pPixelFormat + "'.");
			}
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getID() {
			return this.mID;
		}

		public boolean isCompressed() {
			return this.mCompressed;
		}

		public PixelFormat getPixelFormat() {
			return this.mPixelFormat;
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

	public static interface IPVRTexturePixelBufferStrategy {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public abstract IPVRTexturePixelBufferStrategyBufferManager newPVRTexturePixelBufferStrategyManager(final PVRTexture pPVRTexture) throws IOException;

		public abstract void loadPVRTextureData(final IPVRTexturePixelBufferStrategyBufferManager pPVRTexturePixelBufferStrategyManager, final int pWidth, final int pHeight, final int pBytesPerPixel, final int pGLFormat, final int pGLType, final int pMipmapLevel, final int pCurrentPixelDataOffset, final int pCurrentPixelDataSize) throws IOException;

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

	public static class GreedyPVRTexturePixelBufferStrategy implements IPVRTexturePixelBufferStrategy {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public IPVRTexturePixelBufferStrategyBufferManager newPVRTexturePixelBufferStrategyManager(final PVRTexture pPVRTexture) throws IOException {
			return new GreedyPVRTexturePixelBufferStrategyBufferManager(pPVRTexture);
		}

		@Override
		public void loadPVRTextureData(final IPVRTexturePixelBufferStrategyBufferManager pPVRTexturePixelBufferStrategyManager, final int pWidth, final int pHeight, final int pBytesPerPixel, final int pGLFormat, final int pGLType, final int pLevel, final int pCurrentPixelDataOffset, final int pCurrentPixelDataSize) throws IOException {
			/* Adjust buffer. */
			final Buffer pixelBuffer = pPVRTexturePixelBufferStrategyManager.getPixelBuffer(PVRTextureHeader.SIZE + pCurrentPixelDataOffset, pCurrentPixelDataSize);

			/* Send to hardware. */
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, pLevel, pGLFormat, pWidth, pHeight, 0, pGLFormat, pGLType, pixelBuffer);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class SmartPVRTexturePixelBufferStrategy implements IPVRTexturePixelBufferStrategy {
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
		public void loadPVRTextureData(final IPVRTexturePixelBufferStrategyBufferManager pPVRTexturePixelBufferStrategyManager, final int pWidth, final int pHeight, final int pBytesPerPixel, final int pGLFormat, final int pGLType, final int pLevel, final int pCurrentPixelDataOffset, final int pCurrentPixelDataSize) throws IOException {
			/* Create the texture with the required parameters but without data. */
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, pLevel, pGLFormat, pWidth, pHeight, 0, pGLFormat, pGLType, null);

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
				GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, pLevel, 0, currentStripeOffsetY, pWidth, currentStripeHeight, pGLFormat, pGLType, pixelBuffer);

				GLState.checkGLError();

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
	}

	public static class GreedyPVRTexturePixelBufferStrategyBufferManager implements IPVRTexturePixelBufferStrategyBufferManager {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final ByteBuffer mByteBuffer;

		// ===========================================================
		// Constructors
		// ===========================================================

		public GreedyPVRTexturePixelBufferStrategyBufferManager(final PVRTexture pPVRTexture) throws IOException {
			this.mByteBuffer = pPVRTexture.getPVRDataBuffer();
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public ByteBuffer getPixelBuffer(final int pStart, final int pByteCount) {
			this.mByteBuffer.position(pStart);
			this.mByteBuffer.limit(pStart + pByteCount);

			return this.mByteBuffer.slice();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class SmartPVRTexturePixelBufferStrategyBufferManager implements IPVRTexturePixelBufferStrategyBufferManager {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int DATABUFFER_LENGTH_DEFAULT = 1024;

		// ===========================================================
		// Fields
		// ===========================================================

		private final InputStream mInputStream;
		private int mInputStreamPosition;

		private byte[] mData = new byte[SmartPVRTexturePixelBufferStrategyBufferManager.DATABUFFER_LENGTH_DEFAULT];

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
				throw new AndEngineException("Cannot read data that has been read already. (pStart: '" + pStart + "', this.mInputStreamPosition: '" + this.mInputStreamPosition + "')");
			}

			/* Ensure data buffer is bug enough. */
			if(this.mData.length < pByteCount) {
				this.mData = new byte[pByteCount];
			}

			/* Skip bytes up to where the data was requested. */
			if(this.mInputStreamPosition < pStart) {
				final int bytesToSkip = pStart - this.mInputStreamPosition;
				final long skipped = this.mInputStream.skip(bytesToSkip);

				this.mInputStreamPosition += skipped;

				if(bytesToSkip != skipped) {
					throw new AndEngineException("Skipped: '" + skipped + "' instead of '" + bytesToSkip + "'.");
				}
			}

			/* Read the data. */
			final int bytesToRead = pStart + pByteCount - this.mInputStreamPosition;
			StreamUtils.streamToBytes(this.mInputStream, bytesToRead, this.mData, 0);
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
