package org.andengine.util.level;

import java.io.IOException;
import java.util.HashMap;

import org.andengine.BuildConfig;
import org.andengine.entity.IEntity;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.exception.LevelLoaderException;
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
public abstract class LevelLoaderContentHandler<T extends IEntityLoaderData, L extends IEntityLoaderListener, R extends ILevelLoaderResult> extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IEntityLoader<T> mDefaultEntityLoader;
	protected final HashMap<String, IEntityLoader<T>> mEntityLoaders;
	protected final T mEntityLoaderData;

	private final SmartList<IEntity> mParentEntityStack = new SmartList<IEntity>();

	protected IEntity mRootEntity;
	protected L mEntityLoaderListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelLoaderContentHandler(final IEntityLoader<T> pDefaultEntityLoader, final HashMap<String, IEntityLoader<T>> pEntityLoaders, final T pEntityLoaderData) {
		this(pDefaultEntityLoader, pEntityLoaders, pEntityLoaderData, null);
	}

	public LevelLoaderContentHandler(final IEntityLoader<T> pDefaultEntityLoader, final HashMap<String, IEntityLoader<T>> pEntityLoaders, final T pEntityLoaderData, final L pEntityLoaderListener) {
		this.mDefaultEntityLoader = pDefaultEntityLoader;
		this.mEntityLoaders = pEntityLoaders;
		this.mEntityLoaderData = pEntityLoaderData;
		this.mEntityLoaderListener = pEntityLoaderListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public abstract R getLevelLoaderResult();

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException, LevelLoaderException {
		final String entityName = pLocalName;

		final IEntity parent = (this.mParentEntityStack.isEmpty()) ? null : this.mParentEntityStack.getLast();

		IEntityLoader<T> entityLoader = this.mEntityLoaders.get(entityName);
		if (entityLoader == null) {
			if (this.mDefaultEntityLoader == null) {
				throw new LevelLoaderException("Unexpected tag: '" + entityName + "'.");
			} else {
				entityLoader = this.mDefaultEntityLoader;
			}
		}

		final IEntity entity;
		try {
			entity = entityLoader.onLoadEntity(entityName, parent, pAttributes, this.mEntityLoaderData);
		} catch (final IOException e) {
			throw new LevelLoaderException("Exception when loading entity: '" + entityName + "'.", e);
		}

		if (entity == null) {
			if (BuildConfig.DEBUG) {
				Debug.w("No '" + IEntity.class.getSimpleName() + "' created for entity name: '" + entityName + "'.");
			}
		} else {
			if (parent == null) {
				if (this.mRootEntity == null) {
					this.mRootEntity = entity;
				}
			} else {
				parent.attachChild(entity);
			}

			if (this.mEntityLoaderListener != null) {
				this.mEntityLoaderListener.onEntityLoaded(entity, pAttributes);
			}
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
