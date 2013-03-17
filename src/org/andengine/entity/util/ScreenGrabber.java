package org.andengine.entity.util;

import java.nio.IntBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.opengl.util.GLHelper;
import org.andengine.opengl.util.GLState;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:27:22 - 10.01.2011
 */
public class ScreenGrabber extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mGrabX;
	private int mGrabY;
	private int mGrabWidth;
	private int mGrabHeight;

	private boolean mScreenGrabPending;
	private IScreenGrabberCallback mScreenGrabCallback;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		if (this.mScreenGrabPending) {
			try {
				final Bitmap screenGrab = ScreenGrabber.grab(this.mGrabX, this.mGrabY, this.mGrabWidth, this.mGrabHeight);

				this.mScreenGrabCallback.onScreenGrabbed(screenGrab);
			} catch (final Exception e) {
				this.mScreenGrabCallback.onScreenGrabFailed(e);
			}

			this.mScreenGrabPending = false;
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

	public void grab(final int pGrabWidth, final int pGrabHeight, final IScreenGrabberCallback pScreenGrabCallback) {
		this.grab(0, 0, pGrabWidth, pGrabHeight, pScreenGrabCallback);
	}

	public void grab(final int pGrabX, final int pGrabY, final int pGrabWidth, final int pGrabHeight, final IScreenGrabberCallback pScreenGrabCallback) {
		this.mGrabX = pGrabX;
		this.mGrabY = pGrabY;
		this.mGrabWidth = pGrabWidth;
		this.mGrabHeight = pGrabHeight;
		this.mScreenGrabCallback = pScreenGrabCallback;

		this.mScreenGrabPending = true;
	}

	private static Bitmap grab(final int pGrabX, final int pGrabY, final int pGrabWidth, final int pGrabHeight) {
		final int[] pixelsRGBA_8888 = new int[pGrabWidth * pGrabHeight];
		final IntBuffer pixelsRGBA_8888_Buffer = IntBuffer.wrap(pixelsRGBA_8888);

		// TODO Check availability of OpenGL and GLES20.GL_RGBA combinations that require less conversion operations.
		GLES20.glReadPixels(pGrabX, pGrabY, pGrabWidth, pGrabHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelsRGBA_8888_Buffer);

		/* Convert from RGBA_8888 (Which is actually ABGR as the whole buffer seems to be inverted) --> ARGB_8888. */
		final int[] pixelsARGB_8888 = GLHelper.convertRGBA_8888toARGB_8888(pixelsRGBA_8888);

		return Bitmap.createBitmap(pixelsARGB_8888, pGrabWidth, pGrabHeight, Config.ARGB_8888);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IScreenGrabberCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onScreenGrabbed(final Bitmap pBitmap);
		public void onScreenGrabFailed(final Exception pException);
	}
}
