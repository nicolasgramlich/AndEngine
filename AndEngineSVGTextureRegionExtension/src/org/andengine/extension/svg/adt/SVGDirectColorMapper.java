package org.andengine.extension.svg.adt;

import android.util.SparseIntArray;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 09:21:33 - 25.05.2011
 */
public class SVGDirectColorMapper implements ISVGColorMapper {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SparseIntArray mColorMappings = new SparseIntArray();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGDirectColorMapper() {

	}

	public SVGDirectColorMapper(final Integer pColorFrom, final Integer pColorTo) {
		this.addColorMapping(pColorFrom, pColorTo);
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

	public void addColorMapping(final Integer pColorFrom, final Integer pColorTo) {
		this.mColorMappings.put(pColorFrom, pColorTo);
	}

	@Override
	public Integer mapColor(final Integer pColor) {
		final Integer mappedColor = this.mColorMappings.get(pColor);
		if(mappedColor == null) {
			return pColor;
		} else {
			return mappedColor;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
