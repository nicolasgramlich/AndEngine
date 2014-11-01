package org.andengine.extension.physics.box2d.util.hull;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:01:18 - 14.09.2010
 * @see http://www.iti.fh-flensburg.de/lang/algorithmen/geo/
 */
public class JarvisMarch extends BaseHullAlgorithm {
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
	public int computeHull(final Vector2[] pVectors) {
		this.mVertices = pVectors;
		this.mVertexCount = pVectors.length;
		this.mHullVertexCount = 0;
		this.jarvisMarch();
		return this.mHullVertexCount;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void jarvisMarch() {
		final Vector2[] vertices = this.mVertices;

		int index = this.indexOfLowestVertex();
		do {
			this.swap(this.mHullVertexCount, index);
			index = this.indexOfRightmostVertexOf(vertices[this.mHullVertexCount]);
			this.mHullVertexCount++;
		} while(index > 0);
	}

	private int indexOfRightmostVertexOf(final Vector2 pVector) {
		final Vector2[] vertices = this.mVertices;
		final int vertexCount = this.mVertexCount;

		int i = 0;
		for(int j = 1; j < vertexCount; j++) {
			
			final Vector2 vector2A = Vector2Pool.obtain().set(vertices[j]);
			final Vector2 vector2B = Vector2Pool.obtain().set(vertices[i]);
			if(Vector2Util.isLess(vector2A.sub(pVector), vector2B.sub(pVector))) {
				i = j;
			}
			Vector2Pool.recycle(vector2A);
			Vector2Pool.recycle(vector2B);
		}
		return i;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
