package org.anddev.andengine.util;

import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 22:02:09 - 21.07.2010
 */
public class SAXUtils {
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

	public static int getIntAttribute(final Attributes pAttributes, final String pAttributeName, final int pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Integer.parseInt(value) : pDefaultValue;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
