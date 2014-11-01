package org.andengine.extension.svg.adt;

import org.andengine.extension.svg.util.SAXHelper;
import org.andengine.extension.svg.util.SVGParserUtils;
import org.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:49:55 - 21.05.2011
 */
public class SVGAttributes implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Attributes mAttributes;
	private SVGAttributes mParentSVGAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGAttributes(final Attributes pAttributes, final boolean pAttributesDeepCopy) {
		this.mAttributes = (pAttributesDeepCopy) ? new AttributesImpl(pAttributes) : pAttributes;
	}

	public SVGAttributes(final SVGAttributes pParentSVGAttributes, final Attributes pAttributes, final boolean pAttributesDeepCopy) {
		this.mAttributes = (pAttributesDeepCopy) ? new AttributesImpl(pAttributes) : pAttributes;
		this.mParentSVGAttributes = pParentSVGAttributes;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParentSVGAttributes(final SVGAttributes pParentSVGAttributes) {
		this.mParentSVGAttributes = pParentSVGAttributes;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public String getStringAttribute(final String pAttributeName, final boolean pAllowParentSVGAttributes, final String pDefaultValue) {
		final String s = this.getStringAttribute(pAttributeName, pAllowParentSVGAttributes);
		if (s == null) {
			return pDefaultValue;
		} else {
			return s;
		}
	}

	public String getStringAttribute(final String pAttributeName, final boolean pAllowParentSVGAttributes) {
		final String s = SAXHelper.getStringAttribute(this.mAttributes, pAttributeName);
		if(s == null && pAllowParentSVGAttributes) {
			if(this.mParentSVGAttributes == null) {
				return null;
			} else {
				return this.mParentSVGAttributes.getStringAttribute(pAttributeName, pAllowParentSVGAttributes);
			}
		} else {
			return s;
		}
	}

	public Float getFloatAttribute(final String pAttributeName, final boolean pAllowParentSVGAttributes) {
		return SVGParserUtils.extractFloatAttribute(this.getStringAttribute(pAttributeName, pAllowParentSVGAttributes));
	}

	public Float getFloatAttribute(final String pAttributeName, final boolean pAllowParentSVGAttributes, final float pDefaultValue) {
		final Float f = this.getFloatAttribute(pAttributeName, pAllowParentSVGAttributes);
		if (f == null) {
			return pDefaultValue;
		} else {
			return f;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}