package org.anddev.andengine.opengl.util;

import org.anddev.andengine.util.exception.AndEngineException;

import android.opengl.Matrix;

/**
 * TODO Measure performance with inlined or native Matrix implementations.
 * 
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:49:23 - 04.08.2011
 */
public class GLMatrixStack {
	// ===========================================================
	// Constants
	// ===========================================================

	public final static int GLMATRIXSTACK_DEPTH_MAX = 32;
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

	public void setToResult(final GLMatrixStack pGLMatrixA, final GLMatrixStack pGLMatrixB) {
		System.arraycopy(this.mMatrixStack, this.mMatrixStackOffset, this.mTemp, 0, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.mMatrixStack, this.mMatrixStackOffset, this.mTemp, 0, pGLMatrixB.mMatrixStack, 0);
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
		if (this.mMatrixStackOffset - GLMatrixStack.GLMATRIX_SIZE < GLMatrixStack.GLMATRIXSTACKOFFSET_UNDERFLOW) {
			throw new GLMatrixStackUnderflowException();
		}

		this.mMatrixStackOffset -= GLMatrixStack.GLMATRIX_SIZE;
	}

	public void reset() {
		this.mMatrixStackOffset = 0;
		this.glLoadIdentity();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class GLMatrixStackOverflowException extends AndEngineException {
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

	public class GLMatrixStackUnderflowException extends AndEngineException {
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
