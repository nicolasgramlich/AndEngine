package org.andengine.util.adt.spatial;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:19:11 - 17.08.2010
 */
public enum Direction {
	// ===========================================================
	// Elements
	// ===========================================================

	UP(0, 1),
	DOWN(0, -1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP_LEFT(-1, 1),
	UP_RIGHT(1, 1),
	DOWN_LEFT(-1, -1),
	DOWN_RIGHT(1, -1);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mDeltaX;
	private final int mDeltaY;

	// ===========================================================
	// Constructors
	// ===========================================================

	private Direction(final int pDeltaX, final int pDeltaY) {
		this.mDeltaX = pDeltaX;
		this.mDeltaY = pDeltaY;
	}

	public static Direction fromDelta(final int pDeltaX, final int pDeltaY) {
		switch (pDeltaX) {
			case -1:
				switch (pDeltaY) {
					case 1:
						return UP_LEFT;
					case 0:
						return LEFT;
					case -1:
						return DOWN_LEFT;
				}
				break;
			case 0:
				switch (pDeltaY) {
					case 1:
						return UP;
					case -1:
						return DOWN;
				}
				break;
			case 1:
				switch (pDeltaY) {
					case 1:
						return UP_RIGHT;
					case 0:
						return RIGHT;
					case -1:
						return DOWN_RIGHT;
				}
				break;
		}
		throw new IllegalArgumentException("Unexpected deltaX: '" + pDeltaX + "' deltaY: '" + pDeltaY + "'.");
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isHorizontal() {
		return this.mDeltaY == 0;
	}

	public boolean isVertical() {
		return this.mDeltaX == 0;
	}

	public int getDeltaX() {
		return this.mDeltaX;
	}

	public int getDeltaY() {
		return this.mDeltaY;
	}

	public Direction rotateLeft() {
		switch (this) {
			case UP:
				return LEFT;
			case UP_RIGHT:
				return UP_LEFT;
			case RIGHT:
				return UP;
			case DOWN_RIGHT:
				return UP_RIGHT;
			case DOWN:
				return UP;
			case DOWN_LEFT:
				return DOWN_RIGHT;
			case LEFT:
				return DOWN;
			case UP_LEFT:
				return DOWN_LEFT;
		}
		throw new IllegalArgumentException();
	}

	public Direction rotateRight() {
		switch (this) {
			case UP:
				return RIGHT;
			case UP_RIGHT:
				return DOWN_RIGHT;
			case RIGHT:
				return DOWN;
			case DOWN_RIGHT:
				return DOWN_LEFT;
			case DOWN:
				return LEFT;
			case DOWN_LEFT:
				return UP_LEFT;
			case LEFT:
				return UP;
			case UP_LEFT:
				return UP_RIGHT;
		}
		throw new IllegalArgumentException();
	}

	public Direction opposite() {
		switch (this) {
			case UP:
				return DOWN;
			case UP_RIGHT:
				return DOWN_LEFT;
			case RIGHT:
				return LEFT;
			case DOWN_RIGHT:
				return UP_LEFT;
			case DOWN:
				return UP;
			case DOWN_LEFT:
				return UP_RIGHT;
			case LEFT:
				return RIGHT;
			case UP_LEFT:
				return DOWN_RIGHT;
		}
		throw new IllegalArgumentException();
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
