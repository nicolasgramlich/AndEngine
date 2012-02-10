package org.andengine.util.algorithm.hull;

import org.andengine.util.math.MathConstants;
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
		final int firstHullVertexIndex = HullUtils.indexOfLowestVertex(pVertices, pVertexCount, pVertexOffsetY, pStride);
		final float firstHullVertexX = HullUtils.getVertex(pVertices, pVertexOffsetX, pStride, firstHullVertexIndex);
		final float firstHullVertexY = HullUtils.getVertex(pVertices, pVertexOffsetY, pStride, firstHullVertexIndex);

		int hullVertexCount = 0;
		int currentHullVertexIndex = firstHullVertexIndex;
		float currentHullVertexAngle = 0;
		do {
			HullUtils.swap(pVertices, pStride, hullVertexCount, currentHullVertexIndex);

			{
				final float currentHullPointVertexX = HullUtils.getVertex(pVertices, pVertexOffsetX, pStride, hullVertexCount);
				final float currentHullPointVertexY = HullUtils.getVertex(pVertices, pVertexOffsetY, pStride, hullVertexCount);

				hullVertexCount++;

				/* Compute the angle between the current hull vertex and all remaining vertices.
				 * Pick the smallest angle larger then the current angle. */
				int nextHullVertexIndex = 0;
				float nextHullVertexAngle = MathConstants.PI_TWICE; /* 360 degrees. */

				/* Start searching one behind the already found hull vertices. */
				for(int j = hullVertexCount; j < pVertexCount; j++) {
					final float possibleNextHullVertexX = HullUtils.getVertex(pVertices, pVertexOffsetX, pStride, j);
					final float possibleNextHullVertexY = HullUtils.getVertex(pVertices, pVertexOffsetY, pStride, j);

					final float dX = possibleNextHullVertexX - currentHullPointVertexX;
					final float dY = possibleNextHullVertexY - currentHullPointVertexY;

					float possibleNextHullVertexAngle = MathUtils.atan2(dY, dX);
					if(possibleNextHullVertexAngle < 0) {
						possibleNextHullVertexAngle += MathConstants.PI_TWICE; /* 360 degrees. */
					}
					if((possibleNextHullVertexAngle >= currentHullVertexAngle) && (possibleNextHullVertexAngle <= nextHullVertexAngle)) {
						nextHullVertexIndex = j;
						nextHullVertexAngle = possibleNextHullVertexAngle;
					}
				}

				/* Compare against first hull vertex. */
				if(hullVertexCount > 1) {
					final float dX = firstHullVertexX - currentHullPointVertexX;
					final float dY = firstHullVertexY - currentHullPointVertexY;

					float possibleNextHullVertexAngle = MathUtils.radToDeg(MathUtils.atan2(dY, dX));
					if(possibleNextHullVertexAngle < 0) {
						possibleNextHullVertexAngle += MathConstants.PI_TWICE; /* 360 degrees. */
					}
					if((possibleNextHullVertexAngle >= currentHullVertexAngle) && (possibleNextHullVertexAngle <= nextHullVertexAngle)) {
						break;
					}
				}

				currentHullVertexAngle = nextHullVertexAngle;
				currentHullVertexIndex = nextHullVertexIndex;
			}
		} while(currentHullVertexIndex > 0);

		return hullVertexCount;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
