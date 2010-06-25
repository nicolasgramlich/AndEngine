package org.anddev.andengine.entity;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.handler.runnable.RunnableHandler;
import org.anddev.andengine.util.IEntityMatcher;

import android.speech.tts.TextToSpeech.Engine;


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

	@Override
	public void reset() {
		super.reset();

		final ArrayList<IEntity> entities = this.mEntities;
		for(int i = entities.size() - 1; i >= 0; i--) {
			entities.get(i).reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void clear() {
		this.mEntities.clear();
	}

	public void addEntity(final IEntity pEntity) {
		this.mEntities.add(pEntity);
	}
	
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered 
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise 
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the 
	 * Update-Thread or the GL-Thread!</b>
	 */
	public boolean removeEntity(final IEntity pEntity) {
		return this.mEntities.remove(pEntity);
	}
	
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
	// Inner and Anonymous Classes
	// ===========================================================
}
