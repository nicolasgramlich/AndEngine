package org.andengine.util.level;

import java.util.HashMap;

import org.andengine.entity.IEntity;
import org.andengine.util.adt.list.SmartList;
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
public class LevelParser extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IEntityLoader mDefaultEntityLoader;
	private final HashMap<String, IEntityLoader> mEntityLoaders;

	private SmartList<IEntity> mParentEntityStack = new SmartList<IEntity>();

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
		final String entityName = pLocalName;

		final IEntity parent = (this.mParentEntityStack.isEmpty()) ? null : this.mParentEntityStack.getLast();

		final IEntityLoader entityLoader = this.mEntityLoaders.get(entityName);

		final IEntity entity;
		if(entityLoader != null) {
			entity = entityLoader.onLoadEntity(entityName, pAttributes);
		} else if(this.mDefaultEntityLoader != null) {
			entity = this.mDefaultEntityLoader.onLoadEntity(entityName, pAttributes);
		} else {
			throw new IllegalArgumentException("Unexpected tag: '" + entityName + "'.");
		}

		if(parent != null && entity != null) {
			parent.attachChild(entity);
		}

		this.mParentEntityStack.addLast(entity);
	}

	@Override
	public void endElement(final String pUri, final String pLocalName, final String pQualifiedName) throws SAXException {
		this.mParentEntityStack.removeLast();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
