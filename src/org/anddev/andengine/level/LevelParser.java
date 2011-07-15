package org.anddev.andengine.level;

import java.util.HashMap;

import org.anddev.andengine.level.LevelLoader.IEntityLoader;
import org.anddev.andengine.level.util.constants.LevelConstants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:35:32 - 11.10.2010
 */
public class LevelParser extends DefaultHandler implements LevelConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IEntityLoader mDefaultEntityLoader;
	private final HashMap<String, IEntityLoader> mEntityLoaders;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelParser(final IEntityLoader pDefaultEntityLoader, final HashMap<String, IEntityLoader> pEntityLoaders) {
		this.mDefaultEntityLoader = pDefaultEntityLoader;
		this.mEntityLoaders = pEntityLoaders;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		final IEntityLoader entityLoader = this.mEntityLoaders.get(pLocalName);
		if(entityLoader != null) {
			entityLoader.onLoadEntity(pLocalName, pAttributes);
		} else {
			if(this.mDefaultEntityLoader != null) {
				this.mDefaultEntityLoader.onLoadEntity(pLocalName, pAttributes);
			} else {
				throw new IllegalArgumentException("Unexpected tag: '" + pLocalName + "'.");
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
