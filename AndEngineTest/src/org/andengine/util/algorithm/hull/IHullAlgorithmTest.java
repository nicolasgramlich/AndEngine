package org.andengine.util.algorithm.hull;


import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.opengl.util.VertexUtils;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:32:32 - 08.02.2012
 */
public abstract class IHullAlgorithmTest extends TestCase {
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

	protected abstract IHullAlgorithm newHullALgorithm();

	// ===========================================================
	// TestMethods
	// ===========================================================

	public void testSimple() {
		final IHullAlgorithm hullAlgorithm = this.newHullALgorithm();

		final float[] vertices = new float[] {
				0, 1,
				1, 1,
				1, 0,
				0, 0,
				0.5f, 0.5f
		};

		final int hullvertices = hullAlgorithm.computeHull(vertices, 5, 0, 1, 2);

		Assert.assertEquals(4, hullvertices);
	}

	public void testSwappingOutNonHullPoint() {
		final IHullAlgorithm hullAlgorithm = this.newHullALgorithm();
		
		final float[] vertices = new float[] {
				0, 1,
				1, 1,
				1, 0,
				0.5f, 0.5f,
				0, 0,
		};
		
		final int hullvertices = hullAlgorithm.computeHull(vertices, 5, 0, 1, 2);
		
		Assert.assertEquals(4, hullvertices);
		Assert.assertEquals(VertexUtils.getVertex(vertices, 0, 2, 4), 0.5f);
		Assert.assertEquals(VertexUtils.getVertex(vertices, 1, 2, 4), 0.5f);
	}

	public void testDuplicateHullVertices() {
		final IHullAlgorithm hullAlgorithm = this.newHullALgorithm();
		
		final float[] vertices = new float[] {
				0, 1,
				1, 1,
				0, 1,
				1, 1,
				0, 1,
				1, 0,
				1, 1,
				1, 0,
				0.5f, 0.5f,
				0, 0,
		};
		
		final int hullvertices = hullAlgorithm.computeHull(vertices, vertices.length / 2, 0, 1, 2);
		
		Assert.assertEquals(4, hullvertices);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
