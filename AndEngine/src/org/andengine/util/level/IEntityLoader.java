package org.andengine.util.level;

import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:38:40 - 18.04.2012
 */
public interface IEntityLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes);
}