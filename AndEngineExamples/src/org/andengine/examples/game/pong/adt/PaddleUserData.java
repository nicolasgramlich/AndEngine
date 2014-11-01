package org.andengine.examples.game.pong.adt;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:14:17 - 01.03.2011
 */
public class PaddleUserData {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mOwnerID;
	private final int mOpponentID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PaddleUserData(final int pOwnerID, final int pOpponentID) {
		this.mOwnerID = pOwnerID;
		this.mOpponentID = pOpponentID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getOwnerID() {
		return this.mOwnerID;
	}

	public int getOpponentID() {
		return this.mOpponentID;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
