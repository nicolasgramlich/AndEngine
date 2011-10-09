package org.anddev.andengine.opengl.texture.render;

import java.io.IOException;
import java.nio.IntBuffer;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.math.MathUtils;

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

	private static final int VIEWPORT_CONTAINER_X = 0;
	private static final int VIEWPORT_CONTAINER_Y = 1;
	private static final int VIEWPORT_CONTAINER_WIDTH = 2;
	private static final int VIEWPORT_CONTAINER_HEIGHT = 3;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;
	private final PixelFormat mPixelFormat;
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

		if(!MathUtils.isPowerOfTwo(pWidth) || !MathUtils.isPowerOfTwo(pHeight)) {
			throw new IllegalArgumentException("pWidth (" + pWidth + ") and pHeight (" + pHeight + ") must be powers of two!");
		}

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
	protected void writeTextureToHardware() {
		final int glFormat = this.mPixelFormat.getGLFormat();
		final int glType = this.mPixelFormat.getGLType();
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, glFormat, this.mWidth, this.mHeight, 0, glFormat, glType, null);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void init() {
		this.savePreviousFramebufferObjectID();

		try{
			this.loadToHardware();
		} catch(final IOException e) {
			/* Can not happen. */
		}

		/* The texture to render to must not be bound. */
//		GLState.bindTexture(0);

		/* Generate FBO. */
		this.mFramebufferObjectID = GLState.generateFramebuffer();
		GLState.bindFramebuffer(this.mFramebufferObjectID);

		/* Attach texture to FBO. */
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, this.mTextureID, 0);

		GLState.checkFramebufferStatus();

		this.restorePreviousFramebufferObjectID();
	}

	public void begin() {
		this.savePreviousViewport();
		GLES20.glViewport(0, 0, this.mWidth, this.mHeight);

		GLState.pushProjectionGLMatrix();
		GLState.orthoProjectionGLMatrixf(0, this.mWidth, this.mHeight, 0, -1, 1);

		this.savePreviousFramebufferObjectID();
		GLState.bindFramebuffer(this.mFramebufferObjectID);

		GLState.pushModelViewGLMatrix();
		GLState.loadModelViewGLMatrixIdentity();
	}

	public void end() {
		GLState.popModelViewGLMatrix();

		this.restorePreviousFramebufferObjectID();

		GLState.popProjectionGLMatrix();

		this.resotorePreviousViewport();
	}

	public void destroy() {
		this.unloadFromHardware();

		GLState.deleteFramebuffer(this.mFramebufferObjectID);
	}

	private void savePreviousFramebufferObjectID() {
		this.mPreviousFramebufferObjectID = GLState.getActiveFramebuffer();
	}

	private void restorePreviousFramebufferObjectID() {
		GLState.bindFramebuffer(this.mPreviousFramebufferObjectID);
	}

	private void savePreviousViewport() {
		GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, RenderTexture.VIEWPORT_CONTAINER, 0);

		this.mPreviousViewPortX = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_X];
		this.mPreviousViewPortY = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_Y];
		this.mPreviousViewPortWidth = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_WIDTH];
		this.mPreviousViewPortHeight = RenderTexture.VIEWPORT_CONTAINER[RenderTexture.VIEWPORT_CONTAINER_HEIGHT];
	}

	private void resotorePreviousViewport() {
		GLES20.glViewport(this.mPreviousViewPortX, this.mPreviousViewPortY, this.mPreviousViewPortWidth, this.mPreviousViewPortHeight);
	}

	public int[] getPixelsARGB_8888() {
		return this.getPixelsARGB_8888(0, 0, this.mWidth, this.mHeight, false);
	}

	public int[] getPixelsARGB_8888(final int pX, final int pY, final int pWidth, final int pHeight, final boolean pFlipVertical) {
		final int[] pixelsRGBA_8888 = new int[pWidth * pHeight];
		final IntBuffer glPixelBuffer = IntBuffer.wrap(pixelsRGBA_8888);
		glPixelBuffer.position(0);

		this.begin();
		/* Convert to GL coordinates. */
		final int x = pX;
		final int y = this.mHeight - pHeight - pY;
		GLES20.glReadPixels(x, y, pWidth, pHeight, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), glPixelBuffer);
		this.end();

		if(pFlipVertical) {
			return GLHelper.convertRGBA_8888toARGB_8888_FlippedVertical(pixelsRGBA_8888, pWidth, pHeight);
		} else {
			return GLHelper.convertRGBA_8888toARGB_8888(pixelsRGBA_8888);
		}
	}

	public Bitmap getBitmap() {
		return this.getBitmap(0, 0, this.mWidth, this.mHeight, false);
	}

	public Bitmap getBitmap(final boolean pFlipVertical) {
		return this.getBitmap(0, 0, this.mWidth, this.mHeight, pFlipVertical);
	}

	public Bitmap getBitmap(final int pX, final int pY, final int pWidth, final int pHeight, final boolean pFlipVertical) {
		if(this.mPixelFormat != PixelFormat.RGBA_8888){
			throw new IllegalStateException("Currently only 'PixelFormat." + PixelFormat.RGBA_8888 + "' is supported to be retrieved as a Bitmap.");
		}

		return Bitmap.createBitmap(this.getPixelsARGB_8888(pX, pY, pWidth, pHeight, pFlipVertical), pWidth, pHeight, Config.ARGB_8888);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
