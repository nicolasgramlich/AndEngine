package org.andengine.opengl.util;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:18:30 - 10.02.2012
 */
public class VertexUtils {
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

	/**
	 * @param pVertices
	 * @param pVertexOffset
	 * @param pVertexStride
	 * @param pVertexIndex
	 * @return the value of the <code>pVertexOffset</code>-th attribute of the <code>pVertexIndex</code>-th vertex. 
	 */
	public static float getVertex(final float[] pVertices, final int pVertexOffset, final int pVertexStride, final int pVertexIndex) {
		return pVertices[(pVertexIndex * pVertexStride) + pVertexOffset];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
