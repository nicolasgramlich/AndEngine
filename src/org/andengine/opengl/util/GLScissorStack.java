package org.andengine.opengl.util;

import org.andengine.util.exception.AndEngineRuntimeException;

import android.opengl.GLES20;

/**
 * TODO Measure performance against regular Java Stack<E>
 *
 * (c) 2013 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Michal Stawinski <michal.stawinski@gmail.com>
 * @since 14:49:23 - 02.05.2013
 */
public class GLScissorStack {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int GLSCISSOR_X_POS = 0;
	public static final int GLSCISSOR_Y_POS = 1;
	public static final int GLSCISSOR_WIDTH_POS = 2;
	public static final int GLSCISSOR_HEIGHT_POS = 3;

	public static final int GLSCISSORSTACK_DEPTH_MAX = 32;
	public static final int GLSCISSOR_SIZE = 4;

	private static final int GLSCISSORSTACKOFFSET_UNDERFLOW = -1 * GLScissorStack.GLSCISSOR_SIZE;
	private static final int GLSCISSORSTACKOFFSET_OVERFLOW = GLScissorStack.GLSCISSORSTACK_DEPTH_MAX * GLScissorStack.GLSCISSOR_SIZE;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int[] mTemp = new int[GLScissorStack.GLSCISSOR_SIZE];
	private final int[] mScissorStack = new int[GLScissorStack.GLSCISSORSTACK_DEPTH_MAX * GLScissorStack.GLSCISSOR_SIZE];
	private int mScissorStackOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLScissorStack() {
	}

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int[] getScissor(final int[] pScissors) {
		assertNoUnderflow();

		System.arraycopy(this.mScissorStack, this.mScissorStackOffset, pScissors, 0, GLScissorStack.GLSCISSOR_SIZE);
		return pScissors;
	}

	public int[] getScissorTmp() {
		getScissor(this.mTemp);
		return this.mTemp;
	}

	protected int getStackDepth() {
		return mScissorStackOffset / GLSCISSOR_SIZE;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void glScissors(final int[] pScissors) {
		GLES20.glScissor(pScissors[GLSCISSOR_X_POS], pScissors[GLSCISSOR_Y_POS], pScissors[GLSCISSOR_WIDTH_POS], pScissors[GLSCISSOR_HEIGHT_POS]);
	}

	public void glScissors(final int pX, final int pY, final int pWidth, final int pHeight) {
		GLES20.glScissor(pX, pY, pWidth, pHeight);
	}

	public void glScissorPush(final int[] pScissors) {
		if (getStackDepth() == 0) {
			glScissors(pScissors);
		} else {
			/* take a union between old and new clip rect */
			getScissor(mTemp);
			final int x1 = Math.max(pScissors[GLSCISSOR_X_POS], mTemp[GLSCISSOR_X_POS]);
			final int y1 = Math.max(pScissors[GLSCISSOR_Y_POS], mTemp[GLSCISSOR_Y_POS]);
			final int x2 = Math.min(pScissors[GLSCISSOR_X_POS] + pScissors[GLSCISSOR_WIDTH_POS], mTemp[GLSCISSOR_X_POS] + mTemp[GLSCISSOR_WIDTH_POS]);
			final int y2 = Math.min(pScissors[GLSCISSOR_Y_POS] + pScissors[GLSCISSOR_HEIGHT_POS], mTemp[GLSCISSOR_Y_POS] + mTemp[GLSCISSOR_HEIGHT_POS]);

			final int w = Math.max(0, x2 - x1);
			final int h = Math.max(0, y2 - y1);
			glScissors(x1, y1, w, h);
		}
		pushScissor(pScissors);
	}

	public void glScissorPush(final int pX, final int pY, final int pWidth, final int pHeight) {
		if (getStackDepth() == 0) {
			glScissors(pX, pY, pWidth, pHeight);
		} else {
			/* take a union between old and new clip rect */
			getScissor(mTemp);
			final int x1 = Math.max(pX, mTemp[GLSCISSOR_X_POS]);
			final int y1 = Math.max(pY, mTemp[GLSCISSOR_Y_POS]);
			final int x2 = Math.min(pX + pWidth, mTemp[GLSCISSOR_X_POS] + mTemp[GLSCISSOR_WIDTH_POS]);
			final int y2 = Math.min(pY + pHeight, mTemp[GLSCISSOR_Y_POS] + mTemp[GLSCISSOR_HEIGHT_POS]);

			final int w = Math.max(0, x2 - x1);
			final int h = Math.max(0, y2 - y1);
			glScissors(x1, y1, w, h);
		}
		pushScissor(pX, pY, pWidth, pHeight);
	}

	public void glScissorPop() {
		popScissor();
		if (getStackDepth() > 0) {
			getScissor(this.mTemp);
			glScissors(this.mTemp);
		}
	}

	protected void pushScissor(final int pX, final int pY, final int pWidth, final int pHeight) throws GLScissorStackOverflowException {
		this.mTemp[GLSCISSOR_X_POS] = pX;
		this.mTemp[GLSCISSOR_Y_POS] = pY;
		this.mTemp[GLSCISSOR_WIDTH_POS] = pWidth;
		this.mTemp[GLSCISSOR_HEIGHT_POS] = pHeight;

		pushScissor(this.mTemp);
	}

	protected void pushScissor(final int[] pScissors) throws GLScissorStackOverflowException {
		assertNoOverflow();

		System.arraycopy(pScissors, 0, this.mScissorStack, this.mScissorStackOffset + GLScissorStack.GLSCISSOR_SIZE, GLScissorStack.GLSCISSOR_SIZE);
		this.mScissorStackOffset += GLScissorStack.GLSCISSOR_SIZE;
	}

	protected void popScissor() {
		assertNoUnderflow();

		this.mScissorStackOffset -= GLScissorStack.GLSCISSOR_SIZE;
	}

	private void assertNoUnderflow() {
		if (this.mScissorStackOffset - GLScissorStack.GLSCISSOR_SIZE <= GLScissorStack.GLSCISSORSTACKOFFSET_UNDERFLOW) {
			throw new GLScissorStackUnderflowException();
		}
	}

	private void assertNoOverflow() {
		if (this.mScissorStackOffset + GLScissorStack.GLSCISSOR_SIZE >= GLScissorStack.GLSCISSORSTACKOFFSET_OVERFLOW) {
			throw new GLScissorStackOverflowException();
		}
	}

	public void reset() {
		this.mScissorStackOffset = 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class GLScissorStackOverflowException extends AndEngineRuntimeException {

		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 8253962794945142016L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public GLScissorStackOverflowException() {

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

	public static class GLScissorStackUnderflowException extends AndEngineRuntimeException {

		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = -2962655760038324091L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public GLScissorStackUnderflowException() {

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
