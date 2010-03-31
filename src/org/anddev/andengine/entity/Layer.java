package org.anddev.andengine.entity;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author Nicolas Gramlich
 * @since 12:47:43 - 08.03.2010
 */
public class Layer extends BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<IEntity> mEntities;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Layer() {
		this.mEntities = new ArrayList<IEntity>();
	}

	public Layer(final int pExpectedEntityCount) {
		this.mEntities = new ArrayList<IEntity>(pExpectedEntityCount);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		final ArrayList<IEntity> entities = this.mEntities;
		final int entityCount = entities.size();
		for(int i = 0; i < entityCount; i++) {
			entities.get(i).onDraw(pGL);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		final ArrayList<IEntity> entities = this.mEntities;
		final int entityCount = entities.size();
		for(int i = 0; i < entityCount; i++) {
			entities.get(i).onUpdate(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addEntity(final IEntity pEntity) {
		this.mEntities.add(pEntity);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
