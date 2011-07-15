package org.anddev.andengine.opengl.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import org.anddev.andengine.engine.options.RenderOptions;
import org.anddev.andengine.opengl.texture.Texture.PixelFormat;
import org.anddev.andengine.opengl.texture.region.crop.TextureRegionCrop;
import org.anddev.andengine.util.Debug;

import android.graphics.Bitmap;
import android.opengl.GLException;
import android.opengl.GLUtils;
import android.os.Build;

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
	private static final int[] HARDWAREBUFFERID_CONTAINER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private static int sCurrentHardwareBufferID = -1;
	private static int sCurrentHardwareTextureID = -1;
	private static int sCurrentMatrix = -1;

	private static int sCurrentSourceBlendMode = -1;
	private static int sCurrentDestinationBlendMode = -1;

	private static FastFloatBuffer sCurrentVertexFloatBuffer = null;
	private static FastFloatBuffer sCurrentTextureFloatBuffer = null;
	private static TextureRegionCrop sCurrentTextureRegionCrop = null;

	private static boolean sEnableDither = true;
	private static boolean sEnableLightning = true;
	private static boolean sEnableDepthTest = true;
	private static boolean sEnableMultisample = true;

	private static boolean sEnableScissorTest = false;
	private static boolean sEnableBlend = false;
	private static boolean sEnableCulling = false;
	private static boolean sEnableTextures = false;
	private static boolean sEnableTexCoordArray = false;
	private static boolean sEnableVertexArray = false;

	private static float sLineWidth = 1;

	private static float sRed = -1;
	private static float sGreen = -1;
	private static float sBlue = -1;
	private static float sAlpha = -1;

	public static boolean EXTENSIONS_VERTEXBUFFEROBJECTS = false;
	public static boolean EXTENSIONS_DRAWTEXTURE = false;
	public static boolean EXTENSIONS_TEXTURE_NON_POWER_OF_TWO = false;

	// ===========================================================
	// Methods
	// ===========================================================

	public static void reset(final GL10 pGL) {
		GLHelper.sCurrentHardwareBufferID = -1;
		GLHelper.sCurrentHardwareTextureID = -1;
		GLHelper.sCurrentMatrix = -1;

		GLHelper.sCurrentSourceBlendMode = -1;
		GLHelper.sCurrentDestinationBlendMode = -1;

		GLHelper.sCurrentVertexFloatBuffer = null;
		GLHelper.sCurrentTextureFloatBuffer = null;
		GLHelper.sCurrentTextureRegionCrop = null;

		GLHelper.enableDither(pGL);
		GLHelper.enableLightning(pGL);
		GLHelper.enableDepthTest(pGL);
		GLHelper.enableMultisample(pGL);

		GLHelper.disableBlend(pGL);
		GLHelper.disableCulling(pGL);
		GLHelper.disableTextures(pGL);
		GLHelper.disableTexCoordArray(pGL);
		GLHelper.disableVertexArray(pGL);

		GLHelper.sLineWidth = 1;

		GLHelper.sRed = -1;
		GLHelper.sGreen = -1;
		GLHelper.sBlue = -1;
		GLHelper.sAlpha = -1;

		GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS = false;
		GLHelper.EXTENSIONS_DRAWTEXTURE = false;
		GLHelper.EXTENSIONS_TEXTURE_NON_POWER_OF_TWO = false;
	}

	public static void enableExtensions(final GL10 pGL, final RenderOptions pRenderOptions) {
		final String version = pGL.glGetString(GL10.GL_VERSION);
		final String renderer = pGL.glGetString(GL10.GL_RENDERER);
		final String extensions = pGL.glGetString(GL10.GL_EXTENSIONS);

		Debug.d("RENDERER: " + renderer);
		Debug.d("VERSION: " + version);
		Debug.d("EXTENSIONS: " + extensions);

		final boolean isOpenGL10 = version.contains("1.0");
		final boolean isOpenGL2X = version.contains("2.");
		final boolean isSoftwareRenderer = renderer.contains("PixelFlinger");
		final boolean isVBOCapable = extensions.contains("_vertex_buffer_object");
		final boolean isDrawTextureCapable = extensions.contains("draw_texture");
		final boolean isTextureNonPowerOfTwoCapable = extensions.contains("texture_npot");

		GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS = !pRenderOptions.isDisableExtensionVertexBufferObjects() && !isSoftwareRenderer && (isVBOCapable || !isOpenGL10);
		GLHelper.EXTENSIONS_DRAWTEXTURE = !pRenderOptions.isDisableExtensionVertexBufferObjects() && (isDrawTextureCapable || !isOpenGL10);
		GLHelper.EXTENSIONS_TEXTURE_NON_POWER_OF_TWO = isTextureNonPowerOfTwoCapable || isOpenGL2X;

		GLHelper.hackBrokenDevices();
		Debug.d("EXTENSIONS_VERXTEXBUFFEROBJECTS = " + GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS);
		Debug.d("EXTENSIONS_DRAWTEXTURE = " + GLHelper.EXTENSIONS_DRAWTEXTURE);
	}

	private static void hackBrokenDevices() {
		if (Build.PRODUCT.contains("morrison")) {
			// This is the Motorola Cliq. This device LIES and says it supports
			// VBOs, which it actually does not (or, more likely, the extensions string
			// is correct and the GL JNI glue is broken).
			GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS = false;
			// TODO: if Motorola fixes this, I should switch to using the fingerprint
			// (blur/morrison/morrison/morrison:1.5/CUPCAKE/091007:user/ota-rel-keys,release-keys)
			// instead of the product name so that newer versions use VBOs
		}
	}

	public static void setColor(final GL10 pGL, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if(pAlpha != GLHelper.sAlpha || pRed != GLHelper.sRed || pGreen != GLHelper.sGreen || pBlue != GLHelper.sBlue) {
			GLHelper.sAlpha = pAlpha;
			GLHelper.sRed = pRed;
			GLHelper.sGreen = pGreen;
			GLHelper.sBlue = pBlue;
			pGL.glColor4f(pRed, pGreen, pBlue, pAlpha);
		}
	}

	public static void enableVertexArray(final GL10 pGL) {
		if(!GLHelper.sEnableVertexArray) {
			GLHelper.sEnableVertexArray = true;
			pGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}
	public static void disableVertexArray(final GL10 pGL) {
		if(GLHelper.sEnableVertexArray) {
			GLHelper.sEnableVertexArray = false;
			pGL.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	public static void enableTexCoordArray(final GL10 pGL) {
		if(!GLHelper.sEnableTexCoordArray) {
			GLHelper.sEnableTexCoordArray = true;
			pGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}
	public static void disableTexCoordArray(final GL10 pGL) {
		if(GLHelper.sEnableTexCoordArray) {
			GLHelper.sEnableTexCoordArray = false;
			pGL.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}

	public static void enableScissorTest(final GL10 pGL) {
		if(!GLHelper.sEnableScissorTest) {
			GLHelper.sEnableScissorTest = true;
			pGL.glEnable(GL10.GL_SCISSOR_TEST);
		}
	}
	public static void disableScissorTest(final GL10 pGL) {
		if(GLHelper.sEnableScissorTest) {
			GLHelper.sEnableScissorTest = false;
			pGL.glDisable(GL10.GL_SCISSOR_TEST);
		}
	}

	public static void enableBlend(final GL10 pGL) {
		if(!GLHelper.sEnableBlend) {
			GLHelper.sEnableBlend = true;
			pGL.glEnable(GL10.GL_BLEND);
		}
	}
	public static void disableBlend(final GL10 pGL) {
		if(GLHelper.sEnableBlend) {
			GLHelper.sEnableBlend = false;
			pGL.glDisable(GL10.GL_BLEND);
		}
	}

	public static void enableCulling(final GL10 pGL) {
		if(!GLHelper.sEnableCulling) {
			GLHelper.sEnableCulling = true;
			pGL.glEnable(GL10.GL_CULL_FACE);
		}
	}
	public static void disableCulling(final GL10 pGL) {
		if(GLHelper.sEnableCulling) {
			GLHelper.sEnableCulling = false;
			pGL.glDisable(GL10.GL_CULL_FACE);
		}
	}

	public static void enableTextures(final GL10 pGL) {
		if(!GLHelper.sEnableTextures) {
			GLHelper.sEnableTextures = true;
			pGL.glEnable(GL10.GL_TEXTURE_2D);
		}
	}
	public static void disableTextures(final GL10 pGL) {
		if(GLHelper.sEnableTextures) {
			GLHelper.sEnableTextures = false;
			pGL.glDisable(GL10.GL_TEXTURE_2D);
		}
	}

	public static void enableLightning(final GL10 pGL) {
		if(!GLHelper.sEnableLightning) {
			GLHelper.sEnableLightning = true;
			pGL.glEnable(GL10.GL_LIGHTING);
		}
	}
	public static void disableLightning(final GL10 pGL) {
		if(GLHelper.sEnableLightning) {
			GLHelper.sEnableLightning = false;
			pGL.glDisable(GL10.GL_LIGHTING);
		}
	}

	public static void enableDither(final GL10 pGL) {
		if(!GLHelper.sEnableDither) {
			GLHelper.sEnableDither = true;
			pGL.glEnable(GL10.GL_DITHER);
		}
	}
	public static void disableDither(final GL10 pGL) {
		if(GLHelper.sEnableDither) {
			GLHelper.sEnableDither = false;
			pGL.glDisable(GL10.GL_DITHER);
		}
	}

	public static void enableDepthTest(final GL10 pGL) {
		if(!GLHelper.sEnableDepthTest) {
			GLHelper.sEnableDepthTest = true;
			pGL.glEnable(GL10.GL_DEPTH_TEST);
		}
	}
	public static void disableDepthTest(final GL10 pGL) {
		if(GLHelper.sEnableDepthTest) {
			GLHelper.sEnableDepthTest = false;
			pGL.glDisable(GL10.GL_DEPTH_TEST);
		}
	}

	public static void enableMultisample(final GL10 pGL) {
		if(!GLHelper.sEnableMultisample) {
			GLHelper.sEnableMultisample = true;
			pGL.glEnable(GL10.GL_MULTISAMPLE);
		}
	}
	public static void disableMultisample(final GL10 pGL) {
		if(GLHelper.sEnableMultisample) {
			GLHelper.sEnableMultisample = false;
			pGL.glDisable(GL10.GL_MULTISAMPLE);
		}
	}

	public static void bindBuffer(final GL11 pGL11, final int pHardwareBufferID) {
		/* Reduce unnecessary buffer switching calls. */
		if(GLHelper.sCurrentHardwareBufferID != pHardwareBufferID) {
			GLHelper.sCurrentHardwareBufferID = pHardwareBufferID;
			pGL11.glBindBuffer(GL11.GL_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public static void deleteBuffer(final GL11 pGL11, final int pHardwareBufferID) {
		GLHelper.HARDWAREBUFFERID_CONTAINER[0] = pHardwareBufferID;
		pGL11.glDeleteBuffers(1, GLHelper.HARDWAREBUFFERID_CONTAINER, 0);
	}

	/**
	 * @see {@link GLHelper#forceBindTexture(GL10, int)}
	 * @param pGL
	 * @param pHardwareTextureID
	 */
	public static void bindTexture(final GL10 pGL, final int pHardwareTextureID) {
		/* Reduce unnecessary texture switching calls. */
		if(GLHelper.sCurrentHardwareTextureID != pHardwareTextureID) {
			GLHelper.sCurrentHardwareTextureID = pHardwareTextureID;
			pGL.glBindTexture(GL10.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	/**
	 * @see {@link GLHelper#bindTexture(GL10, int)}
	 * @param pGL
	 * @param pHardwareTextureID
	 */
	public static void forceBindTexture(final GL10 pGL, final int pHardwareTextureID) {
		GLHelper.sCurrentHardwareTextureID = pHardwareTextureID;
		pGL.glBindTexture(GL10.GL_TEXTURE_2D, pHardwareTextureID);
	}

	public static void deleteTexture(final GL10 pGL, final int pHardwareTextureID) {
		GLHelper.HARDWARETEXTUREID_CONTAINER[0] = pHardwareTextureID;
		pGL.glDeleteTextures(1, GLHelper.HARDWARETEXTUREID_CONTAINER, 0);
	}

	public static void texCoordPointer(final GL10 pGL, final FastFloatBuffer pTextureFloatBuffer) {
		if(GLHelper.sCurrentTextureFloatBuffer  != pTextureFloatBuffer) {
			GLHelper.sCurrentTextureFloatBuffer = pTextureFloatBuffer;
			pGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, pTextureFloatBuffer.mByteBuffer);
		}
	}

	public static void texCoordZeroPointer(final GL11 pGL11) {
		pGL11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
	}

	public static void vertexPointer(final GL10 pGL, final FastFloatBuffer pVertexFloatBuffer) {
		if(GLHelper.sCurrentVertexFloatBuffer != pVertexFloatBuffer) {
			GLHelper.sCurrentVertexFloatBuffer = pVertexFloatBuffer;
			pGL.glVertexPointer(2, GL10.GL_FLOAT, 0, pVertexFloatBuffer.mByteBuffer);
		}
	}

	public static void vertexZeroPointer(final GL11 pGL11) {
		pGL11.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);
	}

	public static void blendFunction(final GL10 pGL, final int pSourceBlendMode, final int pDestinationBlendMode) {
		if(GLHelper.sCurrentSourceBlendMode != pSourceBlendMode || GLHelper.sCurrentDestinationBlendMode != pDestinationBlendMode) {
			GLHelper.sCurrentSourceBlendMode = pSourceBlendMode;
			GLHelper.sCurrentDestinationBlendMode = pDestinationBlendMode;
			pGL.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
		}
	}

	public static void lineWidth(final GL10 pGL, final float pLineWidth) {
		if(GLHelper.sLineWidth  != pLineWidth) {
			GLHelper.sLineWidth = pLineWidth;
			pGL.glLineWidth(pLineWidth);
		}
	}

	public static void switchToModelViewMatrix(final GL10 pGL) {
		/* Reduce unnecessary matrix switching calls. */
		if(GLHelper.sCurrentMatrix != GL10.GL_MODELVIEW) {
			GLHelper.sCurrentMatrix = GL10.GL_MODELVIEW;
			pGL.glMatrixMode(GL10.GL_MODELVIEW);
		}
	}

	public static void switchToProjectionMatrix(final GL10 pGL) {
		/* Reduce unnecessary matrix switching calls. */
		if(GLHelper.sCurrentMatrix != GL10.GL_PROJECTION) {
			GLHelper.sCurrentMatrix = GL10.GL_PROJECTION;
			pGL.glMatrixMode(GL10.GL_PROJECTION);
		}
	}

	public static void setProjectionIdentityMatrix(final GL10 pGL) {
		GLHelper.switchToProjectionMatrix(pGL);
		pGL.glLoadIdentity();
	}

	public static void setModelViewIdentityMatrix(final GL10 pGL) {
		GLHelper.switchToModelViewMatrix(pGL);
		pGL.glLoadIdentity();
	}

	public static void setShadeModelFlat(final GL10 pGL) {
		pGL.glShadeModel(GL10.GL_FLAT);
	}

	public static void setPerspectiveCorrectionHintFastest(final GL10 pGL) {
		pGL.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
	}

	public static void bufferData(final GL11 pGL11, final ByteBuffer pByteBuffer, final int pUsage) {
		pGL11.glBufferData(GL11.GL_ARRAY_BUFFER, pByteBuffer.capacity(), pByteBuffer, pUsage);
	}

	public static void textureCrop(final GL11 pGL11, final TextureRegionCrop pTextureRegionCrop) {
		if (pTextureRegionCrop != GLHelper.sCurrentTextureRegionCrop || pTextureRegionCrop.isDirty()) {
			GLHelper.sCurrentTextureRegionCrop = pTextureRegionCrop;
			pGL11.glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, pTextureRegionCrop.getData(), 0);
		}
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 */
	public static void glTexSubImage2D(final GL10 pGL, final int pTarget, final int pLevel, final int pXOffset, final int pYOffset, final Bitmap pBitmap, final PixelFormat pPixelFormat) {
		final int[] pixelsARGB_8888 = GLHelper.getPixelsARGB_8888(pBitmap);

		final Buffer pixelBuffer;
		switch(pPixelFormat) {
			case RGB_565:
				pixelBuffer = ByteBuffer.wrap(GLHelper.convertARGB_8888toRGB_565(pixelsARGB_8888));
				break;
			case RGBA_8888:
				pixelBuffer = IntBuffer.wrap(GLHelper.convertARGB_8888toRGBA_8888(pixelsARGB_8888));
				break;
			case RGBA_4444:
				pixelBuffer = ByteBuffer.wrap(GLHelper.convertARGB_8888toARGB_4444(pixelsARGB_8888));
				break;
			case A_8:
				pixelBuffer = ByteBuffer.wrap(GLHelper.convertARGB_8888toA_8(pixelsARGB_8888));
				break;
			default:
				throw new IllegalArgumentException("Unexpected pTextureFormat: '" + pPixelFormat + "'.");
		}

		pGL.glTexSubImage2D(pTarget, pLevel, pXOffset, pYOffset, pBitmap.getWidth(), pBitmap.getHeight(), pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
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

	public static void checkGLError(final GL10 pGL) throws GLException { // TODO Use more often!
		final int err = pGL.glGetError();
		if (err != GL10.GL_NO_ERROR) {
			throw new GLException(err);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
