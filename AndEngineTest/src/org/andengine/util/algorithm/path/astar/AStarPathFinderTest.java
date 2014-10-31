package org.andengine.util.algorithm.path.astar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.util.algorithm.path.ICostFunction;
import org.andengine.util.algorithm.path.IPathFinderMap;
import org.andengine.util.algorithm.path.Path;
import org.andengine.util.algorithm.path.astar.AStarPathFinder.Node;

/**
 * @author Nicolas Gramlich
 * @since 10:34:13 - 17.08.2010
 */
public class AStarPathFinderTest extends TestCase {
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
	// Test-Methods
	// ===========================================================

	public void testSimplePath() throws Exception {
		final Path path = AStarPathFinderTest.getPath(new String[] {
				"S  ",
				"   ",
				"  F",
		});
		Assert.assertEquals(5, path.getLength());
	}

	public void testCalculateNodeID() {
		this.testCalculateNodeID(0xFFFFFFFFFFFFFFFFL, 0xFFFFFFFF, 0xFFFFFFFF);
		this.testCalculateNodeID(0x8000000080000000L, 0x80000000, 0x80000000);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static Path getPath(final String[] pMap) {
		return AStarPathFinderTest.getPath(pMap, false);
	}

	private static Path getPath(final String[] pMap, final boolean pAllowDiagonal) {
		return AStarPathFinderTest.getPath(pMap, pAllowDiagonal,
				new EuclideanHeuristic<Object>(),
				new ICostFunction<Object>() {
			@Override
			public float getCost(final IPathFinderMap<Object> pPathFinderMap, final int pFromX, final int pFromY, final int pToX, final int pToY, final Object pEntity) {
				return 1;
			}
		});
	}

	private static Path getPath(final String[] pMap, final boolean pAllowDiagonal, final IAStarHeuristic<Object> pAStarHeuristic, final ICostFunction<Object> pCostFunction) {
		final int fromX = AStarPathFinderTest.findColumnOfFirstOccurrence(pMap, 'S');
		final int fromY = AStarPathFinderTest.findRowOfFirstOccurrence(pMap, 'S');
		final int toX = AStarPathFinderTest.findColumnOfFirstOccurrence(pMap, 'F');
		final int toY = AStarPathFinderTest.findRowOfFirstOccurrence(pMap, 'F');

		final int minX = 0;
		final int minY = 0;
		final int maxX = pMap[0].length() - 1;
		final int maxY = pMap.length - 1;

		final Path path = new AStarPathFinder<Object>().findPath(new SimplePathFinderMap(pMap),
				minX, minY, maxX, maxY,
				new Object(),
				fromX, fromY, toX, toY,
				pAllowDiagonal,
				pAStarHeuristic,
				pCostFunction);
		return path;
	}

	private static int findRowOfFirstOccurrence(final String[] pMap, final char pCharacter) {
		for(int y = 0; y < pMap.length; y++) {
			final char[] row = pMap[y].toCharArray();
			for(int x = 0; x < row.length; x++) {
				if(row[x] == pCharacter) {
					return y;
				}
			}
		}
		throw new IllegalArgumentException();
	}

	private static int findColumnOfFirstOccurrence(final String[] pMap, final char pCharacter) {
		for(int y = 0; y < pMap.length; y++) {
			final char[] row = pMap[y].toCharArray();
			for(int x = 0; x < row.length; x++) {
				if(row[x] == pCharacter) {
					return x;
				}
			}
		}
		throw new IllegalArgumentException();
	}

	private void testCalculateNodeID(final long pExpected, final int pX, final int pY) {
		final long actual = Node.calculateID(pX, pY);
		if(pExpected != actual) {
			Assert.fail("Expected: " + Long.toBinaryString(pExpected) + " Actual: " + Long.toBinaryString(actual));
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class SimplePathFinderMap implements IPathFinderMap<Object> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final String[] mMap;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SimplePathFinderMap(final String[] pMap) {
			this.mMap = pMap;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public boolean isBlocked(final int pX, final int pY, final Object pEntity) {
			return this.mMap[pY].charAt(pX) == '#';
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
