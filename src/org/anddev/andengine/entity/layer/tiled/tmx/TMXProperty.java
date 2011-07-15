package org.anddev.andengine.entity.layer.tiled.tmx;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.xml.sax.Attributes;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:14:06 - 27.07.2010
 */
public class TMXProperty implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mName;
	private final String mValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXProperty(final Attributes pAttributes) {
		this.mName = pAttributes.getValue("", TAG_PROPERTY_ATTRIBUTE_NAME);
		this.mValue = pAttributes.getValue("", TAG_PROPERTY_ATTRIBUTE_VALUE);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getName() {
		return this.mName;
	}

	public String getValue() {
		return this.mValue;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return this.mName + "='" + this.mValue + "'";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
