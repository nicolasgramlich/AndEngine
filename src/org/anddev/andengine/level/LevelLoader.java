package org.anddev.andengine.level;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.andengine.level.util.constants.LevelConstants;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:16:19 - 11.10.2010
 */
public class LevelLoader implements LevelConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private String mAssetBasePath;

	private IEntityLoader mDefaultEntityLoader;
	private final HashMap<String, IEntityLoader> mEntityLoaders = new HashMap<String, IEntityLoader>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelLoader() {
		this("");
	}

	public LevelLoader(final String pAssetBasePath) {
		this.setAssetBasePath(pAssetBasePath);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public IEntityLoader getDefaultEntityLoader() {
		return this.mDefaultEntityLoader;
	}
	
	public void setDefaultEntityLoader(IEntityLoader pDefaultEntityLoader) {
		this.mDefaultEntityLoader = pDefaultEntityLoader;
	}

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public void setAssetBasePath(final String pAssetBasePath) {
		if(pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			this.mAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalStateException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onAfterLoadLevel() {

	}

	protected void onBeforeLoadLevel() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerEntityLoader(final String pEntityName, final IEntityLoader pEntityLoader) {
		this.mEntityLoaders.put(pEntityName, pEntityLoader);
	}

	public void registerEntityLoader(final String[] pEntityNames, final IEntityLoader pEntityLoader) {
		final HashMap<String, IEntityLoader> entityLoaders = this.mEntityLoaders;

		for(int i = pEntityNames.length - 1; i >= 0; i--) {
			entityLoaders.put(pEntityNames[i], pEntityLoader);
		}
	}

	public void loadLevelFromAsset(final Context pContext, final String pAssetPath) throws IOException {
		this.loadLevelFromStream(pContext.getAssets().open(this.mAssetBasePath + pAssetPath));
	}

	public void loadLevelFromResource(final Context pContext, final int pRawResourceID) throws IOException {
		this.loadLevelFromStream(pContext.getResources().openRawResource(pRawResourceID));
	}

	public void loadLevelFromStream(final InputStream pInputStream) throws IOException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();

			this.onBeforeLoadLevel();

			final LevelParser levelParser = new LevelParser(this.mDefaultEntityLoader, this.mEntityLoaders);
			xr.setContentHandler(levelParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			this.onAfterLoadLevel();
		} catch (final SAXException se) {
			Debug.e(se);
			/* Doesn't happen. */
		} catch (final ParserConfigurationException pe) {
			Debug.e(pe);
			/* Doesn't happen. */
		} finally {
			StreamUtils.close(pInputStream);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IEntityLoader {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onLoadEntity(final String pEntityName, final Attributes pAttributes);
	}
}
