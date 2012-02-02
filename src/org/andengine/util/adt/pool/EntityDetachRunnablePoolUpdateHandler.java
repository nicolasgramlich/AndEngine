package org.andengine.util.adt.pool;

import org.andengine.entity.IEntity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:16:25 - 31.08.2010
 */
public class EntityDetachRunnablePoolUpdateHandler extends RunnablePoolUpdateHandler<EntityDetachRunnablePoolItem> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityDetachRunnablePoolUpdateHandler() {
		super();
	}

	public EntityDetachRunnablePoolUpdateHandler(final int pInitialPoolSize) {
		super(pInitialPoolSize);
	}

	public EntityDetachRunnablePoolUpdateHandler(final int pInitialPoolSize, final int pGrowth) {
		super(pInitialPoolSize, pGrowth);
	}

	public EntityDetachRunnablePoolUpdateHandler(final int pInitialPoolSize, final int pGrowth, final int pAvailableItemCountMaximum) {
		super(pInitialPoolSize, pGrowth, pAvailableItemCountMaximum);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected EntityDetachRunnablePoolItem onAllocatePoolItem() {
		return new EntityDetachRunnablePoolItem();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void scheduleDetach(final IEntity pEntity) {
		final EntityDetachRunnablePoolItem entityDetachRunnablePoolItem = this.obtainPoolItem();
		entityDetachRunnablePoolItem.setEntity(pEntity);
		this.postPoolItem(entityDetachRunnablePoolItem);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
