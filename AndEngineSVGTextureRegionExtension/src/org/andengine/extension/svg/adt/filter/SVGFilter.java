package org.andengine.extension.svg.adt.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.extension.svg.adt.SVGAttributes;
import org.andengine.extension.svg.adt.filter.element.ISVGFilterElement;
import org.andengine.extension.svg.exception.SVGParseException;
import org.andengine.extension.svg.util.SVGParserUtils;
import org.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;

import android.graphics.Paint;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:12:03 - 26.05.2011
 */
public class SVGFilter implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mID;
	private final String mHref;
	private SVGFilter mParent;

	private final SVGAttributes mSVGAttributes;

	private final ArrayList<ISVGFilterElement> mSVGFilterElements = new ArrayList<ISVGFilterElement>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGFilter(final String pID, final Attributes pAttributes) {
		this.mID = pID;
		this.mHref = SVGParserUtils.parseHref(pAttributes);
		this.mSVGAttributes = new SVGAttributes(pAttributes, true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getID() {
		return this.mID;
	}

	public String getHref() {
		return this.mHref;
	}

	public boolean hasHref() {
		return this.mHref != null;
	}

	public boolean hasHrefResolved() {
		return this.mHref == null || this.mParent != null;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void ensureHrefResolved(final HashMap<String, SVGFilter> pSVGFilterMap) {
		if(!this.hasHrefResolved()) {
			this.resolveHref(pSVGFilterMap);
		}
	}

	private void resolveHref(final HashMap<String, SVGFilter> pSVGFilterMap) {
		final SVGFilter parent = pSVGFilterMap.get(this.mHref);
		if(parent == null) {
			throw new SVGParseException("Could not resolve href: '" + this.mHref + "' of SVGGradient: '" + this.mID + "'.");
		} else {
			parent.ensureHrefResolved(pSVGFilterMap);
			this.mParent = parent;
			this.mSVGAttributes.setParentSVGAttributes(this.mParent.mSVGAttributes);
		}
	}

	public void applyFilterElements(final Paint pPaint) {
		this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_X, true);
		final ArrayList<ISVGFilterElement> svgFilterElements = this.mSVGFilterElements;
		for(int i = 0; i < svgFilterElements.size(); i++) {
			svgFilterElements.get(i).apply(pPaint);
		}
	}

	public void addFilterElement(final ISVGFilterElement pSVGFilterElement) {
		this.mSVGFilterElements.add(pSVGFilterElement);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
