package org.anddev.andengine.opengl;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
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

	private static int mCurrentHardwareTexture = -1;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void setColor(final GL10 pGL, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		pGL.glColor4f(pRed, pGreen, pBlue, pAlpha);
	}

	public static void enableVertexArray(final GL10 pGL) {
		pGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public static void enableTexCoordArray(final GL10 pGL) {
		pGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public static void enableBlend(final GL10 pGL) {
		pGL.glEnable(GL10.GL_BLEND);
	}

	public static void enableTextures(final GL10 pGL) {
		pGL.glEnable(GL10.GL_TEXTURE_2D);
	}

	public static void disableLightning(final GL10 pGL) {
		pGL.glDisable(GL10.GL_LIGHTING);
	}

	public static void disableDither(final GL10 pGL) {
		pGL.glDisable(GL10.GL_DITHER);
	}

	public static void disableDepthTest(final GL10 pGL) {
		pGL.glDisable(GL10.GL_DEPTH_TEST);
	}

	public static void disableMultisample(final GL10 pGL) {
		pGL.glDisable(GL10.GL_MULTISAMPLE);
	}

	public static void disableTextures(final GL10 pGL) {
		pGL.glDisable(GL10.GL_TEXTURE_2D);
	}

	public static void disableVertexArray(final GL10 pGL) {
		pGL.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public static void disableTexCoordArray(final GL10 pGL) {
		pGL.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public static void bindTexture(final GL10 pGL, final int pHardwareTextureID) {
		/* Reduce unnecessary texture switching calls. */
		if(GLHelper.mCurrentHardwareTexture != pHardwareTextureID) {
			GLHelper.mCurrentHardwareTexture = pHardwareTextureID;
			pGL.glBindTexture(GL10.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	public static void texCoordPointer(final GL10 pGL, final ByteBuffer pUVMappingByteBuffer, final int pType) {
		pGL.glTexCoordPointer(2, pType, 0, pUVMappingByteBuffer);
	}

	public static void vertexPointer(final GL10 pGL, final ByteBuffer pByteBuffer, final int pType) {
		pGL.glVertexPointer(2, pType, 0, pByteBuffer);
	}

	public static void blendFunction(final GL10 pGL, final int pSourceBlendMode, final int pDestinationBlendMode) {
		pGL.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
	}
	
	public static void switchToModelViewMatrix(final GL10 pGL) {
		pGL.glMatrixMode(GL10.GL_MODELVIEW);
	}
	
	public static void switchToProjectionMatrix(final GL10 pGL) {
		pGL.glMatrixMode(GL10.GL_PROJECTION);
	}

	public static void setModelViewIdentityMatrix(final GL10 pGL) {
		pGL.glMatrixMode(GL10.GL_MODELVIEW);
		pGL.glLoadIdentity();
	}

	public static void setShadeModelFlat(final GL10 pGL) {
		pGL.glShadeModel(GL10.GL_FLAT);
	}

	public static void setPerspectiveCorrectionHintFastest(final GL10 pGL) {
		pGL.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
