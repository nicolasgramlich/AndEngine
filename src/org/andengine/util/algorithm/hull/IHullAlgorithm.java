package org.andengine.util.algorithm.hull;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:46:22 - 14.09.2010
 */
public interface IHullAlgorithm {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pVertices a interleaved float[] containing vertex data.
	 * @param pVertexCount the amount of vertices to look at in pVertices.
	 * @param pVertexOffsetX the offset of the x-coordinate in a vertex.
	 * @param pVertexOffsetY the offset of the y-coordinate in a vertex.
	 * @param pStride the stride between each vertex.
	 * @return
	 */
	public int computeHull(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pStride);
}
