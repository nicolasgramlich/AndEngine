package org.andengine.util.level;


/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:05:31 - 19.04.2012
 */
public abstract class EntityLoader<T extends IEntityLoaderData> implements IEntityLoader<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String[] mEntityNames;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityLoader(final String ... pEntityNames) {
		this.mEntityNames = pEntityNames;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String[] getEntityNames() {
		return this.mEntityNames;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
