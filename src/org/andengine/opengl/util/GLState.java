package org.andengine.opengl.util;

import java.nio.Buffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;

import org.andengine.BuildConfig;
import org.andengine.engine.options.RenderOptions;
import org.andengine.opengl.exception.GLException;
import org.andengine.opengl.exception.GLFrameBufferException;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.view.ConfigChooser;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.opengl.GLES20;
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

	public static final float LINE_WIDTH_DEFAULT = 1.0f;

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

	private int mCurrentArrayBufferID = -1;
	private int mCurrentIndexBufferID = -1;
	private int mCurrentShaderProgramID = -1;
	private final int[] mCurrentBoundTextureIDs = new int[GLES20.GL_TEXTURE31 - GLES20.GL_TEXTURE0];
	private int mCurrentFramebufferID = -1;
	private int mCurrentActiveTextureIndex = 0;

	private int mCurrentSourceBlendMode = -1;
	private int mCurrentDestinationBlendMode = -1;

	private boolean mDitherEnabled = true;
	private boolean mDepthTestEnabled = true;

	private boolean mScissorTestEnabled = false;
	private boolean mBlendEnabled = false;
	private boolean mCullingEnabled = false;

	private float mLineWidth = 1;

	private final GLMatrixStack mModelViewGLMatrixStack = new GLMatrixStack();
	private final GLMatrixStack mProjectionGLMatrixStack = new GLMatrixStack();
	private final GLScissorStack mScissorStack = new GLScissorStack();

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

		if (BuildConfig.DEBUG) {
			Debug.d("VERSION: " + this.mVersion);
			Debug.d("RENDERER: " + this.mRenderer);
			Debug.d("EGLCONFIG: " + EGLConfig.class.getSimpleName() + "(Red=" + pConfigChooser.getActualRedSize() + ", Green=" + pConfigChooser.getActualGreenSize() + ", Blue=" + pConfigChooser.getActualBlueSize() + ", Alpha=" + pConfigChooser.getActualAlphaSize() + ", Depth=" + pConfigChooser.getActualDepthSize() + ", Stencil=" + pConfigChooser.getActualStencilSize() + ")");
			Debug.d("EXTENSIONS: " + this.mExtensions);
			Debug.d("MAX_VERTEX_ATTRIBS: " + this.mMaximumVertexAttributeCount);
			Debug.d("MAX_VERTEX_UNIFORM_VECTORS: " + this.mMaximumVertexShaderUniformVectorCount);
			Debug.d("MAX_FRAGMENT_UNIFORM_VECTORS: " + this.mMaximumFragmentShaderUniformVectorCount);
			Debug.d("MAX_TEXTURE_IMAGE_UNITS: " + this.mMaximumTextureUnits);
			Debug.d("MAX_TEXTURE_SIZE: " + this.mMaximumTextureSize);
		}

		this.mModelViewGLMatrixStack.reset();
		this.mProjectionGLMatrixStack.reset();
		this.mScissorStack.reset();

		this.mCurrentArrayBufferID = -1;
		this.mCurrentIndexBufferID = -1;
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

		this.lineWidth(GLState.LINE_WIDTH_DEFAULT);

		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

		this.mLineWidth = 1;
	}

	public boolean isScissorTestEnabled() {
		return this.mScissorTestEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableScissorTest() {
		if (this.mScissorTestEnabled) {
			return true;
		}

		this.mScissorTestEnabled = true;
		GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableScissorTest() {
		if (!this.mScissorTestEnabled) {
			return false;
		}

		this.mScissorTestEnabled = false;
		GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setScissorTestEnabled(final boolean pEnabled) {
		if (pEnabled) {
			return this.enableScissorTest();
		} else {
			return this.disableScissorTest();
		}
	}

	public boolean isBlendEnabled() {
		return this.mBlendEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableBlend() {
		if (this.mBlendEnabled) {
			return true;
		}

		this.mBlendEnabled = true;
		GLES20.glEnable(GLES20.GL_BLEND);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableBlend() {
		if (!this.mBlendEnabled) {
			return false;
		}

		this.mBlendEnabled = false;
		GLES20.glDisable(GLES20.GL_BLEND);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setBlendEnabled(final boolean pEnabled) {
		if (pEnabled) {
			return this.enableBlend();
		} else {
			return this.disableBlend();
		}
	}

	public boolean isCullingEnabled() {
		return this.mCullingEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableCulling() {
		if (this.mCullingEnabled) {
			return true;
		}

		this.mCullingEnabled = true;
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableCulling() {
		if (!this.mCullingEnabled) {
			return false;
		}

		this.mCullingEnabled = false;
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setCullingEnabled(final boolean pEnabled) {
		if (pEnabled) {
			return this.enableCulling();
		} else {
			return this.disableCulling();
		}
	}

	public boolean isDitherEnabled() {
		return this.mDitherEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableDither() {
		if (this.mDitherEnabled) {
			return true;
		}

		this.mDitherEnabled = true;
		GLES20.glEnable(GLES20.GL_DITHER);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableDither() {
		if (!this.mDitherEnabled) {
			return false;
		}

		this.mDitherEnabled = false;
		GLES20.glDisable(GLES20.GL_DITHER);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setDitherEnabled(final boolean pEnabled) {
		if (pEnabled) {
			return this.enableDither();
		} else {
			return this.disableDither();
		}
	}

	public boolean isDepthTestEnabled() {
		return this.mDepthTestEnabled;
	}
	/**
	 * @return the previous state.
	 */
	public boolean enableDepthTest() {
		if (this.mDepthTestEnabled) {
			return true;
		}

		this.mDepthTestEnabled = true;
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		return false;
	}
	/**
	 * @return the previous state.
	 */
	public boolean disableDepthTest() {
		if (!this.mDepthTestEnabled) {
			return false;
		}

		this.mDepthTestEnabled = false;
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		return true;
	}
	/**
	 * @return the previous state.
	 */
	public boolean setDepthTestEnabled(final boolean pEnabled) {
		if (pEnabled) {
			return this.enableDepthTest();
		} else {
			return this.disableDepthTest();
		}
	}

	public int generateBuffer() {
		GLES20.glGenBuffers(1, this.mHardwareIDContainer, 0);
		return this.mHardwareIDContainer[0];
	}

	public int generateArrayBuffer(final int pSize, final int pUsage) {
		GLES20.glGenBuffers(1, this.mHardwareIDContainer, 0);
		final int hardwareBufferID = this.mHardwareIDContainer[0];

		this.bindArrayBuffer(hardwareBufferID);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, pSize, null, pUsage);
		this.bindArrayBuffer(0);

		return hardwareBufferID;
	}

	public void bindArrayBuffer(final int pHardwareBufferID) {
		if (this.mCurrentArrayBufferID != pHardwareBufferID) {
			this.mCurrentArrayBufferID = pHardwareBufferID;
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public void deleteArrayBuffer(final int pHardwareBufferID) {
		if (this.mCurrentArrayBufferID == pHardwareBufferID) {
			this.mCurrentArrayBufferID = -1;
		}
		this.mHardwareIDContainer[0] = pHardwareBufferID;
		GLES20.glDeleteBuffers(1, this.mHardwareIDContainer, 0);
	}

	public int generateIndexBuffer(final int pSize, final int pUsage) {
		GLES20.glGenBuffers(1, this.mHardwareIDContainer, 0);
		final int hardwareBufferID = this.mHardwareIDContainer[0];

		this.bindIndexBuffer(hardwareBufferID);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, pSize, null, pUsage);
		this.bindIndexBuffer(0);

		return hardwareBufferID;
	}

	public void bindIndexBuffer(final int pHardwareBufferID) {
		if (this.mCurrentIndexBufferID != pHardwareBufferID) {
			this.mCurrentIndexBufferID = pHardwareBufferID;
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, pHardwareBufferID);
		}
	}

	public void deleteIndexBuffer(final int pHardwareBufferID) {
		if (this.mCurrentIndexBufferID == pHardwareBufferID) {
			this.mCurrentIndexBufferID = -1;
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

	public void checkFramebufferStatus() throws GLFrameBufferException, GLException {
		final int framebufferStatus = this.getFramebufferStatus();
		switch (framebufferStatus) {
			case GLES20.GL_FRAMEBUFFER_COMPLETE:
				return;
			case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_UNSUPPORTED");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS");
			case GLES20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				throw new GLFrameBufferException(framebufferStatus, "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			case 0:
				this.checkError();
			default:
				throw new GLFrameBufferException(framebufferStatus);
		}
	}

	public int getActiveFramebuffer() {
		return this.getInteger(GLES20.GL_FRAMEBUFFER_BINDING);
	}

	public void deleteFramebuffer(final int pHardwareFramebufferID) {
		if (this.mCurrentFramebufferID == pHardwareFramebufferID) {
			this.mCurrentFramebufferID = -1;
		}
		this.mHardwareIDContainer[0] = pHardwareFramebufferID;
		GLES20.glDeleteFramebuffers(1, this.mHardwareIDContainer, 0);
	}

	public void useProgram(final int pShaderProgramID) {
		if (this.mCurrentShaderProgramID != pShaderProgramID) {
			this.mCurrentShaderProgramID = pShaderProgramID;
			GLES20.glUseProgram(pShaderProgramID);
		}
	}

	public void deleteProgram(final int pShaderProgramID) {
		if (this.mCurrentShaderProgramID == pShaderProgramID) {
			this.mCurrentShaderProgramID = -1;
		}
		GLES20.glDeleteProgram(pShaderProgramID);
	}

	public int generateTexture() {
		GLES20.glGenTextures(1, this.mHardwareIDContainer, 0);
		return this.mHardwareIDContainer[0];
	}

	public boolean isTexture(final int pHardwareTextureID) {
		return GLES20.glIsTexture(pHardwareTextureID);
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
		if (pGLActiveTexture != this.mCurrentActiveTextureIndex) {
			this.mCurrentActiveTextureIndex = activeTextureIndex;
			GLES20.glActiveTexture(pGLActiveTexture);
		}
	}

	/**
	 * @see {@link #forceBindTexture(GLES20, int)}
	 * @param GLES20
	 * @param pHardwareTextureID
	 */
	public void bindTexture(final int pHardwareTextureID) {
		if (this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] != pHardwareTextureID) {
			this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] = pHardwareTextureID;
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, pHardwareTextureID);
		}
	}

	public void deleteTexture(final int pHardwareTextureID) {
		if (this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] == pHardwareTextureID) {
			this.mCurrentBoundTextureIDs[this.mCurrentActiveTextureIndex] = -1;
		}
		this.mHardwareIDContainer[0] = pHardwareTextureID;
		GLES20.glDeleteTextures(1, this.mHardwareIDContainer, 0);
	}

	public void blendFunction(final int pSourceBlendMode, final int pDestinationBlendMode) {
		if (this.mCurrentSourceBlendMode != pSourceBlendMode || this.mCurrentDestinationBlendMode != pDestinationBlendMode) {
			this.mCurrentSourceBlendMode = pSourceBlendMode;
			this.mCurrentDestinationBlendMode = pDestinationBlendMode;
			GLES20.glBlendFunc(pSourceBlendMode, pDestinationBlendMode);
		}
	}

	public void lineWidth(final float pLineWidth) {
		if (this.mLineWidth != pLineWidth) {
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

	public void scaleProjectionGLMatrixf(final float pScaleX, final float pScaleY, final float pScaleZ) {
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

	public void glPushScissor(final int pX, final int pY, final int pWidth, final int pHeight) {
		this.mScissorStack.glPushScissor(pX, pY, pWidth, pHeight);
	}

	public void glPopScissor() {
		this.mScissorStack.glPopScissor();
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
	 * @see {@link #finish()},
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
	 * @see {@link #flush()},
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

	public void checkError() throws GLException {
		final int error = GLES20.glGetError();
		if (error != GLES20.GL_NO_ERROR) {
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
