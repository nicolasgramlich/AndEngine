package org.andengine.opengl.texture.compressed.etc1;

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
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;
import org.andengine.util.StreamUtils;
import org.andengine.util.math.MathUtils;

import android.opengl.ETC1;
import android.opengl.ETC1Util;
import android.opengl.GLES20;

/**
 * TODO if (!SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.FROYO)) --> Meaningful Exception!
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

	public ETC1Texture(final TextureManager pTextureManager) throws IOException {
		this(pTextureManager, TextureOptions.DEFAULT, null);
	}

	public ETC1Texture(final TextureManager pTextureManager, final ITextureStateListener pTextureStateListener) throws IOException {
		this(pTextureManager, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public ETC1Texture(final TextureManager pTextureManager, final TextureOptions pTextureOptions) throws IOException {
		this(pTextureManager, pTextureOptions, null);
	}

	public ETC1Texture(final TextureManager pTextureManager, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IOException {
		super(pTextureManager, PixelFormat.RGB_565, pTextureOptions, pTextureStateListener);

		InputStream inputStream = null;
		try {
			inputStream = this.getInputStream();

			this.mETC1TextureHeader = new ETC1TextureHeader(StreamUtils.streamToBytes(inputStream, ETC1.ETC_PKM_HEADER_SIZE));

			if (BuildConfig.DEBUG) {
				if (!(MathUtils.isPowerOfTwo(this.mETC1TextureHeader.mWidth) && MathUtils.isPowerOfTwo(this.mETC1TextureHeader.mHeight))) {
					Debug.w("ETC1 textures with NPOT sizes can cause a crash on PowerVR GPUs!");
				}
			}
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
	protected void writeTextureToHardware(final GLState pGLState) throws IOException {
		final InputStream inputStream = this.getInputStream();
		ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D, 0, 0, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), inputStream);
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
			if (pData.length != ETC1.ETC_PKM_HEADER_SIZE) {
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
