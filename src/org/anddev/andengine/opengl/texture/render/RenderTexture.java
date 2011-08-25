package org.anddev.andengine.opengl.texture.render;

import java.io.IOException;
import java.nio.IntBuffer;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.math.MathUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;

/**
 * A {@link RenderTexture} might only be created during runtime, which might not yet be in {@link BaseGameActivity#onLoadResources()}, but I need to try this.
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

	private static final int[] FRAMEBUFFEROBJECTID_CONTAINER = new int[1];

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
	private final int mFrameBufferObjectID;
	private int mPreviousFrameBufferObjectID;
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
			throw new IllegalArgumentException("pWidth and pHeight must be powers of two!");
		}

		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mPixelFormat = pPixelFormat;

		GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, RenderTexture.FRAMEBUFFEROBJECTID_CONTAINER, 0);
		this.mPreviousFrameBufferObjectID = RenderTexture.FRAMEBUFFEROBJECTID_CONTAINER[0];

		try{
			this.loadToHardware();
		} catch(final IOException e) {
			/* Can not happen. */
		}

		/* Generate FBO. */
		GLES20.glGenFramebuffers(1, RenderTexture.FRAMEBUFFEROBJECTID_CONTAINER, 0);
		this.mFrameBufferObjectID = RenderTexture.FRAMEBUFFEROBJECTID_CONTAINER[0];
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mFrameBufferObjectID);

		/* Attach texture to FBO. */
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, this.mTextureID, 0);

		GLState.checkFrameBufferStatus();

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mPreviousFrameBufferObjectID);
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

	public void begin() {
		this.savePreviousViewport();
		GLES20.glViewport(0, 0, this.mWidth, this.mHeight);

		GLState.switchToProjectionMatrix();
		GLState.glPushMatrix();

		GLState.glOrthof(0, this.mWidth, this.mHeight, 0, -1, 1);

		GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, RenderTexture.FRAMEBUFFEROBJECTID_CONTAINER, 0);
		this.mPreviousFrameBufferObjectID = RenderTexture.FRAMEBUFFEROBJECTID_CONTAINER[0];
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mFrameBufferObjectID);

		GLState.switchToModelViewMatrix();
		GLState.glPushMatrix();
		GLState.glLoadIdentity();
	}

	public void end() {
		GLState.glPopMatrix();

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mPreviousFrameBufferObjectID);

		GLState.switchToProjectionMatrix();
		GLState.glPopMatrix();

		this.resotorePreviousViewport();

		GLState.switchToModelViewMatrix();
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

	public Bitmap getBitmap() {
		return this.getBitmap(false);
	}

	public Bitmap getBitmap(final boolean pFlipVertical) {
		if(this.mPixelFormat != PixelFormat.RGBA_8888){
			throw new IllegalStateException(); // TODO Description...
		}

		final int[] pixelsRGBA_8888 = new int[this.mWidth * this.mHeight];
		final IntBuffer glPixelBuffer = IntBuffer.wrap(pixelsRGBA_8888);
		glPixelBuffer.position(0);

		this.begin();
		GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), glPixelBuffer);
		this.end();

		final int[] pixelsARGB_8888;
		if(pFlipVertical) {
			pixelsARGB_8888 = GLHelper.convertRGBA_8888toARGB_8888_FlippedVertical(pixelsRGBA_8888, this.mWidth, this.mHeight);
		} else {
			pixelsARGB_8888 = GLHelper.convertRGBA_8888toARGB_8888(pixelsRGBA_8888);
		}

		return Bitmap.createBitmap(pixelsARGB_8888, this.mWidth, this.mHeight, Config.ARGB_8888);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
