package org.anddev.andengine.opengl.texture.bitmap.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/**
 * 
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:39:22 - 10.08.2010
 */
public class FileBitmapTextureSource extends BaseBitmapTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private final File mFile;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public FileBitmapTextureSource(final File pFile) {
		this(0, 0, pFile);
	}

	public FileBitmapTextureSource(final int pTexturePositionX, final int pTexturePositionY, final File pFile) {
		super(pTexturePositionX, pTexturePositionY);
		this.mFile = pFile;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		InputStream in = null;
		try {
			in = new FileInputStream(pFile);
			BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in FileBitmapTextureSource. File: " + pFile, e);
		} finally {
			StreamUtils.close(in);
		}

		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
	}

	FileBitmapTextureSource(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final File pFile) {
		super(pTexturePositionX, pTexturePositionY);
		this.mFile = pFile;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public FileBitmapTextureSource clone() {
		return new FileBitmapTextureSource(this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mFile);
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
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = pBitmapConfig;

		InputStream in = null;
		try {
			in = new FileInputStream(this.mFile);
			return BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in " + this.getClass().getSimpleName() + ". File: " + this.mFile, e);
			return null;
		} finally {
			StreamUtils.close(in);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mFile + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}