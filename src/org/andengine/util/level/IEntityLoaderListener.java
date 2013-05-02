package org.andengine.util.level;

import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:52:39 - 02.05.2012
 */
public interface IEntityLoaderListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onEntityLoaded(final IEntity pEntity, final Attributes pAttributes);
}
