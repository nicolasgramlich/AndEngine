package org.anddev.andengine.entity.layer.tiled.tmx;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:10:45 - 20.07.2010
 */
public class TMXLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final TextureManager mTextureManager;
	private final TextureOptions mTextureOptions;
	private final ITMXTilePropertiesListener mTMXTilePropertyListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXLoader(final Context pContext, final TextureManager pTextureManager) {
		this(pContext, pTextureManager, TextureOptions.DEFAULT);
	}

	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final TextureOptions pTextureOptions) {
		this(pContext, pTextureManager, pTextureOptions, null);
	}

	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this(pContext, pTextureManager, TextureOptions.DEFAULT, pTMXTilePropertyListener);
	}

	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final TextureOptions pTextureOptions, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this.mContext = pContext;
		this.mTextureManager = pTextureManager;
		this.mTextureOptions = pTextureOptions;
		this.mTMXTilePropertyListener = pTMXTilePropertyListener;
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

	public TMXTiledMap loadFromAsset(final Context pContext, final String pAssetPath) throws TMXLoadException {
		try {
			return this.load(pContext.getAssets().open(pAssetPath));
		} catch (final IOException e) {
			throw new TMXLoadException("Could not load TMXTiledMap from asset: " + pAssetPath, e);
		}
	}

	public TMXTiledMap load(final InputStream pInputStream) throws TMXLoadException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final TMXParser tmxParser = new TMXParser(this.mContext, this.mTextureManager, this.mTextureOptions, this.mTMXTilePropertyListener);
			xr.setContentHandler(tmxParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return tmxParser.getTMXTiledMap();
		} catch (final SAXException e) {
			throw new TMXLoadException(e);
		} catch (final ParserConfigurationException pe) {
			/* Doesn't happen. */
			return null;
		} catch (final IOException e) {
			throw new TMXLoadException(e);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ITMXTilePropertiesListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties);
	}
}
