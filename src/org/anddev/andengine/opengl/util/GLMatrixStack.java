package org.anddev.andengine.opengl.util;

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

	public final static int MAX_DEPTH = 32;
	public static final int GLMATRIX_SIZE = 16;

	// ===========================================================
	// Fields
	// ===========================================================

	final float[] mValues = new float[GLMatrixStack.MAX_DEPTH * GLMatrixStack.GLMATRIX_SIZE];  // TODO Better name
	int mTop; // TODO Better name

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
		System.arraycopy(this.mValues, this.mTop, pMatrix, 0, GLMatrixStack.GLMATRIX_SIZE);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public GLMatrixStack glLoadIdentity() {
		Matrix.setIdentityM(this.mValues, this.mTop);
		return this;
	}

	public void setToResult(final GLMatrixStack pGLMatrixA, final GLMatrixStack pGLMatrixB) {
		System.arraycopy(this.mValues, this.mTop, this.mTemp, 0, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.mValues, this.mTop, this.mTemp, 0, pGLMatrixB.mValues, 0);
	}

	public void glTranslatef(final float pX, final float pY, final float pZ) {
		Matrix.translateM(this.mValues, this.mTop, pX, pY, pZ);
	}

	public void glRotatef(final float pAngle, final float pX, final float pY, final float pZ) {
		Matrix.setRotateM(this.mTemp, 0, pAngle, pX, pY, pZ);
		System.arraycopy(this.mValues, this.mTop, this.mTemp, GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.mValues, this.mTop, this.mTemp, GLMatrixStack.GLMATRIX_SIZE, this.mTemp, 0);
	}

	public void glScalef(final float pScaleX, final float pScaleY, final float pScaleZ) {
		Matrix.scaleM(this.mValues, this.mTop, pScaleX, pScaleY, pScaleZ);
	}

	public void glOrthof(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		Matrix.orthoM(this.mValues, this.mTop, pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public void glPushMatrix() {
		System.arraycopy(this.mValues, this.mTop, this.mValues, this.mTop + GLMatrixStack.GLMATRIX_SIZE, GLMatrixStack.GLMATRIX_SIZE);
		this.mTop += GLMatrixStack.GLMATRIX_SIZE;
	}

	public void glPopMatrix() {
		this.mTop -= GLMatrixStack.GLMATRIX_SIZE;
	}

	public void reset() {
		this.mTop = 0;
		glLoadIdentity();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
