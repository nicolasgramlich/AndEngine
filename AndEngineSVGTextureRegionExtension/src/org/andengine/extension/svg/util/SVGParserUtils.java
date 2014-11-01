package org.andengine.extension.svg.util;

import org.andengine.extension.svg.util.SVGNumberParser.SVGNumberParserIntegerResult;
import org.andengine.extension.svg.util.constants.ColorUtils;
import org.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;

import android.graphics.Color;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:43:24 - 22.05.2011
 */
public class SVGParserUtils implements ISVGConstants {
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

	public static Float extractFloatAttribute(final String pString) {
		if (pString == null) {
			return null;
		} else {
			try {
				if (pString.endsWith(UNIT_PX)) {
					return Float.parseFloat(pString.substring(0, pString.length() - 2));
				} else {
					return Float.parseFloat(pString);
				}
			} catch (final NumberFormatException nfe) {
				return null;
			}
		}
	}

	public static String extractIDFromURLProperty(final String pProperty) {
		return pProperty.substring("url(#".length(), pProperty.length() - 1);
	}

	public static Integer extractColorFromRGBProperty(final String pProperty) {
		final SVGNumberParserIntegerResult svgNumberParserIntegerResult = SVGNumberParser.parseInts(pProperty.substring("rgb(".length(), pProperty.indexOf(')')));
		if(svgNumberParserIntegerResult.getNumberCount() == 3) {
			return Color.argb(0, svgNumberParserIntegerResult.getNumber(0), svgNumberParserIntegerResult.getNumber(1), svgNumberParserIntegerResult.getNumber(2));
		} else {
			return null;
		}
	}

	public static Integer extraColorIntegerProperty(final String pProperty) {
		return Integer.parseInt(pProperty, 16);
	}

	public static Integer extractColorFromHexProperty(final String pProperty) {
		final String hexColorString = pProperty.substring(1).trim();
		if(hexColorString.length() == 3) {
			final int parsedInt = Integer.parseInt(hexColorString, 16);
			final int red = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_R) >> 8;
			final int green = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_G) >> 4;
			final int blue = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_B) >> 0;
			/* Generate color, duplicating the bits, so that i.e.: #F46 gets #FFAA66. */
			return Color.argb(0, (red << 4) | red, (green << 4) | green, (blue << 4) | blue);
		} else if(hexColorString.length() == 6) {
			return Integer.parseInt(hexColorString, 16);
		} else {
			return null;
		}
	}

	public static String parseHref(final Attributes pAttributes) {
		String href = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_HREF);
		if(href != null) {
			if(href.startsWith("#")) {
				href = href.substring(1);
			}
		}
		return href;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
