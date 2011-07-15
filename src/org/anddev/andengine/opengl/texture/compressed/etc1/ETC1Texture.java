package org.anddev.andengine.opengl.texture.compressed.etc1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.StreamUtils;

import android.opengl.ETC1;
import android.opengl.ETC1Util;

/**
 * TODO if(!SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.FROYO)) --> Meaningful Exception!
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:32:01 - 13.07.2011
 */
public abstract class ETC1Texture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private ETC1TextureHeader mETC1TextureHeader;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ETC1Texture() throws IOException {
		this(TextureOptions.DEFAULT, null);
	}

	public ETC1Texture(final ITextureStateListener pTextureStateListener) throws IOException {
		this(TextureOptions.DEFAULT, pTextureStateListener);
	}

	public ETC1Texture(final TextureOptions pTextureOptions) throws IOException {
		this(pTextureOptions, null);
	}

	public ETC1Texture(final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IOException {
		super(PixelFormat.RGB_565, pTextureOptions, pTextureStateListener);

		InputStream inputStream = null;
		try {
			inputStream = this.getInputStream();

			this.mETC1TextureHeader = new ETC1TextureHeader(StreamUtils.streamToBytes(inputStream, ETC1.ETC_PKM_HEADER_SIZE));
		} finally {
			StreamUtils.close(inputStream);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mETC1TextureHeader.getWidth();
	}

	@Override
	public int getHeight() {
		return this.mETC1TextureHeader.getHeight();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract InputStream getInputStream() throws IOException;

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
		final InputStream inputStream = this.getInputStream();
		ETC1Util.loadTexture(GL10.GL_TEXTURE_2D, 0, 0, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), inputStream);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ETC1TextureHeader {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final ByteBuffer mDataByteBuffer;

		private final int mWidth;
		private final int mHeight;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ETC1TextureHeader(final byte[] pData) {
			if(pData.length != ETC1.ETC_PKM_HEADER_SIZE) {
				throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + "!");
			}

			this.mDataByteBuffer = ByteBuffer.allocateDirect(ETC1.ETC_PKM_HEADER_SIZE).order(ByteOrder.nativeOrder());
			this.mDataByteBuffer.put(pData, 0, ETC1.ETC_PKM_HEADER_SIZE);
			this.mDataByteBuffer.position(0);

			if (!ETC1.isValid(this.mDataByteBuffer)) {
				throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + "!");
			}

			this.mWidth = ETC1.getWidth(this.mDataByteBuffer);
			this.mHeight = ETC1.getHeight(this.mDataByteBuffer);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getWidth() {
			return this.mWidth;
		}

		public int getHeight() {
			return this.mHeight;
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
}
