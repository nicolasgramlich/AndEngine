package org.anddev.andengine.util.spatial.quadtree;

import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.FloatBounds;
import org.anddev.andengine.util.spatial.adt.bounds.source.IFloatBoundsSource;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:15:21 - 10.10.2011
 */
public class FloatQuadTree<T extends ISpatialItem<IFloatBoundsSource>> extends QuadTree<IFloatBoundsSource, FloatBounds, T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final FloatBounds mQueryFloatBounds = new FloatBounds(0, 0, 0, 0);

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloatQuadTree(final FloatBounds pFloatBounds) {
		super(pFloatBounds);
	}

	public FloatQuadTree(final FloatBounds pFloatBounds, final int pMaxLevel) {
		super(pFloatBounds, pMaxLevel);
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

	public synchronized List<T> query(final float pX, final float pY) {
		return this.query(pX, pY, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pX, final float pY, final List<T> pResult) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pResult);
	}
	
	public synchronized List<T> query(final float pX, final float pY, final IMatcher<T> pMatcher) {
		return this.query(pX, pY, pMatcher, new LinkedList<T>());
	}
	
	public synchronized List<T> query(final float pX, final float pY, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized List<T> query(final float pLeft, final float pRight, final float pTop, final float pBottom) {
		return this.query(pLeft, pRight, pTop, pBottom, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pLeft, final float pRight, final float pTop, final float pBottom, final List<T> pResult) {
		this.mQueryFloatBounds.set(pLeft, pRight, pTop, pBottom);
		return this.query(this.mQueryFloatBounds, pResult);
	}
	
	public synchronized List<T> query(final float pLeft, final float pRight, final float pTop, final float pBottom, final IMatcher<T> pMatcher) {
		return this.query(pLeft, pRight, pTop, pBottom, pMatcher, new LinkedList<T>());
	}
	
	public synchronized List<T> query(final float pLeft, final float pRight, final float pTop, final float pBottom, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryFloatBounds.set(pLeft, pRight, pTop, pBottom);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
