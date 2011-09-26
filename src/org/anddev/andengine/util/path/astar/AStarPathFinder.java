package org.anddev.andengine.util.path.astar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.path.ICostFunction;
import org.anddev.andengine.util.path.IPathFinder;
import org.anddev.andengine.util.path.IPathFinderMap;
import org.anddev.andengine.util.path.Path;

/**
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

	private final List<Node> mVisitedNodes = new LinkedList<Node>();
	private final List<Node> mOpenNodes = new LinkedList<Node>();

	private final IPathFinderMap<T> mPathFinderMap;
	private final int mMaxSearchDepth;

	private final Node[][] mNodes;
	private final boolean mAllowDiagonal;

	private final IAStarHeuristic<T> mAStarHeuristic;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AStarPathFinder(final IPathFinderMap<T> pPathFinderMap, final int pMaxSearchDepth, final boolean pAllowDiagonal) {
		this(pPathFinderMap, pMaxSearchDepth, pAllowDiagonal, new EuclideanHeuristic<T>());
	}

	public AStarPathFinder(final IPathFinderMap<T> pPathFinderMap, final int pMaxSearchDepth, final boolean pAllowDiagonal, final IAStarHeuristic<T> pAStarHeuristic) {
		this.mAStarHeuristic = pAStarHeuristic;
		this.mPathFinderMap = pPathFinderMap;
		this.mMaxSearchDepth = pMaxSearchDepth;
		this.mAllowDiagonal = pAllowDiagonal;

		final int pathFinderMapXMin = pPathFinderMap.getXMin();
		final int pathFinderMapXMax = pPathFinderMap.getXMax();
		final int pathFinderMapYMin = pPathFinderMap.getYMin();
		final int pathFinderMapYMax = pPathFinderMap.getYMax();

		this.mNodes = new Node[pPathFinderMap.getHeight()][pPathFinderMap.getWidth()];
		final Node[][] nodes = this.mNodes;
		for(int y = pathFinderMapYMin; y <= pathFinderMapYMax; y++) {
			for(int x = pathFinderMapXMin; x <= pathFinderMapXMax; x++) {
				nodes[y - pathFinderMapYMin][x - pathFinderMapXMin] = new Node(x, y);
			}
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public synchronized Path findPath(final T pEntity, final ICostFunction<T> pCostFunction, final float pMaxCost, final int pFromX, final int pFromY, final int pToX, final int pToY, final IPathFinderListener<T> pPathFinderListener) {
		final IPathFinderMap<T> pathFinderMap = this.mPathFinderMap;
		if(pathFinderMap.isBlocked(pToX, pToY, pEntity)) {
			return null;
		}

		final int pathFinderMapXMin = pathFinderMap.getXMin();
		final int pathFinderMapYMin = pathFinderMap.getYMin();

		/* Drag some fields to local variables. */
		final List<Node> openNodes = this.mOpenNodes;
		final List<Node> visitedNodes = this.mVisitedNodes;

		final Node[][] nodes = this.mNodes;
		final Node fromNode = nodes[pFromY - pathFinderMapYMin][pFromX - pathFinderMapXMin];
		final Node toNode = nodes[pToY - pathFinderMapYMin][pToX - pathFinderMapXMin];

		final IAStarHeuristic<T> aStarHeuristic = this.mAStarHeuristic;
		final boolean allowDiagonalMovement = this.mAllowDiagonal;
		final int maxSearchDepth = this.mMaxSearchDepth;

		/* Initialize algorithm. */
		fromNode.mCost = 0;
		fromNode.mDepth = 0;
		toNode.mParent = null;

		visitedNodes.clear();

		openNodes.clear();
		openNodes.add(fromNode);

		int currentDepth = 0;
		while(currentDepth < maxSearchDepth && !openNodes.isEmpty()) {
			/* The first Node in the open list is the one with the lowest cost. */
			final Node current = openNodes.remove(0);
			if(current == toNode) {
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

					if(!this.isBlocked(pEntity, pFromX, pFromY, neighborX, neighborY)) {
						final float neighborCost = current.mCost + pCostFunction.getCost(pathFinderMap, current.mX, current.mY, neighborX, neighborY, pEntity);
						final Node neighbor = nodes[neighborY - pathFinderMapYMin][neighborX - pathFinderMapXMin];

						if(pPathFinderListener != null) {
							pPathFinderListener.onVisited(pEntity, neighborX, neighborY);
						}

						/* Re-evaluate if there is a better path. */
						if(neighborCost < neighbor.mCost) {
							// TODO Is this ever possible with AStar ??
							if(openNodes.contains(neighbor)) {
								openNodes.remove(neighbor);
							}
							if(visitedNodes.contains(neighbor)) {
								visitedNodes.remove(neighbor);
							}
						}

						if(!openNodes.contains(neighbor) && !(visitedNodes.contains(neighbor))) {
							neighbor.mCost = neighborCost;
							if(neighbor.mCost <= pMaxCost) {
								neighbor.mExpectedRestCost = aStarHeuristic.getExpectedRestCost(pathFinderMap, pEntity, neighborX, neighborY, pToX, pToY);
								currentDepth = Math.max(currentDepth, neighbor.setParent(current));
								openNodes.add(neighbor);

								/* Ensure always the node with the lowest cost+heuristic
								 * will be used next, simply by sorting. */
								Collections.sort(openNodes);
							}
						}
					}
				}
			}
		}

		/* Check if a path was found. */
		if(toNode.mParent == null) {
			return null;
		}

		/* Traceback path. */
		final Path path = new Path();
		Node tmp = nodes[pToY - pathFinderMapYMin][pToX - pathFinderMapXMin];
		while(tmp != fromNode) {
			path.prepend(tmp.mX, tmp.mY);
			tmp = tmp.mParent;
		}
		path.prepend(pFromX, pFromY);

		return path;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected boolean isBlocked(final T pEntity, final int pFromX, final int pFromY, final int pToX, final int pToY) {
		final IPathFinderMap<T> pathFinderMap = this.mPathFinderMap;
		if((pToX < pathFinderMap.getXMin()) || (pToY < pathFinderMap.getYMin()) || (pToX > pathFinderMap.getXMax()) || (pToY > pathFinderMap.getYMax())) {
			return true;
		} else if((pFromX == pToX) && (pFromY == pToY)) {
			return true;
		}

		return pathFinderMap.isBlocked(pToX, pToY, pEntity);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class Node implements Comparable<Node> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		Node mParent;
		int mDepth;

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

		public int setParent(final Node parent) {
			this.mDepth = parent.mDepth + 1;
			this.mParent = parent;

			return this.mDepth;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public int compareTo(final Node pOther) {
			final float totalCost = this.mExpectedRestCost + this.mCost;
			final float totalCostOther = pOther.mExpectedRestCost + pOther.mCost;

			if (totalCost < totalCostOther) {
				return -1;
			} else if (totalCost > totalCostOther) {
				return 1;
			} else {
				return 0;
			}
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
