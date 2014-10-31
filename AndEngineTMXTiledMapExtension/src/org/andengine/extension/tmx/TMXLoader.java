package org.andengine.extension.tmx;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;

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

	private final AssetManager mAssetManager;
	private final TextureManager mTextureManager;
	private final TextureOptions mTextureOptions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final ITMXTilePropertiesListener mTMXTilePropertyListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pContext.getAssets(), pTextureManager, pVertexBufferObjectManager);
	}

	public TMXLoader(final AssetManager pAssetManager, final TextureManager pTextureManager, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pAssetManager, pTextureManager, TextureOptions.DEFAULT, pVertexBufferObjectManager);
	}

	@Deprecated
	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final TextureOptions pTextureOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pContext.getAssets(), pTextureManager, pTextureOptions, pVertexBufferObjectManager);
	}

	public TMXLoader(final AssetManager pAssetManager, final TextureManager pTextureManager, final TextureOptions pTextureOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pAssetManager, pTextureManager, pTextureOptions, pVertexBufferObjectManager, null);
	}

	@Deprecated
	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final VertexBufferObjectManager pVertexBufferObjectManager, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this(pContext.getAssets(), pTextureManager, pVertexBufferObjectManager, pTMXTilePropertyListener);
	}

	public TMXLoader(final AssetManager pAssetManager, final TextureManager pTextureManager, final VertexBufferObjectManager pVertexBufferObjectManager, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this(pAssetManager, pTextureManager, TextureOptions.DEFAULT, pVertexBufferObjectManager, pTMXTilePropertyListener);
	}

	@Deprecated
	public TMXLoader(final Context pContext, final TextureManager pTextureManager, final TextureOptions pTextureOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this(pContext.getAssets(), pTextureManager, pTextureOptions, pVertexBufferObjectManager, pTMXTilePropertyListener);
	}

	public TMXLoader(final AssetManager pAssetManager, final TextureManager pTextureManager, final TextureOptions pTextureOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final ITMXTilePropertiesListener pTMXTilePropertyListener) {
		this.mAssetManager = pAssetManager;
		this.mTextureManager = pTextureManager;
		this.mTextureOptions = pTextureOptions;
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
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

	public TMXTiledMap loadFromAsset(final String pAssetPath) throws TMXLoadException {
		try {
			return this.load(this.mAssetManager.open(pAssetPath));
		} catch (final IOException e) {
			throw new TMXLoadException("Could not load TMXTiledMap from asset: " + pAssetPath, e);
		}
	}

	public TMXTiledMap load(final InputStream pInputStream) throws TMXLoadException {
		try {
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final TMXParser tmxParser = new TMXParser(this.mAssetManager, this.mTextureManager, this.mTextureOptions, this.mVertexBufferObjectManager, this.mTMXTilePropertyListener);
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
