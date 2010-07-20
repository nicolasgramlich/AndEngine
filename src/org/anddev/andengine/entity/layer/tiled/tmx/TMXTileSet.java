package org.anddev.andengine.entity.layer.tiled.tmx;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 19:03:24 - 20.07.2010
 */
public class TMXTileSet implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mFirstGID;
	private final String mName;
	private final int mTileWidth;
	private final int mTileHeight;
	private String mImageSource;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXTileSet(final Attributes pAttributes) {
		this.mFirstGID = Integer.parseInt(pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_FIRSTGID));
		this.mName = pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_NAME);
		this.mTileWidth = Integer.parseInt(pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_TILEWIDTH));
		this.mTileHeight = Integer.parseInt(pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_TILEHEIGHT));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final int getFirstGID() {
		return this.mFirstGID;
	}

	public final String getName() {
		return this.mName;
	}

	public final int getTileWidth() {
		return this.mTileWidth;
	}

	public final int getTileHeight() {
		return this.mTileHeight;
	}

	public void setImageSource(final Attributes pAttributes) {
		this.mImageSource = pAttributes.getValue("", TAG_IMAGE_ATTRIBUTE_SOURCE);
	}

	public String getImageSource() {
		return this.mImageSource;
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
