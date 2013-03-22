package org.andengine.util.algorithm.collision;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.entity.primitive.Line;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:27:22 - 17.07.2010
 */
public final class LineCollisionChecker {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int LINE_VERTEX_COUNT = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private LineCollisionChecker() {

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

	public static boolean checkLineCollision(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		return ((BaseCollisionChecker.relativeCCW(pX1, pY1, pX2, pY2, pX3, pY3) * BaseCollisionChecker.relativeCCW(pX1, pY1, pX2, pY2, pX4, pY4) <= 0)
				&& (BaseCollisionChecker.relativeCCW(pX3, pY3, pX4, pY4, pX1, pY1) * BaseCollisionChecker.relativeCCW(pX3, pY3, pX4, pY4, pX2, pY2) <= 0));
	}

	public static void fillVertices(final Line pLine, final float[] pVertices) {
		pVertices[0 + VERTEX_INDEX_X] = 0;
		pVertices[0 + VERTEX_INDEX_Y] = 0;

		pVertices[2 + VERTEX_INDEX_X] = pLine.getX2() - pLine.getX1();
		pVertices[2 + VERTEX_INDEX_Y] = pLine.getY2() - pLine.getY1();

		pLine.getLocalToSceneTransformation().transform(pVertices);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
