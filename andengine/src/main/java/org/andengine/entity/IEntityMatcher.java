package org.andengine.entity;

import org.andengine.util.IMatcher;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:06:28 - 26.03.2012
 */
public interface IEntityMatcher extends IMatcher<IEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public boolean matches(final IEntity pEntity);
}