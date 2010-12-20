package org.anddev.andengine.entity;

import java.util.ArrayList;
import java.util.Comparator;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.layer.ZIndexSorter;
import org.anddev.andengine.util.IEntityMatcher;


/**
 * @author Nicolas Gramlich
 * @since 12:00:48 - 08.03.2010
 */
public abstract class Entity implements IEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CHILDREN_CAPACITY_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mVisible = true;
	private boolean mIgnoreUpdate = false;
	private int mZIndex = 0;

	private IEntity mParent;

	private ArrayList<IEntity> mChildren;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Entity() {

	}

	public Entity(final int pZIndex) {
		this.mZIndex = pZIndex;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isVisible() {
		return this.mVisible;
	}

	public void setVisible(final boolean pVisible) {
		this.mVisible = pVisible;
	}

	public boolean isIgnoreUpdate() {
		return this.mIgnoreUpdate;
	}

	public void setIgnoreUpdate(final boolean pIgnoreUpdate) {
		this.mIgnoreUpdate = pIgnoreUpdate;
	}

	public IEntity getParent() {
		return this.mParent;
	}

	public void setParent(final IEntity pParent) {
		this.mParent = pParent;
	}

	@Override
	public int getZIndex() {
		return this.mZIndex;
	}

	@Override
	public void setZIndex(final int pZIndex) {
		this.mZIndex = pZIndex;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public final void onDraw(final GL10 pGL, final Camera pCamera) {
		if(this.mVisible) {
			this.onManagedDraw(pGL, pCamera);
		}
	}

	@Override
	public final void onUpdate(final float pSecondsElapsed) {
		if(!this.mIgnoreUpdate) {
			this.onManagedUpdate(pSecondsElapsed);
		}
	}

	@Override
	public IEntity getChild(final int pIndex) {
		if(this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(pIndex);
	}

	@Override
	public int getChildCount() {
		if(this.mChildren == null) {
			return 0;
		}
		return this.mChildren.size();
	}

	@Override
	public void reset() {
		this.mVisible = true;
		this.mIgnoreUpdate = false;

		final ArrayList<IEntity> entities = this.mChildren;
		for(int i = entities.size() - 1; i >= 0; i--) {
			entities.get(i).reset();
		}
	}

	@Override
	public void clearChildren() {
		if(this.mChildren == null) {
			return;
		}
		this.mChildren.clear();
	}

	@Override
	public void addChild(final IEntity pEntity) {
		if(this.mChildren == null) {
			this.allocateChildren();
		}

		this.mChildren.add(pEntity);
	}

	@Override
	public boolean removeChild(final IEntity pEntity) {
		if(this.mChildren == null) {
			return false;
		}
		return this.mChildren.remove(pEntity);
	}

	@Override
	public IEntity removeChild(final int pIndex) {
		if(this.mChildren == null) {
			return null;
		}
		return this.mChildren.remove(pIndex);
	}

	@Override
	public boolean removeChild(final IEntityMatcher pEntityMatcher) {
		if(this.mChildren == null) {
			return false;
		}
		final ArrayList<IEntity> entities = this.mChildren;
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
		if(this.mChildren == null) {
			return null;
		}
		final ArrayList<IEntity> entities = this.mChildren;
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
		if(this.mChildren == null) {
		return;
	}
		ZIndexSorter.getInstance().sort(this.mChildren);
	}

	@Override
	public void sortChildren(final Comparator<IEntity> pEntityComparator) {
		if(this.mChildren == null) {
			return;
		}
		ZIndexSorter.getInstance().sort(this.mChildren, pEntityComparator);
	}

	@Override
	public IEntity replaceChild(final int pEntityIndex, final IEntity pEntity) {
		if(this.mChildren == null) {
			return null;
		}
		final ArrayList<IEntity> entities = this.mChildren;
		final IEntity oldEntity = entities.set(pEntityIndex, pEntity);
		return oldEntity;
	}

	@Override
	public void setChild(final int pEntityIndex, final IEntity pEntity) {
		if(this.mChildren == null) {
			return;
		}
		if(pEntityIndex == this.mChildren.size()) {
			this.addChild(pEntity);
		} else {
			this.mChildren.set(pEntityIndex, pEntity);
		}
	}

	@Override
	public void swapChildren(final int pEntityIndexA, final int pEntityIndexB) {
		if(this.mChildren == null) {
			return;
		}
		final ArrayList<IEntity> entities = this.mChildren;
		final IEntity entityA = entities.get(pEntityIndexA);
		final IEntity entityB = entities.set(pEntityIndexB, entityA);
		entities.set(pEntityIndexA, entityB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void allocateChildren() {
		this.mChildren = new ArrayList<IEntity>(CHILDREN_CAPACITY_DEFAULT);
	}

	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		final ArrayList<IEntity> entities = this.mChildren;
		final int entityCount = entities.size();
		for(int i = 0; i < entityCount; i++) {
			entities.get(i).onDraw(pGL, pCamera);
		}
	}

	protected void onManagedUpdate(final float pSecondsElapsed) {
		final ArrayList<IEntity> entities = this.mChildren;
		final int entityCount = entities.size();
		for(int i = 0; i < entityCount; i++) {
			entities.get(i).onUpdate(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
