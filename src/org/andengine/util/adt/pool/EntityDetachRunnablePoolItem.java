package org.andengine.util.adt.pool;

import org.andengine.entity.IEntity;
import org.andengine.util.call.Callback;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
	protected Callback<IEntity> mCallback;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setEntity(final IEntity pEntity) {
		this.mEntity = pEntity;
	}

	/**
	 * Sets up a callback which will get called right after detaching entity
	 * @param pCallback gets called right after detaching entity; if null nothing will be called
	 */
	public void setCallback(final Callback<IEntity> pCallback) {
		this.mCallback = pCallback;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		this.mEntity.detachSelf();

		if(this.mCallback != null) {
			this.mCallback.onCallback(this.mEntity);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}