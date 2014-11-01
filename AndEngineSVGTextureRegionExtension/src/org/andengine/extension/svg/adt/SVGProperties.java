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
public class SVGProperties implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SVGStyleSet mSVGStyleSet;
	private final Attributes mAttributes;
	private final SVGProperties mParentSVGProperties;

	// ===========================================================
	// Constructors
	// ===========================================================#

	public SVGProperties(final SVGProperties pParentSVGProperties, final Attributes pAttributes, final boolean pAttributesDeepCopy) {
		this.mAttributes = (pAttributesDeepCopy) ? new AttributesImpl(pAttributes) : pAttributes;
		this.mParentSVGProperties = pParentSVGProperties;
		final String styleAttr = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_STYLE);
		if (styleAttr != null) {
			this.mSVGStyleSet = new SVGStyleSet(styleAttr);
		} else {
			this.mSVGStyleSet = null;
		}
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

	public String getStringProperty(final String pPropertyName, final String pDefaultValue) {
		final String s = this.getStringProperty(pPropertyName);
		if (s == null) {
			return pDefaultValue;
		} else {
			return s;
		}
	}

	public String getStringProperty(final String pPropertyName) { // TODO Remove this method and make all others take 'pAllowParentSVGProperties' too.
		return this.getStringProperty(pPropertyName, true);
	}

	public String getStringProperty(final String pPropertyName, final boolean pAllowParentSVGProperties) {
		String s = null;
		if (this.mSVGStyleSet != null) {
			s = this.mSVGStyleSet.getStyle(pPropertyName);
		}
		if (s == null) {
			s = SAXHelper.getStringAttribute(this.mAttributes, pPropertyName);
		}
		if(s == null && pAllowParentSVGProperties) {
			if(this.mParentSVGProperties == null) {
				return null;
			} else {
				return this.mParentSVGProperties.getStringProperty(pPropertyName);
			}
		} else {
			return s;
		}
	}

	public Float getFloatProperty(final String pPropertyName) {
		return SVGParserUtils.extractFloatAttribute(this.getStringProperty(pPropertyName));
	}

	public Float getFloatProperty(final String pPropertyName, final float pDefaultValue) {
		final Float f = this.getFloatProperty(pPropertyName);
		if (f == null) {
			return pDefaultValue;
		} else {
			return f;
		}
	}

	public String getStringAttribute(final String pAttributeName) {
		return SAXHelper.getStringAttribute(this.mAttributes, pAttributeName);
	}

	public String getStringAttribute(final String pAttributeName, final String pDefaultValue) {
		return SAXHelper.getStringAttribute(this.mAttributes, pAttributeName, pDefaultValue);
	}

	public Float getFloatAttribute(final String pAttributeName) {
		return SAXHelper.getFloatAttribute(this.mAttributes, pAttributeName);
	}

	public float getFloatAttribute(final String pAttributeName, final float pDefaultValue) {
		return SAXHelper.getFloatAttribute(this.mAttributes, pAttributeName, pDefaultValue);
	}

	// ===========================================================
	// Property-Testing-Methods
	// ===========================================================

	public static boolean isURLProperty(final String pProperty) {
		return pProperty.startsWith("url(#");
	}

	public static boolean isRGBProperty(final String pProperty) {
		return pProperty.startsWith("rgb(");
	}

	public static boolean isHexProperty(final String pProperty) {
		return pProperty.startsWith("#");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}