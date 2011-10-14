package org.anddev.andengine.util.path.astar;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.anddev.andengine.util.path.ICostFunction;
import org.anddev.andengine.util.path.IPathFinder;
import org.anddev.andengine.util.path.IPathFinderMap;
import org.anddev.andengine.util.path.Path;
import org.anddev.andengine.util.spatial.adt.bounds.IIntBounds;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:16:17 - 16.08.2010
 */
public class AStarPathFinder<T> implements IPathFinder<T>, IIntBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Set<Node> mVisitedNodes = new HashSet<Node>();
	private final PriorityQueue<Node> mOpenNodes = new PriorityQueue<Node>();

	private final int mMaxSearchDepth;

	private final Node[][] mNodes;
	private final boolean mAllowDiagonal;

	private final IAStarHeuristic<T> mAStarHeuristic;
	private int mXMin;
	private int mXMax;
	private int mYMin;
	private int mYMax;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AStarPathFinder(final int pXMin, final int pYMin, final int pXMax, final int pYMax, final int pMaxSearchDepth, final boolean pAllowDiagonal) {
		this(pXMin, pYMin, pXMax, pYMax, pMaxSearchDepth, pAllowDiagonal, new EuclideanHeuristic<T>());
	}

	public AStarPathFinder(final int pXMin, final int pYMin, final int pXMax, final int pYMax, final int pMaxSearchDepth, final boolean pAllowDiagonal, final IAStarHeuristic<T> pAStarHeuristic) {
		this.mAStarHeuristic = pAStarHeuristic;
		this.mMaxSearchDepth = pMaxSearchDepth;
		this.mAllowDiagonal = pAllowDiagonal;

		this.mXMin = pXMin;
		this.mXMax = pXMax;
		this.mYMin = pYMin;
		this.mYMax = pYMax;

		final int width = this.mXMax - this.mXMin + 1;
		final int height = this.mYMax - this.mYMin + 1;

		this.mNodes = new Node[height][width];
		final Node[][] nodes = this.mNodes;
		for(int y = this.mYMin; y <= this.mYMax; y++) {
			for(int x = this.mXMin; x <= this.mXMax; x++) {
				nodes[y - this.mYMin][x - this.mXMin] = new Node(x, y);
			}
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getXMin() {
		return this.mXMin;
	}

	@Override
	public int getYMin() {
		return this.mYMin;
	}

	@Override
	public int getXMax() {
		return this.mXMax;
	}

	@Override
	public int getYMax() {
		return this.mYMax;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public synchronized Path findPath(final int pFromX, final int pFromY, final int pToX, final int pToY, final IPathFinderMap<T> pPathFinderMap, final T pEntity, final ICostFunction<T> pCostFunction, final float pMaxCost, final IPathFinderListener<T> pPathFinderListener) {
		if(pPathFinderMap.isBlocked(pToX, pToY, pEntity)) {
			return null;
		}
		
		this.mVisitedNodes.clear();
		this.mOpenNodes.clear();

		final int xMin = this.mXMin;
		final int yMin = this.mYMin;

		/* Drag some fields to local variables. */
		final Node fromNode = this.mNodes[pFromY - yMin][pFromX - xMin];
		final Node toNode = this.mNodes[pToY - yMin][pToX - xMin];

		final IAStarHeuristic<T> aStarHeuristic = this.mAStarHeuristic;
		final boolean allowDiagonalMovement = this.mAllowDiagonal;
		final int maxSearchDepth = this.mMaxSearchDepth;

		/* Initialize algorithm. */
		fromNode.mCost = 0;
		fromNode.mDepth = 0;
		toNode.mParent = null;

		this.mOpenNodes.add(fromNode);

		int currentDepth = 0;
		while(currentDepth < maxSearchDepth && !this.mOpenNodes.isEmpty()) {
			/* The first Node in the open list is the one with the lowest cost. */
			final Node current = this.mOpenNodes.poll();
			if(current == toNode) {
				break;
			}

			this.mVisitedNodes.add(current);

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

					if(!this.isBlocked(pFromX, pFromY, neighborX, neighborY, pPathFinderMap, pEntity)) {
						final float neighborCost = current.mCost + pCostFunction.getCost(pPathFinderMap, current.mX, current.mY, neighborX, neighborY, pEntity);
						final Node neighbor = this.mNodes[neighborY - yMin][neighborX - xMin];

						if(pPathFinderListener != null) {
							pPathFinderListener.onVisited(pEntity, neighborX, neighborY);
						}

						/* Re-evaluate if there is a better path. */
						if(neighborCost < neighbor.mCost) {
							// TODO Is this ever possible with AStar and a proper heuristic ???
							if(this.mOpenNodes.contains(neighbor)) {
								this.mOpenNodes.remove(neighbor);
							}
							if(this.mVisitedNodes.contains(neighbor)) {
								this.mVisitedNodes.remove(neighbor);
							}
						}

						if(!this.mOpenNodes.contains(neighbor) && !(this.mVisitedNodes.contains(neighbor))) {
							neighbor.mCost = neighborCost;
							if(neighbor.mCost <= pMaxCost) {
								neighbor.mExpectedRestCost = aStarHeuristic.getExpectedRestCost(pPathFinderMap, pEntity, neighborX, neighborY, pToX, pToY);
								currentDepth = Math.max(currentDepth, neighbor.setParent(current));
								this.mOpenNodes.add(neighbor);
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
		Node tmp = this.mNodes[pToY - yMin][pToX - xMin];
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

	protected boolean isBlocked(final int pFromX, final int pFromY, final int pToX, final int pToY, final IPathFinderMap<T> pPathFinderMap, final T pEntity) {
		if((pToX < this.mXMin) || (pToY < this.mYMin) || (pToX > this.mXMax) || (pToY > this.mYMax)) {
			return true;
		} else if((pFromX == pToX) && (pFromY == pToY)) {
			return true;
		}

		return pPathFinderMap.isBlocked(pToX, pToY, pEntity);
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
