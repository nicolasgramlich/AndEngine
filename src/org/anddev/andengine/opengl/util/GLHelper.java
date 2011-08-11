package org.anddev.andengine.opengl.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.anddev.andengine.engine.options.RenderOptions;
import org.anddev.andengine.opengl.texture.Texture.PixelFormat;
import org.anddev.andengine.opengl.util.GLMatrixStack.MatrixMode;
import org.anddev.andengine.util.Debug;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 18:00:43 - 08.03.2010
 */
public class GLHelper {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_PIXEL_RGBA = 4;

	private static final boolean IS_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);

	private static final int[] HARDWARETEXTUREID_CONTAINER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private static GLMatrixStack sGLMatrixStack = new GLMatrixStack();

	private static int sCurrentHardwareBufferID = -1;
	private static int sCurrentHardwareTextureID = -1;

	private static int sCurrentSourceBlendMode = -1;
	private static int sCurrentDestinationBlendMode = -1;

	private static boolean sEnableDither = true;
	private static boolean sEnableDepthTest = true;

	private static boolean sEnableScissorTest = false;
	private static boolean sEnableBlend = false;
	private static boolean sEnableCulling = false;
	private static boolean sEnableTextures = false;

	private static float sLineWidth = 1;

	// ===========================================================
	// Methods
	// ===========================================================

	public static void reset() {
		GLHelper.sGLMatrixStack.reset();

		GLHelper.sCurrentHardwareBufferID = -1;
		GLHelper.sCurrentHardwareTextureID = -1;

		GLHelper.sCurrentSourceBlendMode = -1;
		GLHelper.sCurrentDestinationBlendMode = -1;

		GLHelper.enableDither();
		GLHelper.enableDepthTest();

		GLHelper.disableBlend();
		GLHelper.disableCulling();
		GLHelper.disableTextures();

		GLHelper.sLineWidth = 1;
	}

	public static void enableExtensions(final RenderOptions pRenderOptions) {
		final String version = GLES20.glGetString(GLES20.GL_VERSION);
		final String renderer = GLES20.glGetString(GLES20.GL_RENDERER);
		final String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);

		Debug.d("RENDERER: " + renderer);
		Debug.d("VERSION: " + version);
		Debug.d("EXTENSIONS: " + extensions);
	}

	public static void enableScissorTest() {
		if(!GLHelper.sEnableScissorTest) {
			GLHelper.sEnableScissorTest = true;
			GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
		}
	}
	public static void disableScissorTest() {
		if(GLHelper.sEnableScissorTest) {
			GLHelper.sEnableScissorTest = false;
			GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		}
	}

	public static void enableBlend() {
		if(!GLHelper.sEnableBlend) {
			GLHelper.sEnableBlend = true;
			GLES20.glEnable(GLES20.GL_BLEND);
		}
	}
	public static void disableBlend() {
		if(GLHelper.sEnableBlend) {
			GLHelper.sEnableBlend = false;
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}

	public static void enableCulling() {
		if(!GLHelper.sEnableCulling) {
			GLHelper.sEnableCulling = true;
			GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
	}
	public static void disableCulling() {
		if(GLHelper.sEnableCulling) {
			GLHelper.sEnableCulling = false;
			GLES20.glDisable(GLES20.GL_CULL_FACE);
		}
	}

	public static void enableTextures() {
		if(!GLHelper.sEnableTextures) {
			GLHelper.sEnableTextures = true;
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		}
	}
	public static void disableTextures() {
		if(GLHelper.sEnableTextures) {
			GLHelper.sEnableTextures = false;
			GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		}
	}

	public static void enableDither() {
		if(!GLHelper.sEnableDither) {
			GLHelper.sEnableDither = true;
			GLES20.glEnable(GLES20.GL_DITHER);
		}
	}
	public static void disableDither() {
		if(GLHelper.sEnableDither) {
			GLHelper.sEnableDither = false;
			GLES20.glDisable(GLES20.GL_DITHER);
		}
	}

	public static void enableDepthTest() {
		if(!GLHelper.sEnableDepthTest) {
			GLHelper.sEnableDepthTest = true;
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		}
	}
	public static void disableDepthTest() {
		if(GLHelper.sEnableDepthTest) {
			GLHelper.sEnableDepthTest = false;
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		}
	}

	public static void bindBuffer(final int pHardwareBufferID) {
		/* Reduce unnecessary buffer switching calls. */
		if(GLHelper.sCurrentHardwareBufferID != pHardwareBufferID) {
			GLHelper.sCurrentHardwareBufferID = pHardwareBufferID;
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	/**
	 * @see {@link GLHelper#forceBindTexture(GLES20, int)}
	 * @param GLES20
	 * @param pHardwareTextureID
	 */
	public static void bindTexture(final int pHardwareTextureID) {
		/* Reduce unnecessary texture switching calls. */
		if(GLHelper.sCurrentHardwareTextureID != pHardwareTextureID) {
			GLHelper.sCurrentHardwareTextureID = pHardwareTextureID;
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	public static void deleteTexture(final int pHardwareTextureID) {
		GLHelper.HARDWARETEXTUREID_CONTAINER[0] = pHardwareTextureID;
		GLES20.glDeleteTextures(1, GLHelper.HARDWARETEXTUREID_CONTAINER, 0);
	}

	public static void blendFunction(final int pSourceBlendMode, final int pDestinationBlendMode) {
		if(GLHelper.sCurrentSourceBlendMode != pSourceBlendMode || GLHelper.sCurrentDestinationBlendMode != pDestinationBlendMode) {
			GLHelper.sCurrentSourceBlendMode = pSourceBlendMode;
			GLHelper.sCurrentDestinationBlendMode = pDestinationBlendMode;
			GLES20.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
		}
	}

	public static void lineWidth(final float pLineWidth) {
		if(GLHelper.sLineWidth  != pLineWidth) {
			GLHelper.sLineWidth = pLineWidth;
			GLES20.glLineWidth(pLineWidth);
		}
	}

	public static void switchToModelViewMatrix() {
		GLHelper.sGLMatrixStack.setMatrixMode(MatrixMode.MODELVIEW);
	}

	public static void switchToProjectionMatrix() {
		GLHelper.sGLMatrixStack.setMatrixMode(MatrixMode.PROJECTION);
	}

	public static void switchToMatrix(final MatrixMode pMatrixMode) {
		GLHelper.sGLMatrixStack.setMatrixMode(pMatrixMode);
	}

	public static GLMatrix getProjectionMatrix() {
		return GLHelper.sGLMatrixStack.getProjectionMatrix();
	}

	public static GLMatrix getModelViewMatrix() {
		return GLHelper.sGLMatrixStack.getModelViewMatrix();
	}

	public static GLMatrix getModelViewProjectionMatrix() {
		return GLHelper.sGLMatrixStack.getModelViewProjectionMatrix();
	}

	public static void setProjectionIdentityMatrix() {
		GLHelper.switchToProjectionMatrix();
		GLHelper.sGLMatrixStack.loadIdentity();
	}

	public static void setModelViewIdentityMatrix() {
		GLHelper.switchToModelViewMatrix();
		GLHelper.sGLMatrixStack.loadIdentity();
	}

	public static void glLoadIdentity() {
		GLHelper.sGLMatrixStack.loadIdentity();
	}

	public static void glPushMatrix() {
		GLHelper.sGLMatrixStack.pushMatrix();
	}

	public static void glPopMatrix() {
		GLHelper.sGLMatrixStack.popMatrix();
	}

	public static void glTranslatef(final float pX, final float pY, final int pZ) {
		GLHelper.sGLMatrixStack.translate(pX, pY, pZ);
	}

	public static void glRotatef(final float pAngle, final float pX, final float pY, final int pZ) {
		GLHelper.sGLMatrixStack.rotate(pAngle, pX, pY, pZ);
	}

	public static void glScalef(final float pScaleX, final float pScaleY, final int pScaleZ) {
		GLHelper.sGLMatrixStack.scale(pScaleX, pScaleY, pScaleZ);
	}

	public static void glOrthof(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		GLHelper.sGLMatrixStack.ortho(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 * @param pBorder
	 */
	public static void glTexImage2D(final int pTarget, final int pLevel, final Bitmap pBitmap, final int pBorder, final PixelFormat pPixelFormat) {
		final Buffer pixelBuffer = GLHelper.getPixels(pBitmap, pPixelFormat);

		GLES20.glTexImage2D(pTarget, pLevel, pPixelFormat.getGLFormat(), pBitmap.getWidth(), pBitmap.getHeight(), pBorder, pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 */
	public static void glTexSubImage2D(final int pTarget, final int pLevel, final int pXOffset, final int pYOffset, final Bitmap pBitmap, final PixelFormat pPixelFormat) {
		final Buffer pixelBuffer = GLHelper.getPixels(pBitmap, pPixelFormat);

		GLES20.glTexSubImage2D(pTarget, pLevel, pXOffset, pYOffset, pBitmap.getWidth(), pBitmap.getHeight(), pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
	}

	private static Buffer getPixels(final Bitmap pBitmap, final PixelFormat pPixelFormat) {
		final int[] pixelsARGB_8888 = GLHelper.getPixelsARGB_8888(pBitmap);

		switch(pPixelFormat) {
			case RGB_565:
				return ByteBuffer.wrap(GLHelper.convertARGB_8888toRGB_565(pixelsARGB_8888));
			case RGBA_8888:
				return IntBuffer.wrap(GLHelper.convertARGB_8888toRGBA_8888(pixelsARGB_8888));
			case RGBA_4444:
				return ByteBuffer.wrap(GLHelper.convertARGB_8888toARGB_4444(pixelsARGB_8888));
			case A_8:
				return ByteBuffer.wrap(GLHelper.convertARGB_8888toA_8(pixelsARGB_8888));
			default:
				throw new IllegalArgumentException("Unexpected " + PixelFormat.class.getSimpleName() + ": '" + pPixelFormat + "'.");
		}
	}

	private static int[] convertARGB_8888toRGBA_8888(final int[] pPixelsARGB_8888) {
		if(GLHelper.IS_LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];
				/* ARGB to ABGR */
				pPixelsARGB_8888[i] = pixel & 0xFF00FF00 | (pixel & 0x000000FF) << 16 | (pixel & 0x00FF0000) >> 16;
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];
				/* ARGB to RGBA */
				pPixelsARGB_8888[i] = (pixel & 0x00FFFFFF) << 8 | (pixel & 0xFF000000) >> 24;
			}
		}
		return pPixelsARGB_8888;
	}

	private static byte[] convertARGB_8888toRGB_565(final int[] pPixelsARGB_8888) {
		final byte[] pixelsRGB_565 = new byte[pPixelsARGB_8888.length * 2];
		if(GLHelper.IS_LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1, j = pixelsRGB_565.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				final int red = ((pixel >> 16) & 0xFF);
				final int green = ((pixel >> 8) & 0xFF);
				final int blue = ((pixel) & 0xFF);

				/* Byte1: [R1 R2 R3 R4 R5 G1 G2 G3]
				 * Byte2: [G4 G5 G6 B1 B2 B3 B4 B5] */

				pixelsRGB_565[j--] = (byte)((red & 0xF8) | (green >> 5));
				pixelsRGB_565[j--] = (byte)(((green << 3) & 0xE0) | (blue >> 3));
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1, j = pixelsRGB_565.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				final int red = ((pixel >> 16) & 0xFF);
				final int green = ((pixel >> 8) & 0xFF);
				final int blue = ((pixel) & 0xFF);

				/* Byte2: [G4 G5 G6 B1 B2 B3 B4 B5]
				 * Byte1: [R1 R2 R3 R4 R5 G1 G2 G3]*/

				pixelsRGB_565[j--] = (byte)(((green << 3) & 0xE0) | (blue >> 3));
				pixelsRGB_565[j--] = (byte)((red & 0xF8) | (green >> 5));
			}
		}
		return pixelsRGB_565;
	}

	private static byte[] convertARGB_8888toARGB_4444(final int[] pPixelsARGB_8888) {
		final byte[] pixelsARGB_4444 = new byte[pPixelsARGB_8888.length * 2];
		if(GLHelper.IS_LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1, j = pixelsARGB_4444.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				final int alpha = ((pixel >> 28) & 0x0F);
				final int red = ((pixel >> 16) & 0xF0);
				final int green = ((pixel >> 8) & 0xF0);
				final int blue = ((pixel) & 0x0F);

				/* Byte1: [A1 A2 A3 A4 R1 R2 R3 R4]
				 * Byte2: [G1 G2 G3 G4 G2 G2 G3 G4] */

				pixelsARGB_4444[j--] = (byte)(alpha | red);
				pixelsARGB_4444[j--] = (byte)(green | blue);
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1, j = pixelsARGB_4444.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				final int alpha = ((pixel >> 28) & 0x0F);
				final int red = ((pixel >> 16) & 0xF0);
				final int green = ((pixel >> 8) & 0xF0);
				final int blue = ((pixel) & 0x0F);

				/* Byte2: [G1 G2 G3 G4 G2 G2 G3 G4]
				 * Byte1: [A1 A2 A3 A4 R1 R2 R3 R4] */

				pixelsARGB_4444[j--] = (byte)(green | blue);
				pixelsARGB_4444[j--] = (byte)(alpha | red);
			}
		}
		return pixelsARGB_4444;
	}

	private static byte[] convertARGB_8888toA_8(final int[] pPixelsARGB_8888) {
		final byte[] pixelsA_8 = new byte[pPixelsARGB_8888.length];
		if(GLHelper.IS_LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				pixelsA_8[i] = (byte) (pPixelsARGB_8888[i] >> 24);
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				pixelsA_8[i] = (byte) (pPixelsARGB_8888[i] & 0xFF);
			}
		}
		return pixelsA_8;
	}

	public static int[] getPixelsARGB_8888(final Bitmap pBitmap) {
		final int w = pBitmap.getWidth();
		final int h = pBitmap.getHeight();

		final int[] pixelsARGB_8888 = new int[w * h];
		pBitmap.getPixels(pixelsARGB_8888, 0, w, 0, 0, w, h);

		return pixelsARGB_8888;
	}

	public static void checkGLError() throws GLException { // TODO Use more often!
		final int err = GLES20.glGetError();
		if (err != GLES20.GL_NO_ERROR) {
			throw new GLException(err);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
