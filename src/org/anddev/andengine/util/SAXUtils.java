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

	public static int getIntAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		final String value = pAttributes.getValue("", pAttributeName);
		if(value != null) {
			return Integer.parseInt(value);
		} else {
			throw new IllegalArgumentException("No value found for attribute: " + pAttributeName);
		}
	}
	
	public static String getAttribute(final Attributes pAttributes, final String pAttributeName, final String pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? value : pDefaultValue;
	}

	public static String getAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		final String value = pAttributes.getValue("", pAttributeName);
		if(value != null) {
			return value;
		} else {
			throw new IllegalArgumentException("No value found for attribute: " + pAttributeName);
		}
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final int pValue) {
		appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final long pValue) {
		appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final String pValue) {
		pStringBuilder.append(' ').append(pName).append('=').append('\"').append(pValue).append('\"');
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
