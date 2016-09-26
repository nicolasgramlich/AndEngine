package org.anddev.andengine.util.path.floyd_warshall;

/**
 * exception for detected negative cycle which can not be handled
 * 
 * @author <a href="https://github.com/winniehell/">winniehell</a>
 */
public class NegativeCycleException extends Exception {

	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final long serialVersionUID = 8988380191330186349L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public NegativeCycleException() {
		super("Algorithm can not handle negative cycles!");
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
