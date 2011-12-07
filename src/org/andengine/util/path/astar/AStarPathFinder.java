package org.andengine.util.path.astar;

import java.util.HashSet;
import java.util.TreeSet;

import org.andengine.util.path.ICostFunction;
import org.andengine.util.path.IPathFinder;
import org.andengine.util.path.IPathFinderMap;
import org.andengine.util.path.Path;
import org.andengine.util.spatial.adt.bounds.util.IntBoundsUtils;

/**
 * TODO Nodes could be recycle in a pool.
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:16:17 - 16.08.2010
 */
public class AStarPathFinder<T> implements IPathFinder<T> {
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
	public Path findPath(final IPathFinderMap<T> pPathFinderMap, final int pXMin, final int pYMin, final int pXMax, final int pYMax, final T pEntity, final int pFromX, final int pFromY, final int pToX, final int pToY, final boolean pAllowDiagonal, final IAStarHeuristic<T> pAStarHeuristic, final ICostFunction<T> pCostFunction) {
		return this.findPath(pPathFinderMap, pXMin, pYMin, pXMax, pYMax, pEntity, pFromX, pFromY, pToX, pToY, pAllowDiagonal, pAStarHeuristic, pCostFunction, Float.MAX_VALUE);
	}

	@Override
	public Path findPath(final IPathFinderMap<T> pPathFinderMap, final int pXMin, final int pYMin, final int pXMax, final int pYMax, final T pEntity, final int pFromX, final int pFromY, final int pToX, final int pToY, final boolean pAllowDiagonal, final IAStarHeuristic<T> pAStarHeuristic, final ICostFunction<T> pCostFunction, final float pMaxCost) {
		return this.findPath(pPathFinderMap, pXMin, pYMin, pXMax, pYMax, pEntity, pFromX, pFromY, pToX, pToY, pAllowDiagonal, pAStarHeuristic, pCostFunction, pMaxCost, null);
	}

	@Override
	public Path findPath(final IPathFinderMap<T> pPathFinderMap, final int pXMin, final int pYMin, final int pXMax, final int pYMax, final T pEntity, final int pFromX, final int pFromY, final int pToX, final int pToY, final boolean pAllowDiagonal, final IAStarHeuristic<T> pAStarHeuristic, final ICostFunction<T> pCostFunction, final float pMaxCost, final IPathFinderListener<T> pPathFinderListener) {
		if((pFromX == pToX && pFromY == pToY) || pPathFinderMap.isBlocked(pFromX, pFromY, pEntity) || pPathFinderMap.isBlocked(pToX, pToY, pEntity)) {
			return null;
		}
		/* Drag some fields to local variables. */
		final Node fromNode = new Node(pFromX, pFromY);
		final Node toNode = new Node(pToX, pToY);

		final HashSet<Node> visitedNodes = new HashSet<Node>();
		final TreeSet<Node> openNodes = new TreeSet<Node>();

		final IAStarHeuristic<T> aStarHeuristic = pAStarHeuristic;
		final boolean allowDiagonalMovement = pAllowDiagonal;

		/* Initialize algorithm. */
		fromNode.mExpectedRestCost = pAStarHeuristic.getExpectedRestCost(pPathFinderMap, pEntity, pFromX, pFromY, pToX, pToY);

		openNodes.add(fromNode);

		Node current = null;
		while(!openNodes.isEmpty()) {
			/* The first Node in the open list is the one with the lowest cost. */
			current = openNodes.first();
			openNodes.remove(current);
			if(current.equals(toNode)) {
				break;
			}

			visitedNodes.add(current);

			/* Loop over all neighbors of this position. */
			for(int dX = -1; dX <= 1; dX++) {
				for(int dY = -1; dY <= 1; dY++) {
					if((dX == 0) && (dY == 0)) {
						continue;
					}

					if(!allowDiagonalMovement) {
						if((dX != 0) && (dY != 0)) {
							continue;
						}
					}

					final int neighborX = dX + current.mX;
					final int neighborY = dY + current.mY;

					if(!IntBoundsUtils.contains(pXMin, pYMin, pXMax, pYMax, neighborX, neighborY) || pPathFinderMap.isBlocked(neighborX, neighborY, pEntity)) {
						continue;
					}

					final Node neighbor = new Node(neighborX, neighborY);

					if(pPathFinderListener != null) {
						pPathFinderListener.onVisited(pEntity, neighborX, neighborY);
					}

					if(openNodes.contains(neighbor) || visitedNodes.contains(neighbor)) {
						continue;
					}

					/* Update cost of neighbor as cost of current plus step from current to neighbor. */
					final float costFromCurrentToNeigbor = pCostFunction.getCost(pPathFinderMap, current.mX, current.mY, neighborX, neighborY, pEntity);
					neighbor.mCost = current.mCost + costFromCurrentToNeigbor;
					if(neighbor.mCost <= pMaxCost) {
						neighbor.mExpectedRestCost = aStarHeuristic.getExpectedRestCost(pPathFinderMap, pEntity, neighborX, neighborY, pToX, pToY);
						neighbor.setParent(current);
						openNodes.add(neighbor);
					}
				}
			}
		}

		/* Cleanup. */
		visitedNodes.clear();
		openNodes.clear();

		/* Check if a path was found. */
		if(!current.equals(toNode)) {
			return null;
		}

		/* Calculate path length. */
		int length = 1;
		Node tmp = current;
		while(!tmp.equals(fromNode)) {
			tmp = tmp.mParent;
			length++;
		}

		/* Traceback path. */
		final Path path = new Path(length);
		int index = length - 1;
		tmp = current;
		while(!tmp.equals(fromNode)) {
			path.set(index, tmp.mX, tmp.mY);
			tmp = tmp.mParent;
			index--;
		}
		path.set(0, pFromX, pFromY);

		return path;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	static final class Node implements Comparable<Node> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		Node mParent;

		final int mX;
		final int mY;

		float mCost;
		float mExpectedRestCost;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Node(final int pX, final int pY) {
			this.mX = pX;
			this.mY = pY;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setParent(final Node parent) {
			this.mParent = parent;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public int hashCode() {
			return this.mX << 16 | this.mY;
		}

		@Override
		public boolean equals(final Object pOther) {
			if(this == pOther) {
				return true;
			} else if(pOther == null) {
				return false;
			} else if(this.getClass() != pOther.getClass()) {
				return false;
			}
			return this.equals((Node)pOther);
		}

		@Override
		public int compareTo(final Node pOther) {
			if(this.equals(pOther)) {
				return 0;
			}

			final float totalCost = this.mExpectedRestCost + this.mCost;
			final float totalCostOther = pOther.mExpectedRestCost + pOther.mCost;

			if (totalCost < totalCostOther) {
				return -1;
			} else if (totalCost > totalCostOther) {
				return 1;
			} else {
				if(this.mCost < pOther.mCost) {
					return -1;
				} else if(this.mCost > pOther.mCost) {
					return 1;
				} else {
					return 0;
				}
			}
		}

		@Override
		public String toString() {
			return "Node [x=" + this.mX + ", y=" + this.mY + "]";
		}

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean equals(final Node pNode) {
			return this.mX == pNode.mX && this.mY == pNode.mY;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}