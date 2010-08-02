package org.anddev.andengine.entity.layer.tiled.tmx;

import java.util.ArrayList;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 11:20:49 - 29.07.2010
 */
public class TMXObjectGroup implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mName;
	private final int mWidth;
	private final int mHeight;
	private final ArrayList<TMXObject> mTMXObjects = new ArrayList<TMXObject>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXObjectGroup(final Attributes pAttributes) {
		this.mName = pAttributes.getValue("", TAG_OBJECTGROUP_ATTRIBUTE_NAME);
		this.mWidth = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_OBJECTGROUP_ATTRIBUTE_WIDTH);
		this.mHeight = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_OBJECTGROUP_ATTRIBUTE_HEIGHT);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getName() {
		return this.mName;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	void addTMXObject(final TMXObject pTMXObject) {
		this.mTMXObjects.add(pTMXObject);
	}

	public ArrayList<TMXObject> getTMXObjects() {
		return this.mTMXObjects ;
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
