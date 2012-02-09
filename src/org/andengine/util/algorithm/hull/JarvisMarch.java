package org.andengine.util.algorithm.hull;

import org.andengine.util.math.MathUtils;



/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:01:18 - 14.09.2010
 * @see http://www.iti.fh-flensburg.de/lang/algorithmen/geo/
 */
public class JarvisMarch implements IHullAlgorithm {
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

	@Override
	public int computeHull(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pStride) {
		return this.jarvisMarch(pVertices, pVertexCount, pVertexOffsetX, pVertexOffsetY, pStride);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int jarvisMarch(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pStride) {
		int currentHullVertexIndex = HullUtils.indexOfLowestVertex(pVertices, pVertexCount, pVertexOffsetY, pStride);
		int hullVertexCount = 0;
		float currentHullVertexAngle = 0;
		do {
			HullUtils.swap(pVertices, pStride, hullVertexCount, currentHullVertexIndex);

			{
				final float currentHullPointVertexX = HullUtils.getVertex(pVertices, pVertexOffsetX, pStride, hullVertexCount);
				final float currentHullPointVertexY = HullUtils.getVertex(pVertices, pVertexOffsetY, pStride, hullVertexCount);

				/* Compute the angle between the current hull vertex and all remaining vertices. 
				 * Pick the smallest angle larger then the current angle. */
				int nextHullVertexIndex = 0;
				float nextHullVertexAngle = MathUtils.PI_TWICE; /* 360 degrees. */
				
				/* Start searching one behind the already found hull vertices. */
				for(int j = hullVertexCount + 1; j < pVertexCount; j++) {
					final float possibleNextHullVertexX = HullUtils.getVertex(pVertices, pVertexOffsetX, pStride, j);
					final float possibleNextHullVertexY = HullUtils.getVertex(pVertices, pVertexOffsetY, pStride, j);

					final float dX = possibleNextHullVertexX - currentHullPointVertexX;
					final float dY = possibleNextHullVertexY - currentHullPointVertexY;
					
					float possibleNextHullVertexAngle = MathUtils.atan2(dY, dX);
					if(possibleNextHullVertexAngle < 0) {
						possibleNextHullVertexAngle += MathUtils.PI_TWICE; /* 360 degrees. */
					}
					if(possibleNextHullVertexAngle > currentHullVertexAngle && possibleNextHullVertexAngle < nextHullVertexAngle) {
						nextHullVertexIndex = j;
						nextHullVertexAngle = possibleNextHullVertexAngle;
					}
				}

				currentHullVertexAngle = nextHullVertexAngle;
				currentHullVertexIndex = nextHullVertexIndex;
			}
			hullVertexCount++;
		} while(currentHullVertexIndex > 0);

		return hullVertexCount;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
