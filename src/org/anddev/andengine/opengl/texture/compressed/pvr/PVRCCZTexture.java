package org.anddev.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.ArrayUtils;
import org.anddev.andengine.util.StreamUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:17:23 - 27.07.2011
 */
public abstract class PVRCCZTexture extends PVRTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PVRCCZTexture(final PVRTextureFormat pPVRTextureFormat) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat);
	}

	public PVRCCZTexture(final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureStateListener);
	}

	public PVRCCZTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException, IOException {
		super(pPVRTextureFormat, pTextureOptions);
	}

	public PVRCCZTexture(final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException, IOException {
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
		final InputStream inputStream = this.getInputStream();

		final CCZHeader cczHeader = new CCZHeader(StreamUtils.streamToBytes(inputStream, CCZHeader.SIZE));

		return cczHeader.getCCZCompressionFormat().wrap(inputStream, cczHeader.getUncompressedSize());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class CCZHeader {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final byte[] MAGIC_IDENTIFIER = {
			(byte)'C',
			(byte)'C',
			(byte)'Z',
			(byte)'!'
		};

		public static final int SIZE = 16;

		// ===========================================================
		// Fields
		// ===========================================================

		private final ByteBuffer mDataByteBuffer;
		private final CCZCompressionFormat mCCZCompressionFormat;

		// ===========================================================
		// Constructors
		// ===========================================================

		public CCZHeader(final byte[] pData) {
			this.mDataByteBuffer = ByteBuffer.wrap(pData);
			this.mDataByteBuffer.rewind();
			this.mDataByteBuffer.order(ByteOrder.BIG_ENDIAN);

			/* Check magic bytes. */
			if(!ArrayUtils.equals(pData, 0, CCZHeader.MAGIC_IDENTIFIER, 0, CCZHeader.MAGIC_IDENTIFIER.length)) {
				throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + "!");
			}

			// TODO Check the version?

			this.mCCZCompressionFormat = CCZCompressionFormat.fromID(this.getCCZCompressionFormatID());
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		private short getCCZCompressionFormatID() {
			return this.mDataByteBuffer.getShort(4);
		}

		public CCZCompressionFormat getCCZCompressionFormat() {
			return this.mCCZCompressionFormat;
		}

		public short getVersion() {
			return this.mDataByteBuffer.getShort(6);
		}

		public int getUserdata() {
			return this.mDataByteBuffer.getInt(8);
		}

		public int getUncompressedSize() {
			return this.mDataByteBuffer.getInt(12);
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

	public static enum CCZCompressionFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		ZLIB((short)0),
		BZIP2((short)1),
		GZIP((short)2),
		NONE((short)3);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final short mID;

		// ===========================================================
		// Constructors
		// ===========================================================

		private CCZCompressionFormat(final short pID) {
			this.mID = pID;
		}

		public InputStream wrap(final InputStream pInputStream, final int pUncompressedSize) throws IOException {
			switch(this) {
				case GZIP:
					return new GZIPInputStream(pInputStream, pUncompressedSize);
				case ZLIB:
					return new InflaterInputStream(pInputStream, new Inflater(), pUncompressedSize);
				case NONE:
				case BZIP2:
				default:
					throw new IllegalArgumentException("Unexpected " + CCZCompressionFormat.class.getSimpleName() + ": '" + this + "'.");
			}
		}

		public static CCZCompressionFormat fromID(final short pID) {
			final CCZCompressionFormat[] cczCompressionFormats = CCZCompressionFormat.values();
			final int cczCompressionFormatCount = cczCompressionFormats.length;
			for(int i = 0; i < cczCompressionFormatCount; i++) {
				final CCZCompressionFormat cczCompressionFormat = cczCompressionFormats[i];
				if(cczCompressionFormat.mID == pID) {
					return cczCompressionFormat;
				}
			}
			throw new IllegalArgumentException("Unexpected " + CCZCompressionFormat.class.getSimpleName() + "-ID: '" + pID + "'.");
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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
