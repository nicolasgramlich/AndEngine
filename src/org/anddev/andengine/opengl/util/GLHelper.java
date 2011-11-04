package org.anddev.andengine.opengl.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.anddev.andengine.opengl.texture.Texture.PixelFormat;

import android.graphics.Bitmap;

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

	public static final boolean IS_LITTLE_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * TODO These conversions could be done in native code!
	 */
	public static Buffer getPixels(final Bitmap pBitmap, final PixelFormat pPixelFormat) {
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

	public static int[] convertARGB_8888toRGBA_8888(final int[] pPixelsARGB_8888) {
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

	public static int[] convertRGBA_8888toARGB_8888(final int[] pPixelsRGBA_8888) {
		if(GLHelper.IS_LITTLE_ENDIAN) {
			for(int i = pPixelsRGBA_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsRGBA_8888[i];
				/* ABGR to ARGB */
				pPixelsRGBA_8888[i] = pixel & 0xFF00FF00 | (pixel & 0x000000FF) << 16 | (pixel & 0x00FF0000) >> 16;
			}
		} else {
			for(int i = pPixelsRGBA_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsRGBA_8888[i];
				/* RGBA to ARGB */
				pPixelsRGBA_8888[i] = (pixel >> 8) & 0x00FFFFFF | (pixel & 0x000000FF) << 24;
			}
		}
		return pPixelsRGBA_8888;
	}

	public static int[] convertRGBA_8888toARGB_8888_FlippedVertical(final int[] pPixelsRGBA_8888, final int pWidth, final int pHeight) {
		final int[] pixelsARGB_8888 = new int[pWidth * pHeight];

		if(GLHelper.IS_LITTLE_ENDIAN) {
			for(int y = 0; y < pHeight; y++) {
				for(int x = 0; x < pWidth; x++) {
					final int pixel = pPixelsRGBA_8888[x + (y * pWidth)];
					/* ABGR to ARGB */
					pixelsARGB_8888[x + ((pHeight - y - 1) * pWidth)] = pixel & 0xFF00FF00 | (pixel & 0x000000FF) << 16 | (pixel & 0x00FF0000) >> 16;
				}
			}
		} else {
			for(int y = 0; y < pHeight; y++) {
				for(int x = 0; x < pWidth; x++) {
					final int pixel = pPixelsRGBA_8888[x + (y * pWidth)];
					/* RGBA to ARGB */
					pixelsARGB_8888[x + ((pHeight - y - 1) * pWidth)] = (pixel >> 8) & 0x00FFFFFF | (pixel & 0x000000FF) << 24;
				}
			}
		}
		return pixelsARGB_8888;
	}

	public static byte[] convertARGB_8888toRGB_565(final int[] pPixelsARGB_8888) {
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

	public static byte[] convertARGB_8888toARGB_4444(final int[] pPixelsARGB_8888) {
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

	public static byte[] convertARGB_8888toA_8(final int[] pPixelsARGB_8888) {
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
