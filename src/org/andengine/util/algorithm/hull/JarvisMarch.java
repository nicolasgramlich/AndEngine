package org.andengine.util.algorithm.hull;

import org.andengine.opengl.util.VertexUtils;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;



/**
 * The Jarvis March algorithm marches around the hull,
 * like a ribbon wrapping itself around the points,
 * this algorithm also called the <i><b>gift-wrapping</b></i> algorithm.
 * 
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
	public int computeHull(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pVertexStride) {
		return JarvisMarch.jarvisMarch(pVertices, pVertexCount, pVertexOffsetX, pVertexOffsetY, pVertexStride);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static int jarvisMarch(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pVertexStride) {
		/* Start at the lowest (y-axis) of all vertices. */
		final int firstHullVertexIndex = HullUtils.indexOfLowestVertex(pVertices, pVertexCount, pVertexOffsetY, pVertexStride);
		final float firstHullVertexX = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, firstHullVertexIndex);
		final float firstHullVertexY = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, firstHullVertexIndex);

		int hullVertexCount = 0;
		int currentHullVertexIndex = firstHullVertexIndex;
		float currentHullVertexAngle = 0; /* 0 degrees. */
		do {
			HullUtils.swap(pVertices, pVertexStride, hullVertexCount, currentHullVertexIndex);

			final float currentHullPointVertexX = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, hullVertexCount);
			final float currentHullPointVertexY = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, hullVertexCount);

			hullVertexCount++;

			/* Compute the angle between the current hull vertex and all remaining vertices.
			 * Pick the smallest angle larger then the current angle. */
			int nextHullVertexIndex = 0;
			float nextHullVertexAngle = MathConstants.PI_TWICE; /* 360 degrees. */

			/* Start searching one behind the already found hull vertices. */
			for(int j = hullVertexCount; j < pVertexCount; j++) {
				final float possibleNextHullVertexX = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, j);
				final float possibleNextHullVertexY = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, j);

				/* Ignore identical vertices. */
				if(currentHullPointVertexX != possibleNextHullVertexX || currentHullPointVertexY != possibleNextHullVertexY) {
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
			}

			/* Compare against first hull vertex. */
			if(hullVertexCount > 1) {
				final float dX = firstHullVertexX - currentHullPointVertexX;
				final float dY = firstHullVertexY - currentHullPointVertexY;

				float possibleNextHullVertexAngle = MathUtils.atan2(dY, dX);
				if(possibleNextHullVertexAngle < 0) {
					possibleNextHullVertexAngle += MathConstants.PI_TWICE; /* 360 degrees. */
				}
				if((possibleNextHullVertexAngle >= currentHullVertexAngle) && (possibleNextHullVertexAngle <= nextHullVertexAngle)) {
					break;
				}
			}

			currentHullVertexAngle = nextHullVertexAngle;
			currentHullVertexIndex = nextHullVertexIndex;
		} while(currentHullVertexIndex > 0);

		return hullVertexCount;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
