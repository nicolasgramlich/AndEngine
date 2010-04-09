package org.anddev.andengine.opengl;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.buffer.BufferObject;
import org.anddev.andengine.util.Debug;

import android.os.Build;

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

	private static int mCurrentHardwareBufferID = -1;
	private static int mCurrentHardwareTextureID = -1;
	private static int mCurrentMatrix = -1;

	private static int mCurrentSourceBlendMode = -1;
	private static int mCurrentDestionationBlendMode = -1;

	private static FloatBuffer mCurrentTextureFloatBuffer = null;
	private static FloatBuffer mCurrentVertexFloatBuffer = null;

	private static boolean mEnableDither = true;
	private static boolean mEnableLightning = true;
	private static boolean mEnableDepthTest = true;
	private static boolean mEnableMultisample = true;

	private static boolean mEnableBlend = false;
	private static boolean mEnableTextures = false;
	private static boolean mEnableTexCoordArray = false;
	private static boolean mEnableVertexArray = false;

	public static boolean EXTENSIONS_VERTEXBUFFEROBJECTS = false;
	public static boolean EXTENSIONS_DRAWTEXTURE = false;

	// ===========================================================
	// Methods
	// ===========================================================

	public static void reset(final GL10 pGL) {
		GLHelper.mCurrentHardwareBufferID = -1;
		GLHelper.mCurrentHardwareTextureID = -1;
		GLHelper.mCurrentMatrix = -1;

		GLHelper.mCurrentSourceBlendMode = -1;
		GLHelper.mCurrentDestionationBlendMode = -1;

		GLHelper.mCurrentTextureFloatBuffer = null;
		GLHelper.mCurrentVertexFloatBuffer = null;

		GLHelper.enableDither(pGL);
		GLHelper.enableLightning(pGL);
		GLHelper.enableDepthTest(pGL);
		GLHelper.enableMultisample(pGL);

		GLHelper.disableBlend(pGL);
		GLHelper.disableTextures(pGL);
		GLHelper.disableTexCoordArray(pGL);
		GLHelper.disableVertexArray(pGL);

		GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS = false;
		GLHelper.EXTENSIONS_DRAWTEXTURE = false;
	}

	public static void enableExtensions(final GL10 pGL) {
		final String version = pGL.glGetString(GL10.GL_VERSION);
		final String renderer = pGL.glGetString(GL10.GL_RENDERER);
		final String extensions = pGL.glGetString(GL10.GL_EXTENSIONS);

		Debug.d("RENDERER: " + renderer);
		Debug.d("VERSION: " + version);
		Debug.d("EXTENSIONS: " + extensions);

		final boolean isOpenGL10 = version.endsWith("1.0");
		final boolean isSoftwareRenderer = renderer.contains("PixelFlinger");
		final boolean isVBOCapable = extensions.contains("_vertex_buffer_object");
		final boolean isDrawTextureCapable = extensions.contains("draw_texture");

		GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS = !isSoftwareRenderer && (isVBOCapable || !isOpenGL10);
		GLHelper.EXTENSIONS_DRAWTEXTURE  = isDrawTextureCapable;

		GLHelper.hackBrokenDevices();
		Debug.d("EXTENSIONS_VERXTEXBUFFEROBJECTS = " + EXTENSIONS_VERTEXBUFFEROBJECTS);
		Debug.d("EXTENSIONS_DRAWTEXTURE = " + EXTENSIONS_DRAWTEXTURE);
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

	public static void bindBuffer(final GL11 pGL11, final int pHardwareBufferID) {
		/* Reduce unnecessary buffer switching calls. */
		if(GLHelper.mCurrentHardwareBufferID != pHardwareBufferID) {
			GLHelper.mCurrentHardwareBufferID = pHardwareBufferID;
			pGL11.glBindBuffer(GL11.GL_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public static void bindTexture(final GL10 pGL, final int pHardwareTextureID) {
		/* Reduce unnecessary texture switching calls. */
		if(GLHelper.mCurrentHardwareTextureID != pHardwareTextureID) {
			GLHelper.mCurrentHardwareTextureID = pHardwareTextureID;
			pGL.glBindTexture(GL10.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	public static void texCoordPointer(final GL10 pGL, final FloatBuffer pTextureFloatBuffer) {
		if(GLHelper.mCurrentTextureFloatBuffer  != pTextureFloatBuffer) {
			GLHelper.mCurrentTextureFloatBuffer = pTextureFloatBuffer;
			pGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, pTextureFloatBuffer);
		}
	}

	public static void texCoordZeroPointer(final GL11 pGL11) {
		pGL11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
	}

	public static void vertexPointer(final GL10 pGL, final FloatBuffer pVertexFloatBuffer) {
		if(GLHelper.mCurrentVertexFloatBuffer != pVertexFloatBuffer) {
			GLHelper.mCurrentVertexFloatBuffer = pVertexFloatBuffer;
			pGL.glVertexPointer(2, GL10.GL_FLOAT, 0, pVertexFloatBuffer);
		}
	}

	public static void vertexZeroPointer(final GL11 pGL11) {
		pGL11.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);
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

	public static void bufferData(final GL11 pGL11, final BufferObject pBufferObject, final int pUsage) {
		pGL11.glBufferData(GL11.GL_ARRAY_BUFFER, pBufferObject.getByteCount(), pBufferObject.getFloatBuffer(), pUsage);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
