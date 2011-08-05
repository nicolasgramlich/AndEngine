package org.anddev.andengine.opengl.util;

import java.util.Stack;

import org.anddev.andengine.util.pool.GenericPool;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:38:21 - 04.08.2011
 */
public class GLMatrixStack {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final GenericPool<GLMatrix> mGLMatrixPool = new GenericPool<GLMatrix>() {
		@Override
		protected GLMatrix onAllocatePoolItem() {
			return new GLMatrix();
		}
	};

	private final Stack<GLMatrix> mModelViewGLMatrixStack = new Stack<GLMatrix>();
	private final Stack<GLMatrix> mProjectionGLMatrixStack = new Stack<GLMatrix>();

	private MatrixMode mMatrixMode;
	private Stack<GLMatrix> mCurrentGLMatrixStack = this.mModelViewGLMatrixStack;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLMatrixStack() {
		this.mModelViewGLMatrixStack.push(this.mGLMatrixPool.obtainPoolItem().setToIdentity());
		this.mProjectionGLMatrixStack.push(this.mGLMatrixPool.obtainPoolItem().setToIdentity());
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
		while(this.mModelViewGLMatrixStack.size() > 1) {
			this.popMatrix();
		}
		this.mModelViewGLMatrixStack.peek().setToIdentity();
		
		this.setMatrixMode(MatrixMode.MODELVIEW);
		while(this.mProjectionGLMatrixStack.size() > 1) {
			this.popMatrix();
		}
		this.mProjectionGLMatrixStack.peek().setToIdentity();
	}

	public void pushMatrix() {
		this.mCurrentGLMatrixStack.push(this.mGLMatrixPool.obtainPoolItem().setTo(this.mCurrentGLMatrixStack.peek()));
	}

	public void popMatrix() {
		this.mGLMatrixPool.recyclePoolItem(this.mCurrentGLMatrixStack.pop());
	}

	public void loadIdentity() {
		this.mCurrentGLMatrixStack.peek().setToIdentity();
	}

	public void loadMatrix(final GLMatrix pGLMatrix) {
		this.mCurrentGLMatrixStack.peek().setTo(pGLMatrix);
	}

	public void translate(final float pX, final float pY, final float pZ) {
		this.mCurrentGLMatrixStack.peek().translate(pX, pY, pZ);
	}

	public void rotate(final float pAngle, final float pX, final float pY, final float pZ) {
		this.mCurrentGLMatrixStack.peek().rotate(pAngle, pX, pY, pZ);
	}

	public void scale(float pScaleX, float pScaleY, int pScaleZ) {
		this.mCurrentGLMatrixStack.peek().scale(pScaleX, pScaleY, pScaleZ);
	}

	public void ortho(final float pLeft, final float pRight, final float pBottom, final float pTop, final float pZNear, final float pZFar) {
		this.mCurrentGLMatrixStack.peek().ortho(pLeft, pRight, pBottom, pTop, pZNear, pZFar);
	}

	public GLMatrix getModelViewMatrix() {
		return this.mModelViewGLMatrixStack.peek();
	}

	public GLMatrix getProjectionMatrix() {
		return this.mProjectionGLMatrixStack.peek();
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
