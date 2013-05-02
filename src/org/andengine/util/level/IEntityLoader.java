package org.andengine.util.level;

import java.io.IOException;

import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:38:40 - 18.04.2012
 */
public interface IEntityLoader<T extends IEntityLoaderData> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public String[] getEntityNames();

	/**
	 * @param pEntityName
	 * @param pParent can be <code>null</code>, i.e. for the root {@link IEntity}.
	 * @param pAttributes
	 * @param pEntityLoaderData
	 * @return
	 * @throws IOException
	 */
	public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final T pEntityLoaderData) throws IOException;
}