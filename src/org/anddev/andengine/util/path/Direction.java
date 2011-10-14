package org.anddev.andengine.util.path;

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

	UP(0, -1),
	DOWN(0, 1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP_LEFT(-1, -1),
	UP_RIGHT(1, -1),
	DOWN_LEFT(-1, 1),
	DOWN_RIGHT(1, 1);

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
		switch(pDeltaX) {
			case -1:
				switch(pDeltaY) {
					case -1:
						return UP_LEFT;
					case 0:
						return LEFT;
					case 1:
						return DOWN_LEFT;
				}
				break;
			case 0:
				switch(pDeltaY) {
					case -1:
						return UP;
					case 1:
						return DOWN;
				}
				break;
			case 1:
				switch(pDeltaY) {
					case -1:
						return UP_RIGHT;
					case 0:
						return RIGHT;
					case 1:
						return DOWN_RIGHT;
				}
				break;
		}
		throw new IllegalArgumentException();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getDeltaX() {
		return this.mDeltaX;
	}

	public int getDeltaY() {
		return this.mDeltaY;
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
