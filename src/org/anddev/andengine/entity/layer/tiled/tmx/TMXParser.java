package org.anddev.andengine.entity.layer.tiled.tmx;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.util.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 19:11:29 - 20.07.2010
 */
public class TMXParser extends DefaultHandler implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final Context mContext;
	private final TextureManager mTextureManager;

	private TMXTiledMap mTMXTiledMap;
	private final StringBuilder mStringBuilder = new StringBuilder();

	@SuppressWarnings("unused")
	private boolean mInMap;
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
	@SuppressWarnings("unused")
	private boolean mInLayer;
	@SuppressWarnings("unused")
	private boolean mInData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXParser(final Context pContext, final TextureManager pTextureManager) {
		this.mContext = pContext;
		this.mTextureManager = pTextureManager;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	TMXTiledMap getTMXTiledMap() {
		return this.mTMXTiledMap;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		if(pLocalName.equals(TAG_MAP)){
			this.mInMap = true;
			this.mTMXTiledMap = new TMXTiledMap(pAttributes);
		} else if(pLocalName.equals(TAG_TILESET)){
			this.mInTileset = true;
			this.mTMXTiledMap.addTMXTileSet(new TMXTileSet(pAttributes));
		} else if(pLocalName.equals(TAG_IMAGE)){
			this.mInImage = true;
			final ArrayList<TMXTileSet> tmxTileSets = this.mTMXTiledMap.getTMXTileSets();
			tmxTileSets.get(tmxTileSets.size() - 1).setImageSource(this.mContext, this.mTextureManager, pAttributes);
		} else if(pLocalName.equals(TAG_LAYER)){
			this.mInLayer = true;
			this.mTMXTiledMap.addTMXLayer(new TMXLayer(this.mTMXTiledMap, pAttributes));
		} else if(pLocalName.equals(TAG_DATA)){
			this.mInData = true;
		} else {
			throw new IllegalArgumentException("Unexpected tag: '" + pLocalName + "'.");
		}
	}

	@Override
	public void characters(final char[] pCharacters, final int pStart, final int pLength) throws SAXException {
		this.mStringBuilder.append(pCharacters, pStart, pLength);
	}

	@Override
	public void endElement(final String pUri, final String pLocalName, final String pQualifiedName) throws SAXException {
		if(pLocalName.equals(TAG_MAP)){
			this.mInMap = false;
		} else if(pLocalName.equals(TAG_TILESET)){
			this.mInTileset = false;
		} else if(pLocalName.equals(TAG_IMAGE)){
			this.mInImage = false;
		} else if(pLocalName.equals(TAG_LAYER)){
			this.mInLayer = false;
		} else if(pLocalName.equals(TAG_DATA)){
			final ArrayList<TMXLayer> tmxLayers = this.mTMXTiledMap.getTMXLayers();
			try {
				tmxLayers.get(tmxLayers.size() - 1).setData(this.mStringBuilder.toString());
			} catch (final IOException e) {
				Debug.e(e);
			}
			this.mInData = false;
		} else {
			throw new IllegalArgumentException("Unexpected tag: '" + pLocalName + "'.");
		}

		/* Reset the StringBuilder. */
		this.mStringBuilder.setLength(0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
