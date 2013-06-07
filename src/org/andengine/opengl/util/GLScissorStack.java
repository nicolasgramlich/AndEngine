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

	public static final int GLSCISSOR_X_INDEX = 0;
	public static final int GLSCISSOR_Y_INDEX = 1;
	public static final int GLSCISSOR_WIDTH_INDEX = 2;
	public static final int GLSCISSOR_HEIGHT_INDEX = 3;

	public static final int GLSCISSORSTACK_DEPTH_MAX = 32;
	public static final int GLSCISSOR_SIZE = 4;

	private static final int GLSCISSORSTACKOFFSET_UNDERFLOW = -1 * GLScissorStack.GLSCISSOR_SIZE;
	private static final int GLSCISSORSTACKOFFSET_OVERFLOW = GLScissorStack.GLSCISSORSTACK_DEPTH_MAX * GLScissorStack.GLSCISSOR_SIZE;

	// ===========================================================
	// Fields
	// ===========================================================

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
		if (this.mScissorStackOffset - GLScissorStack.GLSCISSOR_SIZE <= GLScissorStack.GLSCISSORSTACKOFFSET_UNDERFLOW) {
			throw new GLScissorStackUnderflowException();
		}

		System.arraycopy(this.mScissorStack, this.mScissorStackOffset, pScissors, 0, GLScissorStack.GLSCISSOR_SIZE);
		return pScissors;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void glPushScissor(final int pX, final int pY, final int pWidth, final int pHeight) {
		if (this.mScissorStackOffset + GLScissorStack.GLSCISSOR_SIZE >= GLScissorStack.GLSCISSORSTACKOFFSET_OVERFLOW) {
			throw new GLScissorStackOverflowException();
		}

		final int x;
		final int y;
		final int width;
		final int height;
		if (this.mScissorStackOffset == 0) {
			x = pX;
			y = pY;
			width = pWidth;
			height = pHeight;
		} else {
			/* Take the intersection between the current and the new scissor: */
			final int currentX = this.mScissorStack[this.mScissorStackOffset - (GLSCISSOR_SIZE - GLSCISSOR_X_INDEX)];
			final int currentY = this.mScissorStack[this.mScissorStackOffset - (GLSCISSOR_SIZE - GLSCISSOR_Y_INDEX)];
			final int currentWidth = this.mScissorStack[this.mScissorStackOffset - (GLSCISSOR_SIZE - GLSCISSOR_WIDTH_INDEX)];
			final int currentHeight = this.mScissorStack[this.mScissorStackOffset - (GLSCISSOR_SIZE - GLSCISSOR_HEIGHT_INDEX)];

			final float xMin = Math.max(currentX, pX);
			final float xMax = Math.min(currentX + currentWidth, pX + pWidth);

			final float yMin = Math.max(currentY, pY);
			final float yMax = Math.min(currentY + currentHeight, pY + pHeight);

			x = (int) xMin;
			y = (int) yMin;
			width = (int) (xMax - xMin);
			height = (int) (yMax - yMin);
		}

		this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_X_INDEX] = pX;
		this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_Y_INDEX] = pY;
		this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_WIDTH_INDEX] = pWidth;
		this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_HEIGHT_INDEX] = pHeight;

		GLES20.glScissor(x, y, width, height);

		this.mScissorStackOffset += GLScissorStack.GLSCISSOR_SIZE;
	}

	public void glPopScissor() {
		if (this.mScissorStackOffset - GLScissorStack.GLSCISSOR_SIZE <= GLScissorStack.GLSCISSORSTACKOFFSET_UNDERFLOW) {
			throw new GLScissorStackUnderflowException();
		}

		this.mScissorStackOffset -= GLScissorStack.GLSCISSOR_SIZE;

		final int x = this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_X_INDEX];
		final int y = this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_Y_INDEX];
		final int width = this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_WIDTH_INDEX];
		final int height = this.mScissorStack[this.mScissorStackOffset + GLSCISSOR_HEIGHT_INDEX];

		GLES20.glScissor(x, y, width, height);
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
