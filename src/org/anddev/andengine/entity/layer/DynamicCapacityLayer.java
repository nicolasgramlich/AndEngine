package org.anddev.andengine.entity.layer;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.IEntityMatcher;


/**
 * @author Nicolas Gramlich
 * @since 12:47:43 - 08.03.2010
 */
public class DynamicCapacityLayer extends BaseLayer {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int CAPACITY_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<IEntity> mEntities;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicCapacityLayer() {
		this(CAPACITY_DEFAULT);
	}

	public DynamicCapacityLayer(final int pExpectedCapacity) {
		this.mEntities = new ArrayList<IEntity>(pExpectedCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IEntity getEntity(final int pIndex) {
		return this.mEntities.get(pIndex);
	}

	@Override
	public int getEntityCount() {
		return this.mEntities.size();
	}

	@Override
	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		final ArrayList<IEntity> entities = this.mEntities;
		final int entityCount = entities.size();
		for(int i = 0; i < entityCount; i++) {
			entities.get(i).onDraw(pGL, pCamera);
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

	@Override
	public void reset() {
		super.reset();

		final ArrayList<IEntity> entities = this.mEntities;
		for(int i = entities.size() - 1; i >= 0; i--) {
			entities.get(i).reset();
		}
	}

	@Override
	public void clear() {
		this.mEntities.clear();
	}

	@Override
	public void addEntity(final IEntity pEntity) {
		this.mEntities.add(pEntity);
	}

	@Override
	public boolean removeEntity(final IEntity pEntity) {
		return this.mEntities.remove(pEntity);
	}

	@Override
	public IEntity removeEntity(final int pIndex) {
		return this.mEntities.remove(pIndex);
	}

	@Override
	public boolean removeEntity(final IEntityMatcher pEntityMatcher) {
		final ArrayList<IEntity> entities = this.mEntities;
		for(int i = entities.size() - 1; i >= 0; i--) {
			if(pEntityMatcher.matches(entities.get(i))) {
				entities.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public IEntity findEntity(final IEntityMatcher pEntityMatcher) {
		final ArrayList<IEntity> entities = this.mEntities;
		for(int i = entities.size() - 1; i >= 0; i--) {
			final IEntity entity = entities.get(i);
			if(pEntityMatcher.matches(entity)) {
				return entity;
			}
		}
		return null;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
