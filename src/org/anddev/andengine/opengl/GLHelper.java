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

	public static void color4f(final GL10 pGL, float pRed, float pGreen, float pBlue, final float pAlpha) {
		pGL.glColor4f(pRed, pGreen, pBlue, pAlpha);
	}

	public static void enableTextures(final GL10 pGL) {
		pGL.glEnable(GL10.GL_TEXTURE_2D);
	}

	public static void disableTextures(final GL10 pGL) {
		pGL.glDisable(GL10.GL_TEXTURE_2D);
	}

	public static void enableVertexArray(final GL10 pGL) {
		pGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public static void disableVertexArray(final GL10 pGL) {
		pGL.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public static void enableTexCoordArray(final GL10 pGL) {
		pGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public static void disableTexCoordArray(final GL10 pGL) {
		pGL.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public static void bindTexture(final GL10 pGL, final int pHardwareTextureID) {
		pGL.glBindTexture(GL10.GL_TEXTURE_2D, pHardwareTextureID);
	}

	public static void texCoordPointer(final GL10 pGL, final ByteBuffer pUVMappingByteBuffer, final int pType) {
		pGL.glTexCoordPointer(2, pType, 0, pUVMappingByteBuffer);
	}

	public static void vertexPointer(final GL10 pGL, final ByteBuffer pByteBuffer, final int pType) {
		pGL.glVertexPointer(2, pType, 0, pByteBuffer);
	}
	
	public static void blendMode(final GL10 pGL, final int pSourceBlendMode, final int pDestinationBlendMode) {
		pGL.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
