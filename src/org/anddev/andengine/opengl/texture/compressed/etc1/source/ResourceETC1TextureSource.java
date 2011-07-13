package org.anddev.andengine.opengl.texture.compressed.etc1.source;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.anddev.andengine.opengl.texture.source.BaseTextureSource;
import org.anddev.andengine.util.StreamUtils;

import android.content.Context;
import android.opengl.ETC1;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:30:33 - 13.07.2010
 */
public class ResourceETC1TextureSource extends BaseTextureSource implements IETC1TextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private final int mRawResourceID;
	private final Context mContext;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourceETC1TextureSource(final Context pContext, final int pRawResourceID) throws IOException {
		this(pContext, 0, 0, pRawResourceID);
	}

	public ResourceETC1TextureSource(final Context pContext, final int pTexturePositionX, final int pTexturePositionY, final int pRawResourceID) throws IOException {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;

		final InputStream inputStream = this.onGetInputStream();
		final byte[] buf = new byte[4096];
		try {
			if (inputStream.read(buf, 0, ETC1.ETC_PKM_HEADER_SIZE) != ETC1.ETC_PKM_HEADER_SIZE) {
				throw new IOException("Unable to read PKM file header.");
			}
			final ByteBuffer headerBuffer = ByteBuffer.allocateDirect(ETC1.ETC_PKM_HEADER_SIZE).order(ByteOrder.nativeOrder());
			headerBuffer.put(buf, 0, ETC1.ETC_PKM_HEADER_SIZE).position(0);
			if (!ETC1.isValid(headerBuffer)) {
				throw new IOException("Not a PKM file.");
			}
			this.mWidth = ETC1.getWidth(headerBuffer);
			this.mHeight = ETC1.getHeight(headerBuffer);
		} catch(final IOException e) {
			StreamUtils.close(inputStream);
			throw e;
		}
	}

	protected ResourceETC1TextureSource(final Context pContext, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final int pRawResourceID) {
		super(pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public ResourceETC1TextureSource clone() {
		return new ResourceETC1TextureSource(this.mContext, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mRawResourceID);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	public InputStream onGetInputStream() {
		return this.mContext.getResources().openRawResource(this.mRawResourceID);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mRawResourceID + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}