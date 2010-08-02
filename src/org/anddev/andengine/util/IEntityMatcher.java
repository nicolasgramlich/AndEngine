package org.anddev.andengine.util;

import org.anddev.andengine.entity.IEntity;

/**
 * @author Nicolas Gramlich
 * @since 15:45:58 - 21.06.2010
 */
public interface IEntityMatcher {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean matches(final IEntity pEntity);
}

