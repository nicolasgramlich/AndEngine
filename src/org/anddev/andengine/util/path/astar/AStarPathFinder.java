package org.anddev.andengine.util.path.astar;

import java.util.ArrayList;
import java.util.Collections;

import org.anddev.andengine.util.path.IPathFinder;
import org.anddev.andengine.util.path.ITiledMap;
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

	private final ArrayList<Node> mVisitedNodes = new ArrayList<Node>();
	private final ArrayList<Node> mOpenNodes = new ArrayList<Node>();

	private final ITiledMap<T> mTiledMap;
	private final int mMaxSearchDepth;

	private final Node[][] mNodes;
	private final boolean mAllowDiagonalMovement;

	private final IAStarHeuristic<T> mAStarHeuristic;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AStarPathFinder(final ITiledMap<T> pTiledMap, final int pMaxSearchDepth, final boolean pAllowDiagonalMovement) {
		this(pTiledMap, pMaxSearchDepth, pAllowDiagonalMovement, new EuclideanHeuristic<T>());
	}

	public AStarPathFinder(final ITiledMap<T> pTiledMap, final int pMaxSearchDepth, final boolean pAllowDiagonalMovement, final IAStarHeuristic<T> pAStarHeuristic) {
		this.mAStarHeuristic = pAStarHeuristic;
		this.mTiledMap = pTiledMap;
		this.mMaxSearchDepth = pMaxSearchDepth;
		this.mAllowDiagonalMovement = pAllowDiagonalMovement;

		this.mNodes = new Node[pTiledMap.getTileRows()][pTiledMap.getTileColumns()];
		final Node[][] nodes = this.mNodes;
		for(int x = pTiledMap.getTileColumns() - 1; x >= 0; x--) {
			for(int y = pTiledMap.getTileRows() - 1; y >= 0; y--) {
				nodes[y][x] = new Node(x, y);
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
	public Path findPath(final T pEntity, final int pMaxCost, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow) {
		final ITiledMap<T> tiledMap = this.mTiledMap;
		if(tiledMap.isTileBlocked(pEntity, pToTileColumn, pToTileRow)) {
			return null;
		}

		/* Drag some fields to local variables. */
		final ArrayList<Node> openNodes = this.mOpenNodes;
		final ArrayList<Node> visitedNodes = this.mVisitedNodes;

		final Node[][] nodes = this.mNodes;
		final Node fromNode = nodes[pFromTileRow][pFromTileColumn];
		final Node toNode = nodes[pToTileRow][pToTileColumn];

		final IAStarHeuristic<T> aStarHeuristic = this.mAStarHeuristic;
		final boolean allowDiagonalMovement = this.mAllowDiagonalMovement;
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

			/* Loop over all neighbors of this tile. */
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

					final int neighborTileColumn = dX + current.mTileColumn;
					final int neighborTileRow = dY + current.mTileRow;

					if(!this.isTileBlocked(pEntity, pFromTileColumn, pFromTileRow, neighborTileColumn, neighborTileRow)) {
						final float neighborCost = current.mCost + tiledMap.getStepCost(pEntity, current.mTileColumn, current.mTileRow, neighborTileColumn, neighborTileRow);
						final Node neighbor = nodes[neighborTileRow][neighborTileColumn];
						tiledMap.onTileVisitedByPathFinder(neighborTileColumn, neighborTileRow);

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
								neighbor.mExpectedRestCost = aStarHeuristic.getExpectedRestCost(tiledMap, pEntity, neighborTileColumn, neighborTileRow, pToTileColumn, pToTileRow);
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
		Node tmp = nodes[pToTileRow][pToTileColumn];
		while(tmp != fromNode) {
			path.prepend(tmp.mTileColumn, tmp.mTileRow);
			tmp = tmp.mParent;
		}
		path.prepend(pFromTileColumn, pFromTileRow);

		return path;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected boolean isTileBlocked(final T pEntity, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow) {
		if((pToTileColumn < 0) || (pToTileRow < 0) || (pToTileColumn >= this.mTiledMap.getTileColumns()) || (pToTileRow >= this.mTiledMap.getTileRows())) {
			return true;
		} else if((pFromTileColumn == pToTileColumn) && (pFromTileRow == pToTileRow)) {
			return true;
		}

		return this.mTiledMap.isTileBlocked(pEntity, pToTileColumn, pToTileRow);
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

		final int mTileColumn;
		final int mTileRow;

		float mCost;
		float mExpectedRestCost;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Node(final int pTileColumn, final int pTileRow) {
			this.mTileColumn = pTileColumn;
			this.mTileRow = pTileRow;
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
