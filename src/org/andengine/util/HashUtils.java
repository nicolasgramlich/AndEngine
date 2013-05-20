package org.andengine.util;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 00:02:12 - 23.03.2013
 */
public final class HashUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private HashUtils() {

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

	public static int getHashCode(final byte ... pBytes) {
		final int prime = 31;
		int result = 1;

		for (int i = 0; i < pBytes.length; i++) {
			result = prime * result + pBytes[i];
		}

		return result;
	}

	public static int getHashCode(final Object ... pObjects) {
		final int prime = 31;
		int result = 1;

		for (int i = 0; i < pObjects.length; i++) {
			final Object object = pObjects[i];
			result = prime * result + ((object == null) ? 0 : object.hashCode());
		}

		return result;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
