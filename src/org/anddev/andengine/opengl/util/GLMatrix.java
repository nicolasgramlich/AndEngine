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
public class GLMatrix {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int GLMATRIX_SIZE = 16;

	// ===========================================================
	// Fields
	// ===========================================================

	private final float[] mValues = new float[GLMatrix.GLMATRIX_SIZE];
	private final float[] mTemp = new float[2 * GLMatrix.GLMATRIX_SIZE];

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getValues() {
		return this.mValues;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public GLMatrix setToIdentity() {
		Matrix.setIdentityM(this.mValues, 0);
		return this;
	}

	public GLMatrix setTo(final GLMatrix pGLMatrix) {
		System.arraycopy(pGLMatrix.mValues, 0, this.mValues, 0, GLMatrix.GLMATRIX_SIZE);
		return this;
	}

	public void setToResult(final GLMatrix pGLMatrixA, final GLMatrix pGLMatrixB) {
		Matrix.multiplyMM(this.mValues, 0, pGLMatrixA.mValues, 0, pGLMatrixB.mValues, 0);
	}

	public void translate(final float pX, final float pY, final float pZ) {
		Matrix.translateM(this.mValues, 0, pX, pY, pZ);
	}

	public void rotate(final float pAngle, final float pX, final float pY, final float pZ) {
		Matrix.setRotateM(this.mTemp, 0, pAngle, pX, pY, pZ);
		System.arraycopy(this.mValues, 0, this.mTemp, GLMatrix.GLMATRIX_SIZE, GLMatrix.GLMATRIX_SIZE);
		Matrix.multiplyMM(this.mValues, 0, this.mTemp, GLMatrix.GLMATRIX_SIZE, this.mTemp, 0);
	}

	public void scale(final float pScaleX, final float pScaleY, final float pScaleZ) {
		Matrix.scaleM(this.mValues, 0, pScaleX, pScaleY, pScaleZ);
	}

	public void ortho(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		Matrix.orthoM(this.mValues, 0, pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
