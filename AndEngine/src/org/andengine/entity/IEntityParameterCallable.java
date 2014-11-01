package org.andengine.entity;

import org.andengine.util.call.ParameterCallable;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:06:53 - 26.03.2012
 */
public interface IEntityParameterCallable extends ParameterCallable<IEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void call(final IEntity pEntity);
}