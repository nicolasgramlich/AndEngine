package org.anddev.andengine.entity.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;

/**
 * @author Nicolas Gramlich
 * @since 15:11:50 - 15.03.2010
 */
public class ScreenCapture extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mCaptureX;
	private int mCaptureY;
	private int mCaptureWidth;
	private int mCaptureHeight;

	private boolean mScreenCapturePending = false;
	private IScreenCaptureCallback mScreenCaptureCallback;
	private String mFilePath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScreenCapture() {
		super(0, 0);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		if(this.mScreenCapturePending) {
			try {
				ScreenCapture.saveCapture(this.mCaptureX, this.mCaptureY, this.mCaptureWidth, this.mCaptureHeight, this.mFilePath, pGL);

				this.mScreenCaptureCallback.onScreenCaptured(this.mFilePath);
			} catch (final Exception e) {
				this.mScreenCaptureCallback.onScreenCaptureFailed(this.mFilePath, e);
			}

			this.mScreenCapturePending = false;
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		/* Nothing */
	}

	@Override
	public void reset() {
		/* Nothing */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void capture(final int pCaptureWidth, final int pCaptureHeight, final String pFilePath, final IScreenCaptureCallback pScreenCaptureCallback) {
		this.capture(0, 0, pCaptureWidth, pCaptureHeight, pFilePath, pScreenCaptureCallback);
	}

	public void capture(final int pCaptureX, final int pCaptureY, final int pCaptureWidth, final int pCaptureHeight, final String pFilePath, final IScreenCaptureCallback pScreenCaptureCallback) {
		this.mCaptureX = pCaptureX;
		this.mCaptureY = pCaptureY;
		this.mCaptureWidth = pCaptureWidth;
		this.mCaptureHeight = pCaptureHeight;
		this.mFilePath = pFilePath;
		this.mScreenCaptureCallback = pScreenCaptureCallback;

		this.mScreenCapturePending = true;
	}

	private static Bitmap capture(final int pCaptureX, final int pCaptureY, final int pCaptureWidth, final int pCaptureHeight, final GL10 pGL) {
		final int[] source = new int[pCaptureWidth * (pCaptureY + pCaptureHeight)];
		final IntBuffer sourceBuffer = IntBuffer.wrap(source);
		sourceBuffer.position(0);

		// TODO Check availability of OpenGL and GL10.GL_RGBA combinations that require less conversion operations.
		// Note: There is (said to be) a bug with glReadPixels when 'y != 0', so we simply read starting from 'y == 0'.
		pGL.glReadPixels(pCaptureX, 0, pCaptureWidth, pCaptureY + pCaptureHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, sourceBuffer);

		final int[] pixels = new int[pCaptureWidth * pCaptureHeight];

		// Convert from RGBA_8888 (Which is actually ABGR as the whole buffer seems to be inverted) --> ARGB_8888
		for (int y = 0; y < pCaptureHeight; y++) {
			for (int x = 0; x < pCaptureWidth; x++) {
				final int pixel = source[x + ((pCaptureY + y) * pCaptureWidth)];

				final int blue = (pixel & 0x00FF0000) >> 16;
			final int red = (pixel  & 0x000000FF) << 16;
			final int greenAlpha = pixel & 0xFF00FF00;

			pixels[x + ((pCaptureHeight - y - 1) * pCaptureWidth)] = greenAlpha | red | blue;
			}
		}

		return Bitmap.createBitmap(pixels, pCaptureWidth, pCaptureHeight, Config.ARGB_8888);
	}

	private static void saveCapture(final int pCaptureX, final int pCaptureY, final int pCaptureWidth, final int pCaptureHeight, final String pFilePath, final GL10 pGL) throws FileNotFoundException {
		final Bitmap bmp = ScreenCapture.capture(pCaptureX, pCaptureY, pCaptureWidth, pCaptureHeight, pGL);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pFilePath);
			bmp.compress(CompressFormat.PNG, 100, fos);

		} catch (final FileNotFoundException e) {
			StreamUtils.flushCloseStream(fos);
			Debug.e("Error saving file to: " + pFilePath, e);
			throw e;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IScreenCaptureCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onScreenCaptured(final String pFilePath);
		public void onScreenCaptureFailed(final String pFilePath, final Exception pException);
	}
}
