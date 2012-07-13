package org.anddev.andengine.util.path.floyd_warshall;

import java.util.ArrayList;

import org.anddev.andengine.util.path.IStaticPathFinder;
import org.anddev.andengine.util.path.IStaticTiledMap;
import org.anddev.andengine.util.path.WeightedPath;

/**
 * implementation of the Floyd-Warshall-algorithm for shortest paths
 * 
 * @author <a href="https://github.com/winniehell/">winniehell</a>
 */
public class FloydWarshallPathFinder<T> implements IStaticPathFinder<T> {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final PathMatrix mMatrix;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloydWarshallPathFinder()
	{
		mMatrix = new PathMatrix();
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public WeightedPath findPath(float pMaxCost,
	                             int pFromTileColumn, int pFromTileRow,
	                             int pToTileColumn, int pToTileRow) {

		final Node fromNode = mMatrix.get(pFromTileColumn, pFromTileRow,
		                                  pFromTileColumn, pFromTileRow);
		
		final Node toNode = mMatrix.get(pFromTileColumn, pFromTileRow,
		                                pToTileColumn, pToTileRow);
		
		// tile is blocked
		if((toNode.mParent == null) || (toNode.mCost > pMaxCost)) {
			return null;
		}
		
		WeightedPath path = new WeightedPath(toNode.mCost);
		
		int maxStepsLeft = mMatrix.getColumns()*mMatrix.getRows();
		Node currentNode = toNode;
		
		while(currentNode != fromNode) {
			if((currentNode.mParent == null) || (maxStepsLeft == 0)) {
				// data structure broken
				return null;
			}
			
			path.append(currentNode.mColumn, currentNode.mRow);
			
			currentNode = currentNode.mParent;
			--maxStepsLeft;
		}
		
		path.append(fromNode.mColumn, fromNode.mRow);
			
		return path;
	}

	@Override
	public synchronized void initialize(IStaticTiledMap pMap)
	            throws NegativeCycleException {
		final int columns = pMap.getTileColumns();
		final int rows = pMap.getTileRows();
		
		mMatrix.ensureSize(columns, rows);
		
		// initialize path cost with step cost
		for(int fromCol = 0; fromCol < columns; ++fromCol) {
			for(int fromRow = 0; fromRow < rows; ++fromRow) {
				for(int toCol = 0; toCol < columns; ++toCol) {
					for(int toRow = 0; toRow < rows; ++toRow) {
						Node currentNode = mMatrix.get(fromCol, fromRow, toCol, toRow);
						
						if((fromCol < toCol - 1)
						   || (fromCol > toCol + 1)
						   || (fromRow < toRow - 1)
						   || (fromRow > toRow + 1)
						   || pMap.isTileBlocked(fromCol, fromRow)
						   || pMap.isTileBlocked(toCol, toRow)) {
							
							// no a neighbor tile
							currentNode.reset();
						}
						else {
							currentNode.mParent = mMatrix.get(fromCol, fromRow, fromCol, fromRow);
							currentNode.mCost = pMap.getStepCost(fromCol, fromRow, toCol, toRow);
						}
					}
				}
			}
		}
		
		// for each intermediate tile ...
		for(int intermCol = 0; intermCol < columns; ++intermCol) {
			for(int intermRow = 0; intermRow < rows; ++intermRow) {
				
				// ... which is not blocked ...
				if(pMap.isTileBlocked(intermCol, intermRow)) {
					continue;
				}
				
				// ... and from each tile ...
				for(int fromCol = 0; fromCol < columns; ++fromCol) {
					for(int fromRow = 0; fromRow < rows; ++fromRow) {

						// ... which is not blocked ...
						if(pMap.isTileBlocked(fromCol, fromRow)) {
							continue;
						}
						
						// ... besides intermediate tile ...
						if((fromCol == intermCol) && (fromRow == intermRow)) {
							continue;
						}
						
						final Node intermNode = mMatrix.get(fromCol, fromRow, intermCol, intermRow);
						
						// ... to every tile ...
						for(int toCol = 0; toCol < columns; ++toCol) {
							for(int toRow = 0; toRow < rows; ++toRow) {

								// ... which is not blocked ...
								if(pMap.isTileBlocked(toCol, toRow)) {
									continue;
								}
								
								// ... besides intermediate tile ...
								if((toCol == intermCol) && (toRow == intermRow)) {
									continue;
								}
								
								// ... update shortest path information
								final Node currentNode = mMatrix.get(fromCol, fromRow, toCol, toRow);

								float partialCost1 = 
									mMatrix.get(fromCol, fromRow,
											intermCol, intermRow).mCost;
								float partialCost2 = 
									mMatrix.get(intermCol, intermRow,
									            toCol, toRow).mCost;
								
								if(currentNode.mCost > partialCost1 + partialCost2) {
									// negative cycle detection
									if((fromCol == toCol) && (fromRow == toRow)) {
										throw new NegativeCycleException();
									}
									else {
										currentNode.mCost = partialCost1 + partialCost2;
										currentNode.mParent = intermNode;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class Node {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		/**
		 * @name position
		 * @{
		 */
		public final int mColumn;
		public final int mRow;
		/**
		 * @}
		 */
		
		public Node mParent = null;
		public float mCost = Float.MAX_VALUE;

		// ===========================================================
		// Constructors
		// ===========================================================
		
		public Node(final int pColumn, final int pRow) {
			mColumn = pColumn;
			mRow = pRow;
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
		
		public void reset()
		{
			mParent = null;
			mCost = Float.MAX_VALUE;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
	
	private static class NodeMatrix
	{
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		
		private final ArrayList<ArrayList<Node>> mMatrix;

		// ===========================================================
		// Constructors
		// ===========================================================
		
		public NodeMatrix() {
			mMatrix = new ArrayList<ArrayList<Node>>();
			mMatrix.add(new ArrayList<Node>());
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================
		
		/** @return {@link Node} at given position in matrix  */
		public Node get(final int pColumn, final int pRow) {
			synchronized (mMatrix) {
				if(pColumn < mMatrix.size())
				{
					final ArrayList<Node> column = mMatrix.get(pColumn); 
					
					if(pRow < column.size()) {
						return column.get(pRow);
					}
					else {
						throw new IndexOutOfBoundsException();
					}
				}
				else {
					throw new IndexOutOfBoundsException();
				}
			}
		}
		
		/** @return number of columns */
		public int getColumns() {
			synchronized (mMatrix) {
				return mMatrix.size();
			}
		}

		/** @return number of rows */
		public int getRows() {
			synchronized (mMatrix) {
				if(getColumns() > 0) {
					return mMatrix.get(0).size();
				}
				else {
					return 0;
				}
			}
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		
		/**
		 * resize the matrix if necessary to have at least the given number
		 * of columns and rows
		 */
		public void ensureSize(final int pColumns, final int pRows) {
			synchronized (mMatrix) {
				
				int rows = getRows();
				
				while(rows < pRows) {
					for(int column = 0; column < getColumns(); ++column) {
						mMatrix.get(column).add(new Node(column, rows));
					}
					
					++rows;
				}
				
				assert (getRows() >= pRows);
				
				int columns = getColumns();
				
				while(columns < pColumns) {
					final ArrayList<Node> column = new ArrayList<Node>();
					
					for(int row = 0; row < rows; ++rows) {
						column.add(new Node(columns, row));
					}
					
					mMatrix.add(column);
					++columns;
				}
				
				assert (getColumns() >= pColumns);
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
	
	private static class PathMatrix
	{
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		
		private final ArrayList<ArrayList<NodeMatrix>> mMatrix;

		// ===========================================================
		// Constructors
		// ===========================================================
		
		public PathMatrix() {
			mMatrix = new ArrayList<ArrayList<NodeMatrix>>();
			mMatrix.add(new ArrayList<NodeMatrix>());
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================
		
		/** @return {@link Node} at given position in matrix  */
		public Node get(final int pFromColumn, final int pFromRow,
		                final int pToColumn, final int pToRow) {
			synchronized (mMatrix) {
				return get(pFromColumn, pFromRow).get(pToColumn, pToRow);
			}
		}
		
		/** @return number of columns */
		public int getColumns() {
			synchronized (mMatrix) {
				return mMatrix.size();
			}
		}

		/** @return number of rows */
		public int getRows() {
			synchronized (mMatrix) {
				if(getColumns() > 0) {
					return mMatrix.get(0).size();
				}
				else {
					return 0;
				}
			}
		}
		
		/** @return submatrix at given position */
		private NodeMatrix get(final int pFromColumn, final int pFromRow) {
			synchronized (mMatrix) {
				if(pFromColumn < getColumns())
				{
					final ArrayList<NodeMatrix> column = 
						mMatrix.get(pFromColumn); 
					
					if(pFromRow < getRows()) {
						return column.get(pFromRow);
					}
					else {
						throw new IndexOutOfBoundsException();
					}
				}
				else {
					throw new IndexOutOfBoundsException();
				}
			}
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		
		/**
		 * resize the matrix if necessary to have at least the given number
		 * of columns and rows
		 */
		public void ensureSize(final int pColumns, final int pRows) {
			synchronized (mMatrix) {
				while(getRows() < pRows) {
					for(int column = 0; column < getColumns(); ++column) {
						mMatrix.get(column).add(new NodeMatrix());
					}
				}
				
				while(getColumns() < pColumns) {
					final ArrayList<NodeMatrix> column =
						new ArrayList<NodeMatrix>();
					
					while(column.size() < getRows()) {
						column.add(new NodeMatrix());
					}
					
					mMatrix.add(column);
				}
				
				// resize sub matrices
				for(int column = 0; column < getColumns(); ++column) {
					for(int row = 0; row < getRows(); ++row) {
						get(column, row).ensureSize(pColumns, pRows);
					}
				}
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
