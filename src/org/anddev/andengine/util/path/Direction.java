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

	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

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
		if(pDeltaX == 0) {
			if(pDeltaY == 1) {
				return DOWN;
			} else if(pDeltaY == -1) {
				return UP;
			}
		} else if (pDeltaY == 0) {
			if(pDeltaX == 1) {
				return RIGHT;
			} else if(pDeltaX == -1) {
				return LEFT;
			}
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
