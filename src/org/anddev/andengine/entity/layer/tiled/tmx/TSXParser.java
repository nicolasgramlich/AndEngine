package org.anddev.andengine.entity.layer.tiled.tmx;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXParseException;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:37:32 - 08.08.2010
 */
public class TSXParser extends DefaultHandler implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final TextureManager mTextureManager;
	private final TextureOptions mTextureOptions;

	private TMXTileSet mTMXTileSet;

	private int mLastTileSetTileID;

	@SuppressWarnings("unused")
	private boolean mInTileset;
	@SuppressWarnings("unused")
	private boolean mInImage;
	@SuppressWarnings("unused")
	private boolean mInTile;
	@SuppressWarnings("unused")
	private boolean mInProperties;
	@SuppressWarnings("unused")
	private boolean mInProperty;
	private final int mFirstGlobalTileID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TSXParser(final Context pContext, final TextureManager pTextureManager, final TextureOptions pTextureOptions, final int pFirstGlobalTileID) {
		this.mContext = pContext;
		this.mTextureManager = pTextureManager;
		this.mTextureOptions = pTextureOptions;
		this.mFirstGlobalTileID = pFirstGlobalTileID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	TMXTileSet getTMXTileSet() {
		return this.mTMXTileSet;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		if(pLocalName.equals(TAG_TILESET)){
			this.mInTileset = true;
			this.mTMXTileSet = new TMXTileSet(this.mFirstGlobalTileID, pAttributes, this.mTextureOptions);
		} else if(pLocalName.equals(TAG_IMAGE)){
			this.mInImage = true;
			this.mTMXTileSet.setImageSource(this.mContext, this.mTextureManager, pAttributes);
		} else if(pLocalName.equals(TAG_TILE)) {
			this.mInTile = true;
			this.mLastTileSetTileID = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TILE_ATTRIBUTE_ID);
		} else if(pLocalName.equals(TAG_PROPERTIES)) {
			this.mInProperties = true;
		} else if(pLocalName.equals(TAG_PROPERTY)) {
			this.mInProperty = true;
			this.mTMXTileSet.addTMXTileProperty(this.mLastTileSetTileID, new TMXTileProperty(pAttributes));
		} else {
			throw new TMXParseException("Unexpected start tag: '" + pLocalName + "'.");
		}
	}

	@Override
	public void endElement(final String pUri, final String pLocalName, final String pQualifiedName) throws SAXException {
		if(pLocalName.equals(TAG_TILESET)){
			this.mInTileset = false;
		} else if(pLocalName.equals(TAG_IMAGE)){
			this.mInImage = false;
		} else if(pLocalName.equals(TAG_TILE)) {
			this.mInTile = false;
		} else if(pLocalName.equals(TAG_PROPERTIES)) {
			this.mInProperties = false;
		} else if(pLocalName.equals(TAG_PROPERTY)) {
			this.mInProperty = false;
		} else {
			throw new TMXParseException("Unexpected end tag: '" + pLocalName + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
