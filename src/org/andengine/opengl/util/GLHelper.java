package org.andengine.opengl.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.andengine.opengl.texture.PixelFormat;

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
		return GLHelper.getPixels(pBitmap, pPixelFormat, ByteOrder.nativeOrder());
	}

	public static Buffer getPixels(final Bitmap pBitmap, final PixelFormat pPixelFormat, final ByteOrder pByteOrder) {
		final int[] pixelsARGB_8888 = GLHelper.getPixelsARGB_8888(pBitmap);

		switch(pPixelFormat) {
			case RGB_565:
				return ShortBuffer.wrap(GLHelper.convertARGB_8888toRGB_565(pixelsARGB_8888, pByteOrder)); // TODO Is ShortBuffer or IntBuffer faster?
			case RGBA_8888:
				// HACK =(
				final ByteOrder reverseByteOrder = (pByteOrder == ByteOrder.LITTLE_ENDIAN) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
				return IntBuffer.wrap(GLHelper.convertARGB_8888toRGBA_8888(pixelsARGB_8888, reverseByteOrder));
			case RGBA_4444:
				return ShortBuffer.wrap(GLHelper.convertARGB_8888toRGBA_4444(pixelsARGB_8888, pByteOrder)); // TODO Is ShortBuffer or IntBuffer faster?
			case A_8:
				return ByteBuffer.wrap(GLHelper.convertARGB_8888toA_8(pixelsARGB_8888));
			default:
				throw new IllegalArgumentException("Unexpected " + PixelFormat.class.getSimpleName() + ": '" + pPixelFormat + "'.");
		}
	}

	public static int[] convertARGB_8888toRGBA_8888(final int[] pPixelsARGB_8888) {
		return GLHelper.convertARGB_8888toRGBA_8888(pPixelsARGB_8888, ByteOrder.nativeOrder());
	}

	public static int[] convertARGB_8888toRGBA_8888(final int[] pPixelsARGB_8888, final ByteOrder pByteOrder) {
		if(pByteOrder == ByteOrder.LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				/* [A][R][G][B] to [A][B][G][R] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				/* To   : [ A7 A6 A5 A4 A3 A2 A1 A0  |  B7 B6 B5 B4 B3 B2 B1 B0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  R7 R6 R5 R4 R3 R2 R1 R0 ] */
				pPixelsARGB_8888[i] = (pixel & 0xFF00FF00) | ((pixel << 16) & 0x00FF0000) | ((pixel >> 16) & 0x000000FF);
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				/* [A][R][G][B] to [R][G][B][A] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				/* To   : [ R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0  |  A7 A6 A5 A4 A3 A2 A1 A0 ] */
				pPixelsARGB_8888[i] = ((pixel << 8) & 0xFFFFFF00) | ((pixel >> 24) & 0x000000FF);
			}
		}
		return pPixelsARGB_8888;
	}

	public static short[] convertARGB_8888toRGB_565(final int[] pPixelsARGB_8888) {
		return GLHelper.convertARGB_8888toRGB_565(pPixelsARGB_8888, ByteOrder.nativeOrder());
	}

	public static short[] convertARGB_8888toRGB_565(final int[] pPixelsARGB_8888, final ByteOrder pByteOrder) {
		final short[] pixelsRGB_565 = new short[pPixelsARGB_8888.length];
		if(pByteOrder == ByteOrder.LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				/* [A][R][G][B] to [GB][RG] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				/* To   :                                                         [ G4 G3 G2 B7 B6 B5 B4 B5  |  R7 R6 R5 R4 R3 G7 G6 G5 ] */

				/* pixelsRGB_565[i] = (short)(red | green | blue); */
				pixelsRGB_565[i] = (short)(((pixel >> 16) & 0x00F8) | ((pixel >> 13) & 0x07) | ((pixel << 3) & 0xE000) | ((pixel << 5) & 0x1F00));
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				/* [A][R][G][B] to [RG][GB] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				/* To   :                                                         [ R7 R6 R5 R4 R3 G7 G6 G5  |  G4 G3 G2 B7 B6 B5 B4 B3 ] */

				/* pixelsRGB_565[i] = (short)(red | green | blue); */
				pixelsRGB_565[i] = (short)(((pixel >> 8) & 0xF800) | ((pixel >> 5) & 0x07E0) | ((pixel >> 3) & 0x001F));
			}
		}
		return pixelsRGB_565;
	}

	public static short[] convertARGB_8888toRGBA_4444(final int[] pPixelsARGB_8888) {
		return GLHelper.convertARGB_8888toRGBA_4444(pPixelsARGB_8888, ByteOrder.nativeOrder());
	}

	public static short[] convertARGB_8888toRGBA_4444(final int[] pPixelsARGB_8888, final ByteOrder pByteOrder) {
		final short[] pixelsRGBA_4444 = new short[pPixelsARGB_8888.length];
		if(pByteOrder == ByteOrder.LITTLE_ENDIAN) {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				/* [A][R][G][B] to [BA][RG] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				/* To   :                                                         [ B7 B6 B5 B4 A7 A6 A5 A4  |  R7 R6 R5 R4 G7 G6 G5 G4 ] */

				/* pixelsRGBA_4444[i] = (short)(red | green | blue | alpha) */
				pixelsRGBA_4444[i] = (short)(((pixel >> 16) & 0x00F0) | ((pixel >> 12) & 0x000F) | ((pixel << 8) & 0xF000) | ((pixel >> 20) & 0x0F00));
			}
		} else {
			for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsARGB_8888[i];

				/* [A][R][G][B] to [RG][BA] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				/* To   :                                                         [ R7 R6 R5 R4 G7 G6 G5 G4  |  B7 B6 B5 B4 A7 A6 A5 A4 ] */

				/* pixelsRGBA_4444[i] = (short)(red | green | blue | alpha) */
				pixelsRGBA_4444[i] = (short)(((pixel >> 8) & 0xF000) | ((pixel >> 4) & 0x0F00) | ((pixel) & 0x00F0) | ((pixel >> 28) & 0x0000F));
			}
		}
		return pixelsRGBA_4444;
	}

	public static byte[] convertARGB_8888toA_8(final int[] pPixelsARGB_8888) {
		final byte[] pixelsA_8 = new byte[pPixelsARGB_8888.length];
		for(int i = pPixelsARGB_8888.length - 1; i >= 0; i--) {
			/* [A][R][G][B] to [A] */
			/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
			/* To   :                                                                                     [ A7 A6 A5 A4 A3 A2 A1 A0 ] */
			pixelsA_8[i] = (byte) ((pPixelsARGB_8888[i] >> 24) & 0xFF);
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

	public static int[] convertRGBA_8888toARGB_8888(final int[] pPixelsRGBA_8888) {
		return GLHelper.convertRGBA_8888toARGB_8888(pPixelsRGBA_8888, ByteOrder.nativeOrder());
	}

	public static int[] convertRGBA_8888toARGB_8888(final int[] pPixelsRGBA_8888, final ByteOrder pByteOrder) {
		if(pByteOrder == ByteOrder.LITTLE_ENDIAN) {
			for(int i = pPixelsRGBA_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsRGBA_8888[i];
				/* [A][B][G][R] to [A][R][G][B] */
				/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  B7 B6 B5 B4 B3 B2 B1 B0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  R7 R6 R5 R4 R3 R2 R1 R0 ] */
				/* To   : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				pPixelsRGBA_8888[i] = (pixel & 0xFF00FF00) | ((pixel << 16) & 0x00FF0000) | ((pixel >> 16) & 0x000000FF);
			}
		} else {
			for(int i = pPixelsRGBA_8888.length - 1; i >= 0; i--) {
				final int pixel = pPixelsRGBA_8888[i];
				/* [R][G][B][A] to [A][R][G][B] */
				/* From : [ R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0  |  A7 A6 A5 A4 A3 A2 A1 A0 ] */
				/* To   : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
				pPixelsRGBA_8888[i] = ((pixel >> 8) & 0x00FFFFFF) | ((pixel << 24) & 0xFF000000);
			}
		}
		return pPixelsRGBA_8888;
	}

	public static int[] convertRGBA_8888toARGB_8888_FlippedVertical(final int[] pPixelsRGBA_8888, final int pWidth, final int pHeight) {
		return GLHelper.convertRGBA_8888toARGB_8888_FlippedVertical(pPixelsRGBA_8888, pWidth, pHeight, ByteOrder.nativeOrder());
	}

	public static int[] convertRGBA_8888toARGB_8888_FlippedVertical(final int[] pPixelsRGBA_8888, final int pWidth, final int pHeight, final ByteOrder pByteOrder) {
		final int[] pixelsARGB_8888 = new int[pWidth * pHeight];

		if(pByteOrder == ByteOrder.LITTLE_ENDIAN) {
			for(int y = 0; y < pHeight; y++) {
				for(int x = 0; x < pWidth; x++) {
					final int pixel = pPixelsRGBA_8888[x + (y * pWidth)];
					/* [A][B][G][R] to [A][R][G][B] */
					/* From : [ A7 A6 A5 A4 A3 A2 A1 A0  |  B7 B6 B5 B4 B3 B2 B1 B0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  R7 R6 R5 R4 R3 R2 R1 R0 ] */
					/* To   : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
					pixelsARGB_8888[x + ((pHeight - y - 1) * pWidth)] = (pixel & 0xFF00FF00) | ((pixel << 16) & 0x00FF0000) | ((pixel >> 16) & 0x000000FF);
				}
			}
		} else {
			for(int y = 0; y < pHeight; y++) {
				for(int x = 0; x < pWidth; x++) {
					final int pixel = pPixelsRGBA_8888[x + (y * pWidth)];
					/* [R][G][B][A] to [A][R][G][B] */
					/* From : [ R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0  |  A7 A6 A5 A4 A3 A2 A1 A0 ] */
					/* To   : [ A7 A6 A5 A4 A3 A2 A1 A0  |  R7 R6 R5 R4 R3 R2 R1 R0  |  G7 G6 G5 G4 G3 G2 G1 G0  |  B7 B6 B5 B4 B3 B2 B1 B0 ] */
					pixelsARGB_8888[x + ((pHeight - y - 1) * pWidth)] = ((pixel >> 8) & 0x00FFFFFF) | ((pixel << 24) & 0xFF000000);
				}
			}
		}
		return pixelsARGB_8888;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
