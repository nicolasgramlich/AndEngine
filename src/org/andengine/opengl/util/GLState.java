package org.andengine.opengl.util;

import java.nio.Buffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;

import org.andengine.engine.options.RenderOptions;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.view.ConfigChooser;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLUtils;
import android.opengl.Matrix;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 18:00:43 - 08.03.2010
 */
public class GLState {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int GL_UNPACK_ALIGNMENT_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int[] mHardwareIDContainer = new int[1];

	private String mVersion;
	private String mRenderer;
	private String mExtensions;

	private int mMaximumVertexAttributeCount;
	private int mMaximumVertexShaderUniformVectorCount;
	private int mMaximumFragmentShaderUniformVectorCount;
	private int mMaximumTextureSize;
	private int mMaximumTextureUnits;

	private int mCurrentBufferID = -1;
	private int mCurrentShaderProgramID = -1;
	private final int[] mCurrentBoundTextureIDs = new int[GLES20.GL_TEXTURE31 - GLES20.GL_TEXTURE0];
	private int mCurrentFramebufferID = -1;
	private int mCurrentActiveTextureIndex = 0;

	private int mCurrentSourceBlendMode = -1;
	private int mCurrentDestinationBlendMode = -1;

	private boolean mEnableDither = true;
	private boolean mEnableDepthTest = true;

	private boolean mEnableScissorTest = false;
	private boolean mEnableBlend = false;
	private boolean mEnableCulling = false;

	private float mLineWidth = 1;

	private final GLMatrixStack mModelViewGLMatrixStack = new GLMatrixStack();
	private final GLMatrixStack mProjectionGLMatrixStack = new GLMatrixStack();

	private final float[] mModelViewGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];
	private final float[] mProjectionGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];
	private final float[] mModelViewProjectionGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getVersion() {
		return this.mVersion;
	}

	public String getRenderer() {
		return this.mRenderer;
	}

	public String getExtensions() {
		return this.mExtensions;
	}

	public int getMaximumVertexAttributeCount() {
		return this.mMaximumVertexAttributeCount;
	}

	public int getMaximumVertexShaderUniformVectorCount() {
		return this.mMaximumVertexShaderUniformVectorCount;
	}

	public int getMaximumFragmentShaderUniformVectorCount() {
		return this.mMaximumFragmentShaderUniformVectorCount;
	}

	public int getMaximumTextureUnits() {
		return this.mMaximumTextureUnits;
	}

	public int getMaximumTextureSize() {
		return this.mMaximumTextureSize;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset(final RenderOptions pRenderOptions, final ConfigChooser pConfigChooser, final EGLConfig pEGLConfig) {
		this.mVersion = GLES20.glGetString(GLES20.GL_VERSION);
		this.mRenderer = GLES20.glGetString(GLES20.GL_RENDERER);
		this.mExtensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);

		this.mMaximumVertexAttributeCount = this.getInteger(GLES20.GL_MAX_VERTEX_ATTRIBS);
		this.mMaximumVertexShaderUniformVectorCount = this.getInteger(GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS);
		this.mMaximumFragmentShaderUniformVectorCount = this.getInteger(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS);
		this.mMaximumTextureUnits = this.getInteger(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
		this.mMaximumTextureSize = this.getInteger(GLES20.GL_MAX_TEXTURE_SIZE);

		Debug.d("VERSION: " + this.mVersion);
		Debug.d("RENDERER: " + this.mRenderer);
		Debug.d("EGLCONFIG: " + EGLConfig.class.getSimpleName() + "(Red=" + pConfigChooser.getRedSize() + ", Green=" + pConfigChooser.getGreenSize() + ", Blue=" + pConfigChooser.getBlueSize() + ", Alpha=" + pConfigChooser.getAlphaSize() + ", Depth=" + pConfigChooser.getDepthSize() + ", Stencil=" + pConfigChooser.getStencilSize() + ")");
		Debug.d("EXTENSIONS: " + this.mExtensions);
		Debug.d("MAX_VERTEX_ATTRIBS: " + this.mMaximumVertexAttributeCount);
		Debug.d("MAX_VERTEX_UNIFORM_VECTORS: " + this.mMaximumVertexShaderUniformVectorCount);
		Debug.d("MAX_FRAGMENT_UNIFORM_VECTORS: " + this.mMaximumFragmentShaderUniformVectorCount);
		Debug.d("MAX_TEXTURE_IMAGE_UNITS: " + this.mMaximumTextureUnits);
		Debug.d("MAX_TEXTURE_SIZE: " + this.mMaximumTextureSize);

		this.mModelViewGLMatrixStack.reset();
		this.mProjectionGLMatrixStack.reset();

		this.mCurrentBufferID = -1;
		this.mCurrentShaderProgramID = -1;
		Arrays.fill(this.mCurrentBoundTextureIDs, -1);
		this.mCurrentFramebufferID = -1;
		this.mCurrentActiveTextureIndex = 0;

		this.mCurrentSourceBlendMode = -1;
		this.mCurrentDestinationBlendMode = -1;

		this.enableDither();
		this.enableDepthTest();

		this.disableBlend();
		this.disableCulling();

		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

		this.mLineWidth = 1;
	}

	public void enableScissorTest() {
		if(!this.mEnableScissorTest) {
			this.mEnableScissorTest = true;
			GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
		}
	}
	public void disableScissorTest() {
		if(this.mEnableScissorTest) {
			this.mEnableScissorTest = false;
			GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		}
	}

	public void enableBlend() {
		if(!this.mEnableBlend) {
			this.mEnableBlend = true;
			GLES20.glEnable(GLES20.GL_BLEND);
		}
	}
	public void disableBlend() {
		if(this.mEnableBlend) {
			this.mEnableBlend = false;
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}

	public void enableCulling() {
		if(!this.mEnableCulling) {
			this.mEnableCulling = true;
			GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
	}
	public void disableCulling() {
		if(this.mEnableCulling) {
			this.mEnableCulling = false;
			GLES20.glDisable(GLES20.GL_CULL_FACE);
		}
	}

	public void enableDither() {
		if(!this.mEnableDither) {
			this.mEnableDither = true;
			GLES20.glEnable(GLES20.GL_DITHER);
		}
	}
	public void disableDither() {
		if(this.mEnableDither) {
			this.mEnableDither = false;
			GLES20.glDisable(GLES20.GL_DITHER);
		}
	}

	public void enableDepthTest() {
		if(!this.mEnableDepthTest) {
			this.mEnableDepthTest = true;
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		}
	}
	public void disableDepthTest() {
		if(this.mEnableDepthTest) {
			this.mEnableDepthTest = false;
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		}
	}

	public int generateBuffer() {
		GLES20.glGenBuffers(1, this.mHardwareIDContainer, 0);
		return this.mHardwareIDContainer[0];
	}

	public int generateBuffer(final int pSize, final int pUsage) {
		GLES20.glGenBuffers(1, this.mHardwareIDContainer, 0);
		final int hardwareBufferID = this.mHardwareIDContainer[0];

		this.bindBuffer(hardwareBufferID);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, pSize, null, pUsage);
		this.bindBuffer(0);

		return hardwareBufferID;
	}

	public void bindBuffer(final int pHardwareBufferID) {
		if(this.mCurrentBufferID != pHardwareBufferID) {
			this.mCurrentBufferID = pHardwareBufferID;
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public void deleteBuffer(final int pHardwareBufferID) {
		if(this.mCurrentBufferID == pHardwareBufferID) {
			this.mCurrentBufferID = -1;
		}
		this.mHardwareIDContainer[0] = pHardwareBufferID;
		GLES20.glDeleteBuffers(1, this.mHardwareIDContainer, 0);
	}

	public int generateFramebuffer() {
		GLES20.glGenFramebuffers(1, this.mHardwareIDContainer, 0);
		return this.mHardwareIDContainer[0];
	}

	public void bindFramebuffer(final int pFramebufferID) {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, pFramebufferID);
	}

	public int getFramebufferStatus() {
		return GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
	}

	public void checkFramebufferStatus() {
		final int framebufferStatus = this.getFramebufferStatus();
		switch(framebufferStatus) {
			case GLES20.GL_FRAMEBUFFER_COMPLETE:
				return;
			case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
				throw new GLException(framebufferStatus, "GL_FRAMEBUFFER_UNSUPPORTED");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				throw new GLException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
				throw new GLException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				throw new GLException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			case 0:
				this.checkError();
			default:
				throw new GLException(framebufferStatus);
		}
	}

	public int getActiveFramebuffer() {
		return this.getInteger(GLES20.GL_FRAMEBUFFER_BINDING);
	}

	public void deleteFramebuffer(final int pHardwareFramebufferID) {
		if(this.mCurrentFramebufferID == pHardwareFramebufferID) {
			this.mCurrentFramebufferID = -1;
		}
		this.mHardwareIDContainer[0] = pHardwareFramebufferID;
		GLES20.glDeleteFramebuffers(1, this.mHardwareIDContainer, 0);
	}

	public void useProgram(final int pShaderProgramID) {
		if(this.mCurrentShaderProgramID != pShaderProgramID) {
			this.mCurrentShaderProgramID = pShaderProgramID;
			GLES20.glUseProgram(pShaderProgramID);
		}
	}

	public void deleteProgram(final int pShaderProgramID) {
		if(this.mCurrentShaderProgramID == pShaderProgramID) {
			this.mCurrentShaderProgramID = -1;
		}
		GLES20.glDeleteProgram(pShaderProgramID);
	}

	public int generateTexture() {
		GLES20.glGenTextures(1, this.mHardwareIDContainer, 0);
		return this.mHardwareIDContainer[0];
	}

	/**
	 * @return {@link GLES20#GL_TEXTURE0} to {@link GLES20#GL_TEXTURE31}
	 */
	public int getActiveTexture() {
		return this.mCurrentActiveTextureIndex + GLES20.GL_TEXTURE0;
	}

	/**
	 * @param pGLActiveTexture from {@link GLES20#GL_TEXTURE0} to {@link GLES20#GL_TEXTURE31}.
	 */
	public void activeTexture(final int pGLActiveTexture) {
		final int activeTextureIndex = pGLActiveTexture - GLES20.GL_TEXTURE0;
		if(pGLActiveTexture != this.mCurrentActiveTextureIndex) {
			this.mCurrentActiveTextureIndex = activeTextureIndex;
			GLES20.glActiveTexture(pGLActiveTexture);
		}
	}

	/**
	 * @see {@link GLState#forceBindTexture(GLES20, int)}
	 * @param GLES20
	 * @param pHardwareTextureID
	 */
	public void bindTexture(final int pHardwareTextureID) {
		if(this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] != pHardwareTextureID) {
			this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] = pHardwareTextureID;
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	public void deleteTexture(final int pHardwareTextureID) {
		if(this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] == pHardwareTextureID) {
			this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] = -1;
		}
		this.mHardwareIDContainer[0] = pHardwareTextureID;
		GLES20.glDeleteTextures(1, this.mHardwareIDContainer, 0);
	}

	public void blendFunction(final int pSourceBlendMode, final int pDestinationBlendMode) {
		if(this.mCurrentSourceBlendMode != pSourceBlendMode || this.mCurrentDestinationBlendMode != pDestinationBlendMode) {
			this.mCurrentSourceBlendMode = pSourceBlendMode;
			this.mCurrentDestinationBlendMode = pDestinationBlendMode;
			GLES20.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
		}
	}

	public void lineWidth(final float pLineWidth) {
		if(this.mLineWidth  != pLineWidth) {
			this.mLineWidth = pLineWidth;
			GLES20.glLineWidth(pLineWidth);
		}
	}

	public void pushModelViewGLMatrix() {
		this.mModelViewGLMatrixStack.glPushMatrix();
	}

	public void popModelViewGLMatrix() {
		this.mModelViewGLMatrixStack.glPopMatrix();
	}

	public void loadModelViewGLMatrixIdentity() {
		this.mModelViewGLMatrixStack.glLoadIdentity();
	}

	public void translateModelViewGLMatrixf(final float pX, final float pY, final float pZ) {
		this.mModelViewGLMatrixStack.glTranslatef(pX, pY, pZ);
	}

	public void rotateModelViewGLMatrixf(final float pAngle, final float pX, final float pY, final float pZ) {
		this.mModelViewGLMatrixStack.glRotatef(pAngle, pX, pY, pZ);
	}

	public void scaleModelViewGLMatrixf(final float pScaleX, final float pScaleY, final int pScaleZ) {
		this.mModelViewGLMatrixStack.glScalef(pScaleX, pScaleY, pScaleZ);
	}

	public void skewModelViewGLMatrixf(final float pSkewX, final float pSkewY) {
		this.mModelViewGLMatrixStack.glSkewf(pSkewX, pSkewY);
	}

	public void orthoModelViewGLMatrixf(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		this.mModelViewGLMatrixStack.glOrthof(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public void pushProjectionGLMatrix() {
		this.mProjectionGLMatrixStack.glPushMatrix();
	}

	public void popProjectionGLMatrix() {
		this.mProjectionGLMatrixStack.glPopMatrix();
	}

	public void loadProjectionGLMatrixIdentity() {
		this.mProjectionGLMatrixStack.glLoadIdentity();
	}

	public void translateProjectionGLMatrixf(final float pX, final float pY, final float pZ) {
		this.mProjectionGLMatrixStack.glTranslatef(pX, pY, pZ);
	}

	public void rotateProjectionGLMatrixf(final float pAngle, final float pX, final float pY, final float pZ) {
		this.mProjectionGLMatrixStack.glRotatef(pAngle, pX, pY, pZ);
	}

	public void scaleProjectionGLMatrixf(final float pScaleX, final float pScaleY, final int pScaleZ) {
		this.mProjectionGLMatrixStack.glScalef(pScaleX, pScaleY, pScaleZ);
	}

	public void skewProjectionGLMatrixf(final float pSkewX, final float pSkewY) {
		this.mProjectionGLMatrixStack.glSkewf(pSkewX, pSkewY);
	}

	public void orthoProjectionGLMatrixf(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		this.mProjectionGLMatrixStack.glOrthof(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public float[] getModelViewGLMatrix() {
		this.mModelViewGLMatrixStack.getMatrix(this.mModelViewGLMatrix);
		return this.mModelViewGLMatrix;
	}

	public float[] getProjectionGLMatrix() {
		this.mProjectionGLMatrixStack.getMatrix(this.mProjectionGLMatrix);
		return this.mProjectionGLMatrix;
	}

	public float[] getModelViewProjectionGLMatrix() {
		Matrix.multiplyMM(this.mModelViewProjectionGLMatrix, 0, this.mProjectionGLMatrixStack.mMatrixStack, this.mProjectionGLMatrixStack.mMatrixStackOffset, this.mModelViewGLMatrixStack.mMatrixStack, this.mModelViewGLMatrixStack.mMatrixStackOffset);
		return this.mModelViewProjectionGLMatrix;
	}

	public void resetModelViewGLMatrixStack() {
		this.mModelViewGLMatrixStack.reset();
	}

	public void resetProjectionGLMatrixStack() {
		this.mProjectionGLMatrixStack.reset();
	}

	public void resetGLMatrixStacks() {
		this.mModelViewGLMatrixStack.reset();
		this.mProjectionGLMatrixStack.reset();
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 * @param pBorder
	 */
	public void glTexImage2D(final int pTarget, final int pLevel, final Bitmap pBitmap, final int pBorder, final PixelFormat pPixelFormat) {
		final Buffer pixelBuffer = GLHelper.getPixels(pBitmap, pPixelFormat, ByteOrder.BIG_ENDIAN);

		GLES20.glTexImage2D(pTarget, pLevel, pPixelFormat.getGLInternalFormat(), pBitmap.getWidth(), pBitmap.getHeight(), pBorder, pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
	}

	/**
	 * <b>Note:</b> does not pre-multiply the alpha channel!</br>
	 * Except that difference, same as: {@link GLUtils#texSubImage2D(int, int, int, int, Bitmap, int, int)}</br>
	 * </br>
	 * See topic: '<a href="http://groups.google.com/group/android-developers/browse_thread/thread/baa6c33e63f82fca">PNG loading that doesn't premultiply alpha?</a>'
	 */
	public void glTexSubImage2D(final int pTarget, final int pLevel, final int pX, final int pY, final Bitmap pBitmap, final PixelFormat pPixelFormat) {
		final Buffer pixelBuffer = GLHelper.getPixels(pBitmap, pPixelFormat, ByteOrder.BIG_ENDIAN);

		GLES20.glTexSubImage2D(pTarget, pLevel, pX, pY, pBitmap.getWidth(), pBitmap.getHeight(), pPixelFormat.getGLFormat(), pPixelFormat.getGLType(), pixelBuffer);
	}

	/**
	 * Tells the OpenGL driver to send all pending commands to the GPU immediately.
	 *
	 * @see {@link GLState#finish()},
	 * 		{@link RenderTexture#end(GLState, boolean, boolean)}.
	 */
	public void flush() {
		GLES20.glFlush();
	}

	/**
	 * Tells the OpenGL driver to send all pending commands to the GPU immediately,
	 * and then blocks until the effects of those commands have been completed on the GPU.
	 * Since this is a costly method it should be only called when really needed.
	 *
	 * @see {@link GLState#flush()},
	 * 		{@link RenderTexture#end(GLState, boolean, boolean)}.
	 */
	public void finish() {
		GLES20.glFinish();
	}

	public int getInteger(final int pAttribute) {
		GLES20.glGetIntegerv(pAttribute, this.mHardwareIDContainer, 0);
		return this.mHardwareIDContainer[0];
	}

	public int getError() {
		return GLES20.glGetError();
	}

	public void checkError() throws GLException { // TODO Use more often!
		final int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR) {
			throw new GLException(error);
		}
	}

	public void clearError() {
		GLES20.glGetError();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
