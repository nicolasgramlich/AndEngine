package org.anddev.andengine.entity.layer;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.IEntityMatcher;


/**
 * @author Nicolas Gramlich
 * @since 12:47:43 - 08.03.2010
 */
public class FixedCapacityLayer extends BaseLayer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IEntity[] mEntities;
	private final int mCapacity;
	private int mEntityCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedCapacityLayer(final int pCapacity) {
		this.mCapacity = pCapacity;
		this.mEntities = new IEntity[pCapacity];
		this.mEntityCount = 0;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public int getEntityCount() {
		return this.mEntityCount;
	}

	@Override
	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		final IEntity[] entities = this.mEntities;
		final int entityCount = this.mEntityCount;
		for(int i = 0; i < entityCount; i++) {
			entities[i].onDraw(pGL, pCamera);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		final IEntity[] entities = this.mEntities;
		final int entityCount = this.mEntityCount;
		for(int i = 0; i < entityCount; i++) {
			entities[i].onUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void reset() {
		super.reset();

		final IEntity[] entities = this.mEntities;
		for(int i = this.mEntityCount - 1; i >= 0; i--) {
			entities[i].reset();
		}
	}

	@Override
	public IEntity getEntity(final int pIndex) {
		if(pIndex < 0 || pIndex > this.mEntityCount - 1) {
			throw new IndexOutOfBoundsException();
		} else {
			return this.mEntities[pIndex];
		}
	}
	
	@Override
	public void clear() {
		final IEntity[] entities = this.mEntities;		
		for(int i = this.mEntityCount - 1; i >= 0; i--) {
			entities[i] = null;
		}
		this.mEntityCount = 0;
	}

	@Override
	public void addEntity(final IEntity pEntity) {
		if(this.mEntityCount < this.mCapacity) {
			this.mEntities[this.mEntityCount] = pEntity;
			this.mEntityCount++;
		}
	}
	
	@Override
	public boolean removeEntity(final IEntity pEntity) {
		return removeEntity(indexOfEntity(pEntity)) != null;
	}

	@Override
	public IEntity removeEntity(final int pIndex) {
		if(pIndex == -1) {
			return null;
		} else {
			final IEntity[] entities = this.mEntities;	
			final IEntity out = entities[pIndex];	
			if(pIndex == this.mEntityCount - 1) {
				this.mEntities[this.mEntityCount--] = null;
			} else {
				entities[pIndex] = entities[this.mEntityCount - 1];	
				this.mEntityCount--;
				entities[this.mEntityCount] = null;
			}
			return out;
		}
	}
	
	@Override
	public boolean removeEntity(final IEntityMatcher pEntityMatcher) {
		final IEntity[] entities = this.mEntities;
		for(int i = entities.length - 1; i >= 0; i--) {
			if(pEntityMatcher.matches(entities[i])) {
				this.removeEntity(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public IEntity findEntity(final IEntityMatcher pEntityMatcher) {
		final IEntity[] entities = this.mEntities;
		for(int i = entities.length - 1; i >= 0; i--) {
			final IEntity entity = entities[i];
			if(pEntityMatcher.matches(entity)) {
				return entity;
			}
		}
		return null;
	}
	
	private int indexOfEntity(final IEntity pEntity) {
		final IEntity[] entities = this.mEntities;
		for(int i = entities.length - 1; i >= 0; i--) {
			final IEntity entity = entities[i];
			if(entity == pEntity) {
				return i;
			}
		}
		return -1;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
