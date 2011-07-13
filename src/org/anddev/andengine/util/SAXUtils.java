package org.anddev.andengine.util;

import org.xml.sax.Attributes;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public static String getAttribute(final Attributes pAttributes, final String pAttributeName, final String pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? value : pDefaultValue;
	}

	public static String getAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		final String value = pAttributes.getValue("", pAttributeName);
		if(value != null) {
			return value;
		} else {
			throw new IllegalArgumentException("No value found for attribute: '" + pAttributeName + "'");
		}
	}

	public static boolean getBooleanAttribute(final Attributes pAttributes, final String pAttributeName, final boolean pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Boolean.parseBoolean(value) : pDefaultValue;
	}

	public static boolean getBooleanAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Boolean.parseBoolean(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}

	public static byte getByteAttribute(final Attributes pAttributes, final String pAttributeName, final byte pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Byte.parseByte(value) : pDefaultValue;
	}

	public static byte getByteAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Byte.parseByte(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}


	public static short getShortAttribute(final Attributes pAttributes, final String pAttributeName, final short pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Short.parseShort(value) : pDefaultValue;
	}

	public static short getShortAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Short.parseShort(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}


	public static int getIntAttribute(final Attributes pAttributes, final String pAttributeName, final int pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Integer.parseInt(value) : pDefaultValue;
	}

	public static int getIntAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Integer.parseInt(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}


	public static long getLongAttribute(final Attributes pAttributes, final String pAttributeName, final long pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Long.parseLong(value) : pDefaultValue;
	}

	public static long getLongAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Long.parseLong(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}


	public static float getFloatAttribute(final Attributes pAttributes, final String pAttributeName, final float pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Float.parseFloat(value) : pDefaultValue;
	}

	public static float getFloatAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Float.parseFloat(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}


	public static double getDoubleAttribute(final Attributes pAttributes, final String pAttributeName, final double pDefaultValue) {
		final String value = pAttributes.getValue("", pAttributeName);
		return (value != null) ? Double.parseDouble(value) : pDefaultValue;
	}

	public static double getDoubleAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		return Double.parseDouble(SAXUtils.getAttributeOrThrow(pAttributes, pAttributeName));
	}


	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final boolean pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final byte pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final short pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final int pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final long pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final float pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final double pValue) {
		SAXUtils.appendAttribute(pStringBuilder, pName, String.valueOf(pValue));
	}

	public static void appendAttribute(final StringBuilder pStringBuilder, final String pName, final String pValue) {
		pStringBuilder.append(' ').append(pName).append('=').append('\"').append(pValue).append('\"');
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
