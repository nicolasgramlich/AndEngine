package org.andengine.extension.physics.box2d.util.hull;

import com.badlogic.gdx.math.Vector2;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:00:50 - 14.09.2010
 * @see http://www.iti.fh-flensburg.de/lang/algorithmen/geo/
 */
public class GrahamScan extends BaseHullAlgorithm {
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
	public int computeHull(final Vector2[] pVertices) {
		this.mVertices = pVertices;
		this.mVertexCount = pVertices.length;
		if(this.mVertexCount < 3) {
			return this.mVertexCount;
		}
		this.mHullVertexCount = 0;
		this.grahamScan();
		return this.mHullVertexCount;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void grahamScan() {
		this.swap(0, this.indexOfLowestVertex());
		final Vector2 pl = new Vector2(this.mVertices[0]);
		this.makeAllVerticesRelativeTo(pl);
		this.sort();
		this.makeAllVerticesRelativeTo(new Vector2(pl).mul(-1));
		int i = 3;
		int k = 3;
		while(k < this.mVertexCount) {
			this.swap(i, k);
			while(!this.isConvex(i - 1)) {
				this.swap(i - 1, i--);
			}
			k++;
			i++;
		}
		this.mHullVertexCount = i;
	}

	private void makeAllVerticesRelativeTo(final Vector2 pVector) {
		final Vector2[] vertices = this.mVertices;
		final int vertexCount = this.mVertexCount;
		
		final Vector2 vertexCopy = new Vector2(pVector); // necessary, as pVector might be in mVertices[]
		for(int i = 0; i < vertexCount; i++) {
			vertices[i].sub(vertexCopy);
		}
	}

	private boolean isConvex(final int pIndex) {
		final Vector2[] vertices = this.mVertices;
		return Vector2Util.isConvex(vertices[pIndex], vertices[pIndex - 1], vertices[pIndex + 1]);
	}

	private void sort() {
		this.quicksort(1, this.mVertexCount - 1); // without Vertex 0
	}

	private void quicksort(final int pFromIndex, final int pToIndex) {
		final Vector2[] vertices = this.mVertices;
		int i = pFromIndex;
		int j = pToIndex;
		
		final Vector2 q = vertices[(pFromIndex + pToIndex) / 2];
		while(i <= j) {
			while(Vector2Util.isLess(vertices[i], q)) {
				i++;
			}
			while(Vector2Util.isLess(q, vertices[j])) {
				j--;
			}
			if(i <= j) {
				this.swap(i++, j--);
			}
		}
		if(pFromIndex < j) {
			this.quicksort(pFromIndex, j);
		}
		if(i < pToIndex) {
			this.quicksort(i, pToIndex);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
