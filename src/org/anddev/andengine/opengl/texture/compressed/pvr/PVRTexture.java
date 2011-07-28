package org.anddev.andengine.opengl.texture.compressed.pvr;

import static org.anddev.andengine.util.constants.DataConstants.BYTES_PER_INT;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.ArrayUtils;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.StreamUtils;

/**
 * [16:32:42] Ricardo Quesada: "quick tip for PVR + NPOT + RGBA4444 textures: Don't forget to pack the bytes: glPixelStorei(GL_UNPACK_ALIGNMENT,1);"
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:18:10 - 13.07.2011
 * @see https://github.com/cocos2d/cocos2d-iphone/blob/develop/cocos2d/CCTexturePVR.m
 */
public abstract class PVRTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int FLAG_MIPMAP = (1<<8); // has mip map levels
	public static final int FLAG_TWIDDLE = (1<<9); // is twiddled
	public static final int FLAG_BUMPMAP = (1<<10); // has normals encoded for a bump map
	public static final int FLAG_TILING = (1<<11); // is bordered for tiled pvr
	public static final int FLAG_CUBEMAP = (1<<12); // is a cubemap/skybox
	public static final int FLAG_FALSEMIPCOL = (1<<13); // are there false colored MIP levels
	public static final int FLAG_VOLUME = (1<<14); // is this a volume texture
	public static final int FLAG_ALPHA = (1<<15); // v2.1 is there transparency info in the texture
	public static final int FLAG_VERTICALFLIP = (1<<16); // v2.1 is the texture vertically flipped

	// ===========================================================
	// Fields
	// ===========================================================

	private final PVRTextureHeader mPVRTextureHeader;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, TextureOptions.DEFAULT, null);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		this(pPVRTextureFormat, pTextureOptions, null);
	}

	public PVRTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat.getPixelFormat(), pTextureOptions, pTextureStateListener);

		InputStream inputStream = null;
		try {
			inputStream = this.onGetInputStream();
			this.mPVRTextureHeader = new PVRTextureHeader(StreamUtils.streamToBytes(inputStream, PVRTextureHeader.SIZE));
		} finally {
			StreamUtils.close(inputStream);
		}

		if(!MathUtils.isPowerOfTwo(this.getWidth()) || !MathUtils.isPowerOfTwo(this.getHeight())) {  // TODO GLHelper.EXTENSIONS_NON_POWER_OF_TWO
			throw new IllegalArgumentException("mWidth and mHeight must be a power of 2!");
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

	@Override
	protected void generateHardwareTextureID(final GL10 pGL) {
		//		// TODO
		//		if(this.mMipMapCount > 0) {
		pGL.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);
		//		}

		super.generateHardwareTextureID(pGL);
	}

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
		final InputStream inputStream = this.onGetInputStream();
		try {
			final byte[] data = StreamUtils.streamToBytes(inputStream);
			final ByteBuffer dataByteBuffer = ByteBuffer.wrap(data);
			dataByteBuffer.rewind();
			dataByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

			int width = this.getWidth();
			int height = this.getHeight();

			final int dataLength = this.mPVRTextureHeader.getDataLength();
			final int glFormat = this.mPixelFormat.getGLFormat();
			final int glType = this.mPixelFormat.getGLType();

			final int bytesPerPixel = this.mPVRTextureHeader.getBitsPerPixel() / 8;

			/* Calculate the data size for each texture level and respect the minimum number of blocks. */
			int mipmapLevel = 0;
			int currentPixelDataOffset = 0;
			while (currentPixelDataOffset < dataLength) {
				final int currentPixelDataSize = width * height * bytesPerPixel;
				final ByteBuffer pixelData = ByteBuffer.allocate(currentPixelDataSize).order(ByteOrder.nativeOrder());
				pixelData.put(data, PVRTextureHeader.SIZE + currentPixelDataOffset, currentPixelDataSize);

				if (mipmapLevel > 0 && (width != height || MathUtils.nextPowerOfTwo(width) != width)) {
					Debug.w(String.format("Mipmap level '%u' is not squared. Width: '%u', height: '%u'. Texture won't render correctly.", mipmapLevel, width, height));
				}

				pGL.glTexImage2D(GL10.GL_TEXTURE_2D, mipmapLevel, glFormat, width, height, 0, glFormat, glType, pixelData);

				GLHelper.checkGLError(pGL);

				currentPixelDataOffset += currentPixelDataSize;

				/* Prepare next mipmap level. */
				width = Math.max(width >> 1, 1);
				height = Math.max(height >> 1, 1);

				mipmapLevel++;
			}
		} finally {
			StreamUtils.close(inputStream);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

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

		public static final int SIZE = 13 * BYTES_PER_INT;
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
			if(!ArrayUtils.equals(pData, 11 * BYTES_PER_INT, MAGIC_IDENTIFIER, 0, MAGIC_IDENTIFIER.length)) {
				throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + "!");
			}

			this.mPVRTextureFormat = PVRTextureFormat.fromID(this.getFlags() & FORMAT_FLAG_MASK);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public PVRTextureFormat getPVRTextureFormat() {
			return this.mPVRTextureFormat;
		}

		public int headerLength() {
			return this.mDataByteBuffer.getInt(0 * BYTES_PER_INT); // TODO Constants
		}

		public int getHeight() {
			return this.mDataByteBuffer.getInt(1 * BYTES_PER_INT);
		}

		public int getWidth() {
			return this.mDataByteBuffer.getInt(2 * BYTES_PER_INT);
		}

		public int getNumMipmaps() {
			return this.mDataByteBuffer.getInt(3 * BYTES_PER_INT);
		}

		public int getFlags() {
			return this.mDataByteBuffer.getInt(4 * BYTES_PER_INT);
		}

		public int getDataLength() {
			return this.mDataByteBuffer.getInt(5 * BYTES_PER_INT);
		}

		public int getBitsPerPixel() {
			return this.mDataByteBuffer.getInt(6 * BYTES_PER_INT);
		}

		public int getBitmaskRed() {
			return this.mDataByteBuffer.getInt(7 * BYTES_PER_INT);
		}

		public int getBitmaskGreen() {
			return this.mDataByteBuffer.getInt(8 * BYTES_PER_INT);
		}

		public int getBitmaskBlue() {
			return this.mDataByteBuffer.getInt(9 * BYTES_PER_INT);
		}

		public int getBitmaskAlpha() {
			return this.mDataByteBuffer.getInt(10 * BYTES_PER_INT);
		}

		public boolean hasAlpha() {
			return this.getBitmaskAlpha() != 0;
		}

		public int getPVRTag() {
			return this.mDataByteBuffer.getInt(11 * BYTES_PER_INT);
		}

		public int numSurfs() {
			return this.mDataByteBuffer.getInt(12 * BYTES_PER_INT);
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
		AI_88(0x17, false, PixelFormat.AI_8),
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
