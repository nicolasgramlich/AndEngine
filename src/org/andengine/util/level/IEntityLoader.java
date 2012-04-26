package org.andengine.util.level;

import java.io.IOException;

import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

/**
 * (c) Zynga 2012
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

	public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes, final T pEntityLoaderData) throws IOException;
}