package org.andengine.opengl.util;

import org.andengine.util.exception.AndEngineRuntimeException;
import org.andengine.util.math.MathConstants;

import android.opengl.Matrix;

/**
 * TODO Measure performance with inlined or native Matrix implementations.
 *
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:49:23 - 04.08.2011
 */
public class GLMatrixStack {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int GLMATRIXSTACK_DEPTH_MAX = 32;
	public static final int GLMATRIX_SIZE = 16;

	private static final int GLMATRIXSTACKOFFSET_UNDERFLOW = -1 * GLMatrixStack.GLMATRIX_SIZE;
	private static final int GLMATRIXSTACKOFFSET_OVERFLOW = GLMatrixStack.GLMATRIXSTACK_DEPTH_MAX * GLMatrixStack.GLMATRIX_SIZE;

	// ===========================================================
	// Fields
	// ===========================================================

	final float[] mMatrixStack = new float[GLMatrixStack.GLMATRIXSTACK_DEPTH_MAX * GLMatrixStack.GLMATRIX_SIZE];
	int mMatrixStackOffset;

	private final float[] mTemp = new float[2 * GLMatrixStack.GLMATRIX_SIZE];

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLMatrixStack() {
		this.glLoadIdentity();
	}

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void getMatrix(final float[] pMatrix) {
		if (this.mMatrixStackOffset - GLMatrixStack.GLMATRIX_SIZE <= GLMatrixStack.GLMATRIXSTACKOFFSET_UNDERFLOW) {
			throw new GLMatrixStackUnderflowException();
		}

		System.arraycopy(this.mMatrixStack, this.mMatrixStackOffset, pMatrix, 0, GLMatrixStack.GLMATRIX_SIZE);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void glLoadIdentity() {
		Matrix.setIdentityM(this.mMatrixStack, this.mMatrixStackOffset);
	}

	public void glTranslatef(final float pX, final float pY, final float pZ) {
		Matrix.translateM(this.mMatrixStack, this.mMatrixStackOffset, pX, pY, pZ);
	}

	public void glRotatef(final float pAngle, final float pX, final float pY, final float pZ) {
		Matrix.setRotateM(this.mTemp, 0, pAngle, pX, pY, pZ);
		System.arraycopy(this.mMatrixStack, this.mMatrixStackOffset, this.mTemp, GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.mMatrixStack, this.mMatrixStackOffset, this.mTemp, GLMatrixStack.GLMATRIX_SIZE, this.mTemp, 0);
	}

	public void glScalef(final float pScaleX, final float pScaleY, final float pScaleZ) {
		Matrix.scaleM(this.mMatrixStack, this.mMatrixStackOffset, pScaleX, pScaleY, pScaleZ);
	}

	public void glSkewf(final float pSkewX, final float pSkewY) {
		GLMatrixStack.setSkewM(this.mTemp, 0, pSkewX, pSkewY);
		System.arraycopy(this.mMatrixStack, this.mMatrixStackOffset, this.mTemp, GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.mMatrixStack, this.mMatrixStackOffset, this.mTemp, GLMatrixStack.GLMATRIX_SIZE, this.mTemp, 0);
	}

	public void glOrthof(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		Matrix.orthoM(this.mMatrixStack, this.mMatrixStackOffset, pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public void glPushMatrix() throws GLMatrixStackOverflowException {
		if (this.mMatrixStackOffset + GLMatrixStack.GLMATRIX_SIZE >= GLMatrixStack.GLMATRIXSTACKOFFSET_OVERFLOW) {
			throw new GLMatrixStackOverflowException();
		}

		System.arraycopy(this.mMatrixStack, this.mMatrixStackOffset, this.mMatrixStack, this.mMatrixStackOffset + GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		this.mMatrixStackOffset += GLMatrixStack.GLMATRIX_SIZE;
	}

	public void glPopMatrix() {
		if (this.mMatrixStackOffset - GLMatrixStack.GLMATRIX_SIZE <= GLMatrixStack.GLMATRIXSTACKOFFSET_UNDERFLOW) {
			throw new GLMatrixStackUnderflowException();
		}

		this.mMatrixStackOffset -= GLMatrixStack.GLMATRIX_SIZE;
	}

	public void reset() {
		this.mMatrixStackOffset = 0;
		this.glLoadIdentity();
	}

	private static void setSkewM(final float[] pMatrixStack, final int pOffset, final float pSkewX, final float pSkewY) {
		pMatrixStack[pOffset + 0] = 1.0f;
		pMatrixStack[pOffset + 1] = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewY);
		pMatrixStack[pOffset + 2] = 0.0f;
		pMatrixStack[pOffset + 3] = 0.0f;

		pMatrixStack[pOffset + 4] = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewX);
		pMatrixStack[pOffset + 5] = 1.0f;
		pMatrixStack[pOffset + 6] = 0.0f;
		pMatrixStack[pOffset + 7] = 0.0f;

		pMatrixStack[pOffset + 8] = 0.0f;
		pMatrixStack[pOffset + 9] = 0.0f;
		pMatrixStack[pOffset + 10] = 1.0f;
		pMatrixStack[pOffset + 11] = 0.0f;

		pMatrixStack[pOffset + 12] = 0.0f;
		pMatrixStack[pOffset + 13] = 0.0f;
		pMatrixStack[pOffset + 14] = 0.0f;
		pMatrixStack[pOffset + 15] = 1.0f;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class GLMatrixStackOverflowException extends AndEngineRuntimeException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = -800847781599300100L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public GLMatrixStackOverflowException() {

		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class GLMatrixStackUnderflowException extends AndEngineRuntimeException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = -3268021423136372954L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public GLMatrixStackUnderflowException() {

		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
