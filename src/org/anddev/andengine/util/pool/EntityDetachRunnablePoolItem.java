package org.anddev.andengine.util.pool;

import org.anddev.andengine.entity.IEntity;

/**
 * @author Nicolas Gramlich
 * @since 00:53:22 - 28.08.2010
 */
public class EntityDetachRunnablePoolItem extends RunnablePoolItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected IEntity mEntity;
	protected IEntity mParent;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setEntity(final IEntity pEntity) {
		this.mEntity = pEntity;
	}

	public void setParent(final IEntity pParent) {
		this.mParent = pParent;
	}

	public void set(final IEntity pEntity, final IEntity pParent) {
		this.mEntity = pEntity;
		this.mParent = pParent;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		this.mParent.detachChild(this.mEntity);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}