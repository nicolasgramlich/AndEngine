package org.andengine.entity;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:04:41 - 19.11.2011
 */
public interface IEntityFactory<T extends IEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public T create(final float pX, final float pY);
}
