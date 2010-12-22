package org.anddev.andengine.util.pool;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.layer.Layer;

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
	protected Layer mLayer;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setEntity(final IEntity pEntity) {
		this.mEntity = pEntity;
	}

	public void setLayer(final Layer pLayer) {
		this.mLayer = pLayer;
	}

	public void set(final IEntity pEntity, final Layer pLayer) {
		this.mEntity = pEntity;
		this.mLayer = pLayer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		this.mLayer.detachChild(this.mEntity);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}