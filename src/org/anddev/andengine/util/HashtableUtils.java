package org.anddev.andengine.util;

import java.util.Hashtable;

/**
 * 
 */
public class HashtableUtils {
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
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public static int getIntValueOrThrow(final Hashtable<String, String> pHashtable, final String pKey) {
		final String value = pHashtable.containsKey(pKey) ? pHashtable.get(pKey) : null;
		if(value != null) {
			return Integer.parseInt(value);
		} else {
			throw new IllegalArgumentException("No value found for attribute: " + pKey);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
