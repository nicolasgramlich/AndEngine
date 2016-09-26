package org.anddev.andengine.entity.layer.tiled.tmx;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TSXLoadException;
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
 * @since 17:18:37 - 08.08.2010
 */
public class TSXLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final TextureManager mTextureManager;
	private final TextureOptions mTextureOptions;

	private static String sAssetBasePath = "";

	// ===========================================================
	// Constructors
	// ===========================================================

	public TSXLoader(final Context pContext, final TextureManager pTextureManager, final TextureOptions pTextureOptions) {
		this.mContext = pContext;
		this.mTextureManager = pTextureManager;
		this.mTextureOptions = pTextureOptions;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public static void setAssetBasePath(final String pAssetBasePath) {
		if(pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			TSXLoader.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalArgumentException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public TMXTileSet loadFromAsset(final Context pContext, final int pFirstGlobalTileID, final String pAssetPath) throws TSXLoadException {
		try {
			return this.load(pFirstGlobalTileID, pContext.getAssets().open(TSXLoader.sAssetBasePath + pAssetPath));
		} catch (final IOException e) {
			throw new TSXLoadException("Could not load TMXTileSet from asset: " + TSXLoader.sAssetBasePath + pAssetPath, e);
		}
	}

	private TMXTileSet load(final int pFirstGlobalTileID, final InputStream pInputStream) throws TSXLoadException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final TSXParser tsxParser = new TSXParser(this.mContext, this.mTextureManager, this.mTextureOptions, pFirstGlobalTileID);
			xr.setContentHandler(tsxParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return tsxParser.getTMXTileSet();
		} catch (final SAXException e) {
			throw new TSXLoadException(e);
		} catch (final ParserConfigurationException pe) {
			/* Doesn't happen. */
			return null;
		} catch (final IOException e) {
			throw new TSXLoadException(e);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
