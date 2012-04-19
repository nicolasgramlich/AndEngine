package org.andengine.util.level;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.util.StreamUtils;
import org.andengine.util.level.exception.LevelLoaderException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.res.AssetManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @param <T>
 * @since 14:16:19 - 11.10.2010
 */
public abstract class LevelLoader<T extends IEntityLoaderDataSource, R extends ILevelLoaderResult> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final HashMap<String, IEntityLoader<T>> mEntityLoaders = new HashMap<String, IEntityLoader<T>>();
	private IEntityLoader<T> mDefaultEntityLoader;
	private T mEntityLoaderDataSource;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelLoader() {

	}
	
	public LevelLoader(final T pEntityLoaderDataSource) {
		this.mEntityLoaderDataSource = pEntityLoaderDataSource;
	}

	public LevelLoader(final IEntityLoader<T> pDefaultEntityLoader) {
		this.mDefaultEntityLoader = pDefaultEntityLoader;
	}

	public LevelLoader(final T pEntityLoaderDataSource, final IEntityLoader<T> pDefaultEntityLoader) {
		this.mEntityLoaderDataSource = pEntityLoaderDataSource;
		this.mDefaultEntityLoader = pDefaultEntityLoader;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setDefaultEntityLoader(final IEntityLoader<T> pDefaultEntityLoader) {
		this.mDefaultEntityLoader = pDefaultEntityLoader;
	}

	public void setEntityLoaderDataSource(final T pEntityLoaderDataSource) {
		this.mEntityLoaderDataSource = pEntityLoaderDataSource;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	protected abstract LevelLoaderContentHandler<T, R> onCreateLevelLoaderContentHandler(final HashMap<String, IEntityLoader<T>> pEntityLoaders, final IEntityLoader<T> pDefaultEntityLoader, final T pEntityLoaderDataSource);

	public void registerEntityLoader(final IEntityLoader<T> pEntityLoader) {
		final String[] entityNames = pEntityLoader.getEntityNames();
		for(int i = 0; i < entityNames.length; i++) {
			final String entityName = entityNames[i];
			this.mEntityLoaders.put(entityName, pEntityLoader);
		}
	}

	public void clearEntityLoaders() {
		this.mEntityLoaders.clear();
	}

	public R loadLevelFromAsset(final AssetManager pAssetManager, final String pAssetPath) throws LevelLoaderException {
		try {
			return this.loadLevelFromStream(pAssetManager.open(pAssetPath));
		} catch (final IOException e) {
			throw new LevelLoaderException(e);
		}
	}

	public R loadLevelFromStream(final InputStream pInputStream) throws LevelLoaderException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();

			final LevelLoaderContentHandler<T, R> levelContentHandler = this.onCreateLevelLoaderContentHandler(this.mEntityLoaders, this.mDefaultEntityLoader, this.mEntityLoaderDataSource);
			xr.setContentHandler(levelContentHandler);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return levelContentHandler.getLevelLoaderResult();
		} catch (final Exception e) {
			throw new LevelLoaderException(e);
		} finally {
			StreamUtils.close(pInputStream);
		}
	}


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
