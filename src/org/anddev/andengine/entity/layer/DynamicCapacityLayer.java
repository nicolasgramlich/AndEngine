package org.anddev.andengine.entity.layer;

import java.util.ArrayList;
import java.util.Comparator;

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

	// ===========================================================
	// Fields
	// ===========================================================

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
	public IEntity getChild(final int pIndex) {
		return this.mEntities.get(pIndex);
	}

	@Override
	public int getChildCount() {
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
	public void clearChildren() {
		this.mEntities.clear();
	}

	@Override
	public void addChild(final IEntity pEntity) {
		this.mEntities.add(pEntity);
	}

	@Override
	public boolean removeChild(final IEntity pEntity) {
		return this.mEntities.remove(pEntity);
	}

	@Override
	public IEntity removeChild(final int pIndex) {
		return this.mEntities.remove(pIndex);
	}

	@Override
	public boolean removeChild(final IEntityMatcher pEntityMatcher) {
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
	public IEntity findChild(final IEntityMatcher pEntityMatcher) {
		final ArrayList<IEntity> entities = this.mEntities;
		for(int i = entities.size() - 1; i >= 0; i--) {
			final IEntity entity = entities.get(i);
			if(pEntityMatcher.matches(entity)) {
				return entity;
			}
		}
		return null;
	}

	@Override
	public void sortChildren() {
		ZIndexSorter.getInstance().sort(this.mEntities);
	}
	
	@Override
	public void sortChildren(final Comparator<IEntity> pEntityComparator) {
		ZIndexSorter.getInstance().sort(this.mEntities, pEntityComparator);
	}

	@Override
	public IEntity replaceChild(final int pEntityIndex, final IEntity pEntity) {
		final ArrayList<IEntity> entities = this.mEntities;
		final IEntity oldEntity = entities.set(pEntityIndex, pEntity);
		return oldEntity;
	}

	@Override
	public void setChild(final int pEntityIndex, final IEntity pEntity) {
		if(pEntityIndex == this.mEntities.size()) {
			this.addChild(pEntity);
		} else {
			this.mEntities.set(pEntityIndex, pEntity);
		}
	}

	@Override
	public void swapChildren(final int pEntityIndexA, final int pEntityIndexB) {
		final ArrayList<IEntity> entities = this.mEntities;
		final IEntity entityA = entities.get(pEntityIndexA);
		final IEntity entityB = entities.set(pEntityIndexB, entityA);
		entities.set(pEntityIndexA, entityB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
