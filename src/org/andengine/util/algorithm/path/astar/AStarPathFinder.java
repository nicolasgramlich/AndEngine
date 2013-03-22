package org.andengine.util.algorithm.path.astar;

import org.andengine.util.adt.list.ShiftList;
import org.andengine.util.adt.map.LongSparseArray;
import org.andengine.util.adt.queue.IQueue;
import org.andengine.util.adt.queue.SortedQueue;
import org.andengine.util.adt.spatial.bounds.util.IntBoundsUtils;
import org.andengine.util.algorithm.path.ICostFunction;
import org.andengine.util.algorithm.path.IPathFinder;
import org.andengine.util.algorithm.path.IPathFinderMap;
import org.andengine.util.algorithm.path.Path;

/**
 * TODO Nodes could be recycle in a pool.
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @author Greg Haynes
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
		if (((pFromX == pToX) && (pFromY == pToY)) || pPathFinderMap.isBlocked(pFromX, pFromY, pEntity) || pPathFinderMap.isBlocked(pToX, pToY, pEntity)) {
			return null;
		}

		/* Drag some fields to local variables. */
		final Node fromNode = new Node(pFromX, pFromY, pAStarHeuristic.getExpectedRestCost(pPathFinderMap, pEntity, pFromX, pFromY, pToX, pToY));

		final long fromNodeID = fromNode.mID;
		final long toNodeID = Node.calculateID(pToX, pToY);

		final LongSparseArray<Node> visitedNodes = new LongSparseArray<Node>();
		final LongSparseArray<Node> openNodes = new LongSparseArray<Node>();
		final IQueue<Node> sortedOpenNodes = new SortedQueue<Node>(new ShiftList<Node>());

		final boolean allowDiagonalMovement = pAllowDiagonal;

		/* Initialize algorithm. */
		openNodes.put(fromNodeID, fromNode);
		sortedOpenNodes.enter(fromNode);

		Node currentNode = null;
		while (openNodes.size() > 0) {
			/* The first Node in the open list is the one with the lowest cost. */
			currentNode = sortedOpenNodes.poll();
			final long currentNodeID = currentNode.mID;
			if (currentNodeID == toNodeID) {
				break;
			}

			visitedNodes.put(currentNodeID, currentNode);

			/* Loop over all neighbors of this position. */
			for (int dX = -1; dX <= 1; dX++) {
				for (int dY = -1; dY <= 1; dY++) {
					if ((dX == 0) && (dY == 0)) {
						continue;
					}

					if (!allowDiagonalMovement && (dX != 0) && (dY != 0)) {
						continue;
					}

					final int neighborNodeX = dX + currentNode.mX;
					final int neighborNodeY = dY + currentNode.mY;
					final long neighborNodeID = Node.calculateID(neighborNodeX, neighborNodeY);

					if (!IntBoundsUtils.contains(pXMin, pYMin, pXMax, pYMax, neighborNodeX, neighborNodeY) || pPathFinderMap.isBlocked(neighborNodeX, neighborNodeY, pEntity)) {
						continue;
					}

					if (visitedNodes.indexOfKey(neighborNodeID) >= 0) {
						continue;
					}

					Node neighborNode = openNodes.get(neighborNodeID);
					final boolean neighborNodeIsNew;
					/* Check if neighbor exists. */
					if (neighborNode == null) {
						neighborNodeIsNew = true;
						neighborNode = new Node(neighborNodeX, neighborNodeY, pAStarHeuristic.getExpectedRestCost(pPathFinderMap, pEntity, neighborNodeX, neighborNodeY, pToX, pToY));
					} else {
						neighborNodeIsNew = false;
					}

					/* Update cost of neighbor as cost of current plus step from current to neighbor. */
					final float costFromCurrentToNeigbor = pCostFunction.getCost(pPathFinderMap, currentNode.mX, currentNode.mY, neighborNodeX, neighborNodeY, pEntity);
					final float neighborNodeCost = currentNode.mCost + costFromCurrentToNeigbor;
					if (neighborNodeCost > pMaxCost) {
						/* Too expensive -> remove if isn't a new node. */
						if (!neighborNodeIsNew) {
							openNodes.remove(neighborNodeID);
						}
					} else {
						neighborNode.setParent(currentNode, costFromCurrentToNeigbor);
						if (neighborNodeIsNew) {
							openNodes.put(neighborNodeID, neighborNode);
						} else {
							/* Remove so that re-insertion puts it to the correct spot. */
							sortedOpenNodes.remove(neighborNode);
						}

						sortedOpenNodes.enter(neighborNode);

						if (pPathFinderListener != null) {
							pPathFinderListener.onVisited(pEntity, neighborNodeX, neighborNodeY);
						}
					}
				}
			}
		}

		/* Cleanup. */
		// TODO We could just let the GC do its work.
		visitedNodes.clear();
		openNodes.clear();
		sortedOpenNodes.clear();

		/* Check if a path was found. */
		if (currentNode.mID != toNodeID) {
			return null;
		}

		/* Calculate path length. */
		int length = 1;
		Node tmp = currentNode;
		while (tmp.mID != fromNodeID) {
			tmp = tmp.mParent;
			length++;
		}

		/* Traceback path. */
		final Path path = new Path(length);
		int index = length - 1;
		tmp = currentNode;
		while (tmp.mID != fromNodeID) {
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

	/* package */ static final class Node implements Comparable<Node> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		/* package */ Node mParent;

		/* package */ final int mX;
		/* package */ final int mY;
		/* package */ final long mID;
		/* package */ final float mExpectedRestCost;

		/* package */ float mCost;
		/* package */ float mTotalCost;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Node(final int pX, final int pY, final float pExpectedRestCost) {
			this.mX = pX;
			this.mY = pY;
			this.mExpectedRestCost = pExpectedRestCost;

			this.mID = Node.calculateID(pX, pY);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setParent(final Node pParent, final float pCost) {
			this.mParent = pParent;
			this.mCost = pCost;
			this.mTotalCost = pCost + this.mExpectedRestCost;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public int compareTo(final Node pNode) {
			final float diff = this.mTotalCost - pNode.mTotalCost;
			if (diff > 0) {
				return 1;
			} else if (diff < 0) {
				return -1;
			} else {
				return 0;
			}
		}

		@Override
		public boolean equals(final Object pOther) {
			if (this == pOther) {
				return true;
			} else if (pOther == null) {
				return false;
			} else if (this.getClass() != pOther.getClass()) {
				return false;
			}
			return this.equals((Node) pOther);
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + " [x=" + this.mX + ", y=" + this.mY + "]";
		}

		// ===========================================================
		// Methods
		// ===========================================================

		public static long calculateID(final int pX, final int pY) {
			return (((long) pX) << 32) | (((long) pY) & 0xFFFFFFFFL);
		}

		public boolean equals(final Node pNode) {
			return this.mID == pNode.mID;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}