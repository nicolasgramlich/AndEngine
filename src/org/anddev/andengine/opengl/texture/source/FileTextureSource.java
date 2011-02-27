package org.anddev.andengine.opengl.texture.source;

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
 * @author Nicolas Gramlich
 * @since 16:39:22 - 10.08.2010
 */
public class FileTextureSource implements ITextureSource {
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

	public FileTextureSource(final File pFile) {
		this.mFile = pFile;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		InputStream in = null;
		try {
			in = new FileInputStream(pFile);
			BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in FileTextureSource. File: " + pFile, e);
		} finally {
			StreamUtils.close(in);
		}

		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
	}

	FileTextureSource(final File pFile, final int pWidth, final int pHeight) {
		this.mFile = pFile;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public FileTextureSource clone() {
		return new FileTextureSource(this.mFile, this.mWidth, this.mHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public Bitmap onLoadBitmap() {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = Config.ARGB_8888;

		InputStream in = null;
		try {
			in = new FileInputStream(this.mFile);
			return BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in FileTextureSource. File: " + this.mFile, e);
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