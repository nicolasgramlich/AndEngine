package org.anddev.andengine.opengl.texture;

import java.io.IOException;
import java.nio.IntBuffer;

import org.anddev.andengine.opengl.texture.Texture.PixelFormat;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.exception.AndEngineException;
import org.anddev.andengine.util.math.MathUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;

/**
 * TODO When rendering inverted (upside-down), there might be no need to for two byte[] in getBitmap()!?!
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 07:13:05 - 24.08.2011
 */
public class RenderTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWAREID_CONTAINER = new int[1];
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
	private int mFBO;
	private int mPreviousFBO;
	private int mPreviousViewPortX;
	private int mPreviousViewPortY;
	private int mPreviousViewPortWidth;
	private int mPreviousViewPortHeight;

	private Texture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderTexture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, PixelFormat.RGBA_8888);
	}

	public RenderTexture(final int pWidth, final int pHeight, final PixelFormat pPixelFormat) {
		if(!MathUtils.isPowerOfTwo(pWidth) || !MathUtils.isPowerOfTwo(pHeight)) {
			throw new IllegalArgumentException("pWidth and pHeight must be powers of two!");
		}

		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mPixelFormat = pPixelFormat;

		GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, RenderTexture.HARDWAREID_CONTAINER, 0);
		this.mPreviousFBO = RenderTexture.HARDWAREID_CONTAINER[0];

		this.mTexture = new Texture(pPixelFormat, TextureOptions.NEAREST, null) {
			@Override
			public int getWidth() {
				return pWidth;
			}

			@Override
			public int getHeight() {
				return pHeight;
			}

			@Override
			protected void writeTextureToHardware() {
				final int glFormat = pPixelFormat.getGLFormat();
				final int glType = pPixelFormat.getGLType();
				GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, glFormat, pWidth, pHeight, 0, glFormat, glType, null);
			}
		};

		try{
			this.mTexture.loadToHardware();
		} catch(final IOException e) {
			/* Can not happen. */
		}

		/* Generate FBO. */
		GLES20.glGenFramebuffers(1, RenderTexture.HARDWAREID_CONTAINER, 0);
		this.mFBO = RenderTexture.HARDWAREID_CONTAINER[0];
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mFBO);

		/* Attach texture to FBO. */
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, this.mTexture.mHardwareTextureID, 0);

		final int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER); // TODO Put to GLHelper...
		if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
			throw new AndEngineException("Could not attach texture to framebuffer"); // TODO Description...
		}

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mPreviousFBO);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ITexture getTexture() {
		return this.mTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void begin() {
		this.savePreviousViewport();
		GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
		
		GLHelper.switchToProjectionMatrix();
		GLHelper.glPushMatrix();

		GLHelper.glOrthof(0, this.mWidth, this.mHeight, 0, -1, 1);

		GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, RenderTexture.HARDWAREID_CONTAINER, 0);
		this.mPreviousFBO = RenderTexture.HARDWAREID_CONTAINER[0];
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mFBO);
		
		GLHelper.switchToModelViewMatrix();
		GLHelper.glPushMatrix();
		GLHelper.glLoadIdentity();
	}

	public void end() {
		GLHelper.glPopMatrix();
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.mPreviousFBO);

		GLHelper.switchToProjectionMatrix();
		GLHelper.glPopMatrix();

		this.resotorePreviousViewport();
		
		GLHelper.switchToModelViewMatrix();
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
		if(this.mPixelFormat != PixelFormat.RGBA_8888){
			throw new IllegalStateException(); // TODO Description...
		}

		final int[] rawPixels = new int[this.mWidth * this.mHeight];
		final IntBuffer rawPixelBuffer = IntBuffer.wrap(rawPixels);
		rawPixelBuffer.position(0);

		this.begin();
		GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, this.mPixelFormat.getGLFormat(), this.mPixelFormat.getGLType(), rawPixelBuffer);
		this.end();

		final int[] pixels = new int[this.mWidth * this.mHeight];

		// Convert from RGBA_8888 (Which is actually ABGR as the whole buffer seems to be inverted) --> ARGB_8888
		for (int y = 0; y < this.mHeight; y++) {
			for (int x = 0; x < this.mWidth; x++) {
				final int pixel = rawPixels[x + (y * this.mWidth)];

				final int blue = (pixel & 0x00FF0000) >> 16;
			final int red = (pixel  & 0x000000FF) << 16;
			final int greenAlpha = pixel & 0xFF00FF00;

			pixels[x + ((this.mHeight - y - 1) * this.mWidth)] = greenAlpha | red | blue;
			}
		}

		return Bitmap.createBitmap(pixels, this.mWidth, this.mHeight, Config.ARGB_8888);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
