package org.andengine.entity.primitive;

import android.opengl.GLES20;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:46:51 - 28.03.2012
 */
public enum DrawMode {
	// ===========================================================
	// Elements
	// ===========================================================

	POINTS(GLES20.GL_POINTS),
	LINE_STRIP(GLES20.GL_LINE_STRIP),
	LINE_LOOP(GLES20.GL_LINE_LOOP),
	LINES(GLES20.GL_LINES),
	TRIANGLE_STRIP(GLES20.GL_TRIANGLE_STRIP),
	TRIANGLE_FAN(GLES20.GL_TRIANGLE_FAN),
	TRIANGLES(GLES20.GL_TRIANGLES);

	// ===========================================================
	// Constants
	// ===========================================================

	public final int mDrawMode;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private DrawMode(final int pDrawMode) {
		this.mDrawMode = pDrawMode;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getDrawMode() {
		return this.mDrawMode;
	}

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