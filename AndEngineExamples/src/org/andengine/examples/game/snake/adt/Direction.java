package org.andengine.examples.game.snake.adt;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 02:29:05 - 08.07.2010
 */
public enum Direction {
	// ===========================================================
	// Elements
	// ===========================================================

	UP, DOWN, LEFT, RIGHT;

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
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static int addToX(final Direction pDirection, final int pX) {
		switch(pDirection) {
			case UP:
			case DOWN:
				return pX;
			case LEFT:
				return pX - 1;
			case RIGHT:
				return pX + 1;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static int addToY(final Direction pDirection, final int pY) {
		switch(pDirection) {
			case LEFT:
			case RIGHT:
				return pY;
			case UP:
				return pY - 1;
			case DOWN:
				return pY + 1;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static Direction opposite(final Direction pDirection) {
		switch(pDirection) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			default:
				return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
