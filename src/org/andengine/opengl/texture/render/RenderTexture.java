package org.andengine.opengl.texture.render;

import java.io.IOException;
import java.nio.IntBuffer;

import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.util.GLHelper;
import org.andengine.opengl.util.GLState;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;

/**
 *
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 07:13:05 - 24.08.2011
 */
public class RenderTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] VIEWPORT_CONTAINER = new int[4];
	private static final float[] CLEARCOLOR_CONTAINER = new float[4];

	private static final int VIEWPORT_CONTAINER_X_INDEX = 0;
	private static final int VIEWPORT_CONTAINER_Y_INDEX = RenderTexture.VIEWPORT_CONTAINER_X_INDEX + 1;
	private static final int VIEWPORT_CONTAINER_WIDTH_INDEX = RenderTexture.VIEWPORT_CONTAINER_Y_INDEX + 1;
	private static final int VIEWPORT_CONTAINER_HEIGHT_INDEX = RenderTexture.VIEWPORT_CONTAINER_WIDTH_INDEX + 1;

	private static final int CLEARCOLOR_CONTAINER_RED_INDEX = 0;
	private static final int CLEARCOLOR_CONTAINER_GREEN_INDEX = RenderTexture.CLEARCOLOR_CONTAINER_RED_INDEX + 1;
	private static final int CLEARCOLOR_CONTAINER_BLUE_INDEX = RenderTexture.CLEARCOLOR_CONTAINER_GREEN_INDEX + 1;
	private static final int CLEARCOLOR_CONTAINER_ALPHA_INDEX = RenderTexture.CLEARCOLOR_CONTAINER_BLUE_INDEX + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final PixelFormat mPixelFormat;
	private final int mWidth;
	private final int mHeight;

	private int mFramebufferObjectID;
	private int mPreviousFramebufferObjectID;
	private int mPreviousViewPortX;
	private int mPreviousViewPortY;
	private int mPreviousViewPortWidth;
	private int mPreviousViewPortHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderTexture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, PixelFormat.RGBA_8888);
	}

	public RenderTexture(final int pWidth, final int pHeight, final PixelFormat pPixelFormat) {
		super(pPixelFormat, TextureOptions.NEAREST, null);

		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.mPixelFormat = pPixelFormat;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GLState pGLState) {
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, this.mPixelFormat.getGLInternalFormat(), this.mWidth, this.mHeight, 0, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), null);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void init(final GLState pGLState) {
		this.savePreviousFramebufferObjectID(pGLState);

		try{
			this.loadToHardware(pGLState);
		} catch(final IOException e) {
			/* Can not happen. */
		}

		/* The texture to render to must not be bound. */
//		GLState.bindTexture(0);

		/* Generate FBO. */
		this.mFramebufferObjectID = pGLState.generateFramebuffer();
		pGLState.bindFramebuffer(this.mFramebufferObjectID);

		/* Attach texture to FBO. */
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, this.mTextureID, 0);

		pGLState.checkFramebufferStatus();

		this.restorePreviousFramebufferObjectID(pGLState);
	}

	public void begin(final GLState pGLState) {
		this.begin(pGLState, false, false);
	}

	public void begin(final GLState pGLState, final boolean pFlipX, final boolean pFlipY) {
		this.savePreviousViewport();
		GLES20.glViewport(0, 0, this.mWidth, this.mHeight);

		pGLState.pushProjectionGLMatrix();

		final float left;
		final float right;
		final float bottom;
		final float top;
		if(pFlipX) {
			left = this.mWidth;
			right = 0;
		} else {
			left = 0;
			right = this.mWidth;
		}
		if(pFlipY) {
			top = this.mHeight;
			bottom = 0;
		} else {
			top = 0;
			bottom = this.mHeight;
		}
		pGLState.orthoProjectionGLMatrixf(left, right, bottom, top, -1, 1);

		this.savePreviousFramebufferObjectID(pGLState);
		pGLState.bindFramebuffer(this.mFramebufferObjectID);

		pGLState.pushModelViewGLMatrix();
		pGLState.loadModelViewGLMatrixIdentity();
	}

	public void begin(final GLState pGLState, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.begin(pGLState, false, false, pRed, pGreen, pBlue, pAlpha);
	}

	public void begin(final GLState pGLState, final boolean pFlipX, final boolean pFlipY, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.begin(pGLState, pFlipX, pFlipY);

		/* Save clear color. */
		GLES20.glGetFloatv(GLES20.GL_COLOR_CLEAR_VALUE, RenderTexture.CLEARCOLOR_CONTAINER, 0);

		GLES20.glClearColor(pRed, pGreen, pBlue, pAlpha);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		/* Restore clear color. */
		GLES20.glClearColor(RenderTexture.CLEARCOLOR_CONTAINER[RenderTexture.CLEARCOLOR_CONTAINER_RED_INDEX], RenderTexture.CLEARCOLOR_CONTAINER[RenderTexture.CLEARCOLOR_CONTAINER_GREEN_INDEX], RenderTexture.CLEARCOLOR_CONTAINER[RenderTexture.CLEARCOLOR_CONTAINER_BLUE_INDEX], RenderTexture.CLEARCOLOR_CONTAINER[RenderTexture.CLEARCOLOR_CONTAINER_ALPHA_INDEX]);
	}

	public void end(final GLState pGLState) {
		pGLState.popModelViewGLMatrix();

		this.restorePreviousFramebufferObjectID(pGLState);

		pGLState.popProjectionGLMatrix();

		this.resotorePreviousViewport();
	}

	public void destroy(final GLState pGLState) {
		this.unloadFromHardware(pGLState);

		pGLState.deleteFramebuffer(this.mFramebufferObjectID);
	}

	private void savePreviousFramebufferObjectID(final GLState pGLState) {
		this.mPreviousFramebufferObjectID = pGLState.getActiveFramebuffer();
	}

	private void restorePreviousFramebufferObjectID(final GLState pGLState) {
		pGLState.bindFramebuffer(this.mPreviousFramebufferObjectID);
	}

	private void savePreviousViewport() {
		GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, RenderTexture.VIEWPORT_CONTAINER, 0);

		this.mPreviousViewPortX = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_X_INDEX];
		this.mPreviousViewPortY = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_Y_INDEX];
		this.mPreviousViewPortWidth = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_WIDTH_INDEX];
		this.mPreviousViewPortHeight = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_HEIGHT_INDEX];
	}

	private void resotorePreviousViewport() {
		GLES20.glViewport(this.mPreviousViewPortX, this.mPreviousViewPortY, this.mPreviousViewPortWidth, this.mPreviousViewPortHeight);
	}

	public int[] getPixelsARGB_8888(final GLState pGLState) {
		return this.getPixelsARGB_8888(pGLState, 0, 0, this.mWidth, this.mHeight);
	}

	public int[] getPixelsARGB_8888(final GLState pGLState, final int pX, final int pY, final int pWidth, final int pHeight) {
		final int[] pixelsRGBA_8888 = new int[pWidth * pHeight];
		final IntBuffer glPixelBuffer = IntBuffer.wrap(pixelsRGBA_8888);
		glPixelBuffer.position(0);

		this.begin(pGLState);
		GLES20.glReadPixels(pX, pY, pWidth, pHeight, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), glPixelBuffer);
		this.end(pGLState);

		return GLHelper.convertRGBA_8888toARGB_8888(pixelsRGBA_8888);
	}

	public Bitmap getBitmap(final GLState pGLState) {
		return this.getBitmap(pGLState, 0, 0, this.mWidth, this.mHeight);
	}

	public Bitmap getBitmap(final GLState pGLState, final int pX, final int pY, final int pWidth, final int pHeight) {
		if(this.mPixelFormat != PixelFormat.RGBA_8888){
			throw new IllegalStateException("Currently only 'PixelFormat." + PixelFormat.RGBA_8888 + "' is supported to be retrieved as a Bitmap.");
		}

		return Bitmap.createBitmap(this.getPixelsARGB_8888(pGLState, pX, pY, pWidth, pHeight), pWidth, pHeight, Config.ARGB_8888);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
