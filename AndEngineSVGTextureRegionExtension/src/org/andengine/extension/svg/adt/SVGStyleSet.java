package org.andengine.extension.svg.adt;

import java.util.HashMap;

/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:49:43 - 21.05.2011
 */
public class SVGStyleSet {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final HashMap<String, String> mStyleMap = new HashMap<String, String>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGStyleSet(final String pString) {
		final String[] styles = pString.split(";");
		for (final String s : styles) {
			final String[] style = s.split(":");
			if (style.length == 2) {
				this.mStyleMap.put(style[0], style[1]);
			}
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getStyle(final String pStyleName) {
		return this.mStyleMap.get(pStyleName);
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