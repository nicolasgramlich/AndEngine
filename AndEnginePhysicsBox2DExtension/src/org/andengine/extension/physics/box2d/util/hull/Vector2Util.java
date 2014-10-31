package org.andengine.extension.physics.box2d.util.hull;

import com.badlogic.gdx.math.Vector2;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:05:33 - 14.09.2010
 */
class Vector2Util {
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

	public static boolean isLess(final Vector2 pVertexA, final Vector2 pVertexB) {
		final float f = pVertexA.cross(pVertexB);
		return f > 0 || f == 0 && Vector2Util.isLonger(pVertexA, pVertexB);
	}

	public static boolean isLonger(final Vector2 pVertexA, final Vector2 pVertexB) {
		return pVertexA.lenManhattan() > pVertexB.lenManhattan();
	}

	public static float getManhattanDistance(final Vector2 pVertexA, final Vector2 pVertexB) {
		return Math.abs(pVertexA.x - pVertexB.x) + Math.abs(pVertexA.y - pVertexB.y);
	}

	public static boolean isConvex(final Vector2 pVertexTest, final Vector2 pVertexA, final Vector2 pVertexB) {
		final float f = Vector2Util.area2(pVertexTest, pVertexA, pVertexB);
		return f < 0 || f == 0 && !Vector2Util.isBetween(pVertexTest, pVertexA, pVertexB);
	}

	public static float area2(final Vector2 pVertexTest, final Vector2 pVertexA, final Vector2 pVertexB) {
		return (pVertexA.x - pVertexTest.x) * (pVertexB.y - pVertexTest.y) - (pVertexB.x - pVertexTest.x) * (pVertexA.y - pVertexTest.y);
	}

	public static float area2(final Vector2 pVertexTest, final Vector2Line pLine) {
		return Vector2Util.area2(pVertexTest, pLine.mVertexA, pLine.mVertexB);
	}

	public static boolean isBetween(final Vector2 pVertexTest, final Vector2 pVertexA, final Vector2 pVertexB) {
		return Vector2Util.getManhattanDistance(pVertexA, pVertexB) >= Vector2Util.getManhattanDistance(pVertexTest, pVertexA) + Vector2Util.getManhattanDistance(pVertexTest, pVertexB);
	}

	public static boolean isRightOf(final Vector2 pVertexTest, final Vector2Line pLine) {
		return Vector2Util.area2(pVertexTest, pLine) < 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
