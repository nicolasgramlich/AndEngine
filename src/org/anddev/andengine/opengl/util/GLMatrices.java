package org.anddev.andengine.opengl.util;

import android.opengl.Matrix;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:38:21 - 04.08.2011
 */
public class GLMatrices {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private MatrixMode mMatrixMode;

	private final GLMatrixStack mModelViewGLMatrixStack = new GLMatrixStack();
	private final GLMatrixStack mProjectionGLMatrixStack = new GLMatrixStack();
	private GLMatrixStack mCurrentGLMatrixStack = this.mModelViewGLMatrixStack;

	private final float[] mModelViewGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];
	private final float[] mProjectionGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];
	private final float[] mModelViewProjectionGLMatrix = new float[GLMatrixStack.GLMATRIX_SIZE];

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLMatrices() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setMatrixMode(final MatrixMode pMatrixMode) {
		this.mMatrixMode = pMatrixMode;
		switch(this.mMatrixMode) {
			case PROJECTION:
				this.mCurrentGLMatrixStack = this.mProjectionGLMatrixStack;
				return;
			case MODELVIEW:
				this.mCurrentGLMatrixStack = this.mModelViewGLMatrixStack;
				return;
			default:
				throw new IllegalArgumentException("Unexpected " + MatrixMode.class.getSimpleName() + ": '" + pMatrixMode + "'.");
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		this.setMatrixMode(MatrixMode.MODELVIEW);
		this.mModelViewGLMatrixStack.reset();

		this.setMatrixMode(MatrixMode.MODELVIEW);
		this.mProjectionGLMatrixStack.reset();
	}

	public void glPushMatrix() {
		this.mCurrentGLMatrixStack.glPushMatrix();
	}

	public void glPopMatrix() {
		this.mCurrentGLMatrixStack.glPopMatrix();
	}

	public void glLoadIdentity() {
		this.mCurrentGLMatrixStack.glLoadIdentity();
	}

	public void glTranslatef(final float pX, final float pY, final float pZ) {
		this.mCurrentGLMatrixStack.glTranslatef(pX, pY, pZ);
	}

	public void glRotatef(final float pAngle, final float pX, final float pY, final float pZ) {
		this.mCurrentGLMatrixStack.glRotatef(pAngle, pX, pY, pZ);
	}

	public void glScalef(final float pScaleX, final float pScaleY, final int pScaleZ) {
		this.mCurrentGLMatrixStack.glScalef(pScaleX, pScaleY, pScaleZ);
	}

	public void glOrthof(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		this.mCurrentGLMatrixStack.glOrthof(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
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
		Matrix.multiplyMM(this.mModelViewProjectionGLMatrix, 0, this.mProjectionGLMatrixStack.mValues, this.mProjectionGLMatrixStack.mTop, this.mModelViewGLMatrixStack.mValues, this.mModelViewGLMatrixStack.mTop);
		return this.mModelViewProjectionGLMatrix;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public enum MatrixMode {
		// ===========================================================
		// Elements
		// ===========================================================

		PROJECTION,
		MODELVIEW;

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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
