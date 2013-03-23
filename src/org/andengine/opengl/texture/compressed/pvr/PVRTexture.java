package org.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.andengine.BuildConfig;
import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy.GreedyPVRTexturePixelBufferStrategy;
import org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy.IPVRTexturePixelBufferStrategy;
import org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy.IPVRTexturePixelBufferStrategy.IPVRTexturePixelBufferStrategyBufferManager;
import org.andengine.opengl.util.GLState;
import org.andengine.util.StreamUtils;
import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.adt.data.constants.DataConstants;
import org.andengine.util.adt.io.out.ByteBufferOutputStream;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

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

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), TextureOptions.DEFAULT, null);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, pPVRTexturePixelBufferStrategy, TextureOptions.DEFAULT, null);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), TextureOptions.DEFAULT, pTextureStateListener);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, pPVRTexturePixelBufferStrategy, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), pTextureOptions, null);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, pPVRTexturePixelBufferStrategy, pTextureOptions, null);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pTextureManager, pPVRTextureFormat, new GreedyPVRTexturePixelBufferStrategy(), pTextureOptions, pTextureStateListener);
	}

	public PVRTexture(final TextureManager pTextureManager, final PVRTextureFormat pPVRTextureFormat, final IPVRTexturePixelBufferStrategy pPVRTexturePixelBufferStrategy, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pTextureManager, pPVRTextureFormat.getPixelFormat(), pTextureOptions, pTextureStateListener);
		this.mPVRTexturePixelBufferStrategy = pPVRTexturePixelBufferStrategy;

		InputStream inputStream = null;
		try {
			inputStream = this.getInputStream();
			this.mPVRTextureHeader = new PVRTextureHeader(StreamUtils.streamToBytes(inputStream, PVRTextureHeader.SIZE));
		} finally {
			StreamUtils.close(inputStream);
		}

		if (this.mPVRTextureHeader.getPVRTextureFormat().getPixelFormat() != pPVRTextureFormat.getPixelFormat()) {
			throw new IllegalArgumentException("Other PVRTextureFormat: '" + this.mPVRTextureHeader.getPVRTextureFormat().getPixelFormat() + "' found than expected: '" + pPVRTextureFormat.getPixelFormat() + "'.");
		}

		if (this.mPVRTextureHeader.getPVRTextureFormat().isCompressed()) { // TODO && ! GLHELPER_EXTENSION_PVRTC] ) {
			throw new IllegalArgumentException("Invalid PVRTextureFormat: '" + this.mPVRTextureHeader.getPVRTextureFormat() + "'.");
		}

		if (this.hasMipMaps()) {
			switch (pTextureOptions.mMinFilter) {
				case GLES20.GL_NEAREST_MIPMAP_NEAREST:
				case GLES20.GL_NEAREST_MIPMAP_LINEAR:
				case GLES20.GL_LINEAR_MIPMAP_NEAREST:
				case GLES20.GL_LINEAR_MIPMAP_LINEAR:
					break;
				default:
					if (BuildConfig.DEBUG) {
						Debug.w("This '" + this.getClass().getSimpleName() + "' contains mipmaps, but the provided '" + pTextureOptions.getClass().getSimpleName() + "' don't have MipMaps enabled on the MinFilter!");
					}
			}
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

	public boolean hasMipMaps() {
		return this.mPVRTextureHeader.getNumMipmaps() > 0;
	}

	public PVRTextureHeader getPVRTextureHeader() {
		return this.mPVRTextureHeader;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract InputStream onGetInputStream() throws IOException;

	public InputStream getInputStream() throws IOException {
		return this.onGetInputStream();
	}

	@Override
	protected void writeTextureToHardware(final GLState pGLState) throws IOException {
		final IPVRTexturePixelBufferStrategyBufferManager pvrTextureLoadStrategyManager = this.mPVRTexturePixelBufferStrategy.newPVRTexturePixelBufferStrategyManager(this);

		int width = this.getWidth();
		int height = this.getHeight();

		final int dataLength = this.mPVRTextureHeader.getDataLength();

		final int bytesPerPixel = this.mPVRTextureHeader.getBitsPerPixel() / DataConstants.BITS_PER_BYTE;

		/* Adjust unpack alignment. */
		GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);

		int currentLevel = 0;
		int currentPixelDataOffset = 0;
		while (currentPixelDataOffset < dataLength) {
			if (currentLevel > 0 && (width != height || MathUtils.nextPowerOfTwo(width) != width)) {
				Debug.w("Mipmap level '" + currentLevel + "' is not squared. Width: '" + width + "', height: '" + height + "'. Texture won't render correctly.");
			}

			final int currentPixelDataSize = height * width * bytesPerPixel;

			/* Load the current level. */
			this.mPVRTexturePixelBufferStrategy.loadPVRTextureData(pvrTextureLoadStrategyManager, width, height, bytesPerPixel, this.mPixelFormat, currentLevel, currentPixelDataOffset, currentPixelDataSize);

			currentPixelDataOffset += currentPixelDataSize;

			/* Prepare next mipmap level. */
			width = Math.max(width / 2, 1);
			height = Math.max(height / 2, 1);

			currentLevel++;
		}

		/* Restore default unpack alignment. */
		GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public ByteBuffer getPVRTextureBuffer() throws IOException {
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

		static final byte[] MAGIC_IDENTIFIER = {
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
			if (!ArrayUtils.equals(pData, 11 * DataConstants.BYTES_PER_INT, PVRTextureHeader.MAGIC_IDENTIFIER, 0, PVRTextureHeader.MAGIC_IDENTIFIER.length)) {
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
			for (int i = 0; i < pvrTextureFormatCount; i++) {
				final PVRTextureFormat pvrTextureFormat = pvrTextureFormats[i];
				if (pvrTextureFormat.mID == pID) {
					return pvrTextureFormat;
				}
			}
			throw new IllegalArgumentException("Unexpected " + PVRTextureFormat.class.getSimpleName() + "-ID: '" + pID + "'.");
		}

		public static PVRTextureFormat fromPixelFormat(final PixelFormat pPixelFormat) throws IllegalArgumentException {
			switch (pPixelFormat) {
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
}