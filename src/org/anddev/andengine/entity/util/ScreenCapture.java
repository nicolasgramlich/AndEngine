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

	private int mWidth;
	private int mHeight;

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
			saveCapture(this.mWidth, this.mHeight, this.mFilePath, pGL);

			this.mScreenCaptureCallback.onScreenCaptured(this.mFilePath);

			this.mScreenCapturePending = false;
			this.mFilePath = null;
			this.mScreenCaptureCallback = null;
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

	public void capture(final int pWidth, final int pHeight, final String pFilePath, final IScreenCaptureCallback pScreenCaptureCallback) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mFilePath = pFilePath;
		this.mScreenCaptureCallback = pScreenCaptureCallback;
		this.mScreenCapturePending = true;
	}

	private static Bitmap capture(final int pX, final int pY, final int pWidth, final int pHeight, final GL10 pGL) {
		// TODO FIXME Use this code http://blog.javia.org/android-opengl-es-screenshot/ and fix bugs.
		final int b[] = new int[pWidth * (pY + pHeight)];
		final int bt[] = new int[pWidth * pHeight];
		final IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		pGL.glReadPixels(pX, 0, pWidth, pY + pHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

		for (int i = 0, k = 0; i < pHeight; i++, k++) {
			for (int j = 0; j < pWidth; j++) {
				final int pix = b[i * pWidth + j];
				final int pb = (pix >> 16) & 0xff;
				final int pr = (pix << 16) & 0x00ff0000;
				final int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(pHeight - k - 1) * pWidth + j] = pix1;
			}
		}

		return Bitmap.createBitmap(bt, pWidth, pHeight, Config.ARGB_8888);
	}

	private static void saveCapture(final int pWidth, final int pHeight, final String pFilePath, final GL10 pGL) {
		saveCapture(0, 0, pWidth, pHeight, pFilePath, pGL);
	}

	private static void saveCapture(final int pX, final int pY, final int pWidth, final int pHeight, final String pFilePath, final GL10 pGL) {
		final Bitmap bmp = capture(pX, pY, pWidth, pHeight, pGL);
		try {
			final FileOutputStream fos = new FileOutputStream(pFilePath);
			bmp.compress(CompressFormat.PNG, 100, fos);

			StreamUtils.flushCloseStream(fos);
		} catch (final FileNotFoundException e) {
			Debug.e("Error saving file to: " + pFilePath, e);
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
	}
}
