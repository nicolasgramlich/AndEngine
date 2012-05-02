package org.andengine.util.level;

import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

/**
 * (c) Zynga 2012
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
