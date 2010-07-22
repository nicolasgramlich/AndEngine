package org.anddev.andengine.entity.layer;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.runnable.RunnableHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.util.IEntityMatcher;

/**
 * @author Nicolas Gramlich
 * @since 12:09:22 - 09.07.2010
 */
public interface ILayer extends IEntity {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public abstract IEntity getEntity(final int pIndex);

	public abstract void addEntity(final IEntity pEntity);

	public abstract IEntity findEntity(final IEntityMatcher pEntityMatcher);

	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered 
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise 
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the 
	 * Update-Thread or the GL-Thread!</b>
	 */
	public IEntity removeEntity(final int pIndex);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered 
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise 
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the 
	 * Update-Thread or the GL-Thread!</b>
	 */
	public abstract boolean removeEntity(final IEntity pEntity);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered 
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise 
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the 
	 * Update-Thread or the GL-Thread!</b>
	 */
	public abstract boolean removeEntity(final IEntityMatcher pEntityMatcher);

	public ArrayList<ITouchArea> getTouchAreas();

	public void registerTouchArea(final ITouchArea pTouchArea);

	public void unregisterTouchArea(final ITouchArea pTouchArea);
	
	public int getEntityCount();

	public abstract void clear();
}