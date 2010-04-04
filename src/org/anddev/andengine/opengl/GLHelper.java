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
	private static int mCurrentMatrix = -1;

	private static int mCurrentSourceBlendMode = -1;
	private static int mCurrentDestionationBlendMode = -1;

	private static ByteBuffer mCurrentTextureByteBuffer = null;
	private static ByteBuffer mCurrentVertexByteBuffer = null;

	private static boolean mEnableDither = true;
	private static boolean mEnableLightning = true;
	private static boolean mEnableDepthTest = true;
	private static boolean mEnableMultisample = true;

	private static boolean mEnableBlend = false;
	private static boolean mEnableTextures = false;
	private static boolean mEnableTexCoordArray = false;
	private static boolean mEnableVertexArray = false;

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
	
	public static void reset() {
		GLHelper.mCurrentHardwareTexture = -1;
		GLHelper.mCurrentMatrix = -1;

		GLHelper.mCurrentSourceBlendMode = -1;
		GLHelper.mCurrentDestionationBlendMode = -1;

		GLHelper.mCurrentTextureByteBuffer = null;
		GLHelper.mCurrentVertexByteBuffer = null;
		
		GLHelper.mEnableDither = true;
		GLHelper.mEnableLightning = true;
		GLHelper.mEnableDepthTest = true;
		GLHelper.mEnableMultisample = true;

		GLHelper.mEnableBlend = false;
		GLHelper.mEnableTextures = false;
		GLHelper.mEnableTexCoordArray = false;
		GLHelper.mEnableVertexArray = false;
	}

	public static void setColor(final GL10 pGL, final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		pGL.glColor4f(pRed, pGreen, pBlue, pAlpha);
	}

	public static void enableVertexArray(final GL10 pGL) {
		if(!GLHelper.mEnableVertexArray) {
			GLHelper.mEnableVertexArray = true;
			pGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}
	public static void disableVertexArray(final GL10 pGL) {
		if(GLHelper.mEnableVertexArray) {
			GLHelper.mEnableVertexArray = false;
			pGL.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	public static void enableTexCoordArray(final GL10 pGL) {
		if(!GLHelper.mEnableTexCoordArray) {
			GLHelper.mEnableTexCoordArray = true;
			pGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}
	public static void disableTexCoordArray(final GL10 pGL) {
		if(GLHelper.mEnableTexCoordArray) {
			GLHelper.mEnableTexCoordArray = false;
			pGL.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}

	public static void enableBlend(final GL10 pGL) {
		if(!GLHelper.mEnableBlend) {
			GLHelper.mEnableBlend = true;
			pGL.glEnable(GL10.GL_BLEND);
		}
	}
	public static void disableBlend(final GL10 pGL) {
		if(GLHelper.mEnableBlend) {
			GLHelper.mEnableBlend = false;
			pGL.glDisable(GL10.GL_BLEND);
		}
	}

	public static void enableTextures(final GL10 pGL) {
		if(!GLHelper.mEnableTextures) {
			GLHelper.mEnableTextures = true;
			pGL.glEnable(GL10.GL_TEXTURE_2D);
		}
	}
	public static void disableTextures(final GL10 pGL) {
		if(GLHelper.mEnableTextures) {
			GLHelper.mEnableTextures = false;
			pGL.glDisable(GL10.GL_TEXTURE_2D);
		}
	}

	public static void enableLightning(final GL10 pGL) {
		if(!GLHelper.mEnableLightning) {
			GLHelper.mEnableLightning = true;
			pGL.glEnable(GL10.GL_LIGHTING);
		}
	}
	public static void disableLightning(final GL10 pGL) {
		if(GLHelper.mEnableLightning) {
			GLHelper.mEnableLightning = false;
			pGL.glDisable(GL10.GL_LIGHTING);
		}
	}

	public static void enableDither(final GL10 pGL) {
		if(!GLHelper.mEnableDither) {
			GLHelper.mEnableDither = true;
			pGL.glEnable(GL10.GL_DITHER);
		}
	}
	public static void disableDither(final GL10 pGL) {
		if(GLHelper.mEnableDither) {
			GLHelper.mEnableDither = false;
			pGL.glDisable(GL10.GL_DITHER);
		}
	}

	public static void enableDepthTest(final GL10 pGL) {
		if(!GLHelper.mEnableDepthTest) {
			GLHelper.mEnableDepthTest = true;
			pGL.glEnable(GL10.GL_DEPTH_TEST);
		}
	}
	public static void disableDepthTest(final GL10 pGL) {
		if(GLHelper.mEnableDepthTest) {
			GLHelper.mEnableDepthTest = false;
			pGL.glDisable(GL10.GL_DEPTH_TEST);
		}
	}

	public static void enableMultisample(final GL10 pGL) {
		if(!GLHelper.mEnableMultisample) {
			GLHelper.mEnableMultisample = true;
			pGL.glEnable(GL10.GL_MULTISAMPLE);
		}
	}
	public static void disableMultisample(final GL10 pGL) {
		if(GLHelper.mEnableMultisample) {
			GLHelper.mEnableMultisample = false;
			pGL.glDisable(GL10.GL_MULTISAMPLE);
		}
	}

	public static void bindTexture(final GL10 pGL, final int pHardwareTextureID) {
		/* Reduce unnecessary texture switching calls. */
		if(GLHelper.mCurrentHardwareTexture != pHardwareTextureID) {
			GLHelper.mCurrentHardwareTexture = pHardwareTextureID;
			pGL.glBindTexture(GL10.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	public static void texCoordPointer(final GL10 pGL, final ByteBuffer pTextureByteBuffer) {
		if(GLHelper.mCurrentTextureByteBuffer  != pTextureByteBuffer) {
			GLHelper.mCurrentTextureByteBuffer = pTextureByteBuffer;
			pGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, pTextureByteBuffer);
		}
	}

	public static void vertexPointer(final GL10 pGL, final ByteBuffer pVertexByteBuffer) {
		if(GLHelper.mCurrentVertexByteBuffer  != pVertexByteBuffer) {
			GLHelper.mCurrentVertexByteBuffer = pVertexByteBuffer;
			pGL.glVertexPointer(2, GL10.GL_FLOAT, 0, pVertexByteBuffer);
		}
	}

	public static void blendFunction(final GL10 pGL, final int pSourceBlendMode, final int pDestinationBlendMode) {
		if(GLHelper.mCurrentSourceBlendMode != pSourceBlendMode || GLHelper.mCurrentDestionationBlendMode != pDestinationBlendMode) {
			GLHelper.mCurrentSourceBlendMode = pSourceBlendMode;
			GLHelper.mCurrentDestionationBlendMode = pDestinationBlendMode;
			pGL.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
		}
	}

	public static void switchToModelViewMatrix(final GL10 pGL) {
		/* Reduce unnecessary matrix switching calls. */
		if(GLHelper.mCurrentMatrix != GL10.GL_MODELVIEW) {
			GLHelper.mCurrentMatrix = GL10.GL_MODELVIEW;
			pGL.glMatrixMode(GL10.GL_MODELVIEW);
		}
	}

	public static void switchToProjectionMatrix(final GL10 pGL) {
		/* Reduce unnecessary matrix switching calls. */
		if(GLHelper.mCurrentMatrix != GL10.GL_PROJECTION) {
			GLHelper.mCurrentMatrix = GL10.GL_PROJECTION;
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
