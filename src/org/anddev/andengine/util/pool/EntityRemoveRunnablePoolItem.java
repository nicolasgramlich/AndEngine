package org.anddev.andengine.util.pool;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.layer.ILayer;

/**
 * @author Nicolas Gramlich
 * @since 00:53:22 - 28.08.2010
 */
public class EntityRemoveRunnablePoolItem extends RunnablePoolItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected IEntity mEntity;
	protected ILayer mLayer;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setEntity(final IEntity pEntity) {
		this.mEntity = pEntity;
	}

	public void setLayer(final ILayer pLayer) {
		this.mLayer = pLayer;
	}

	public void set(final IEntity pEntity, final ILayer pLayer) {
		this.mEntity = pEntity;
		this.mLayer = pLayer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		this.mLayer.removeChild(this.mEntity);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}