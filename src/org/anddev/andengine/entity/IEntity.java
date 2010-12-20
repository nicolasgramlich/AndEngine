package org.anddev.andengine.entity;

import java.util.ArrayList;
import java.util.Comparator;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.runnable.RunnableHandler;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.opengl.IDrawable;
import org.anddev.andengine.util.IEntityMatcher;


/**
 * @author Nicolas Gramlich
 * @since 11:20:25 - 08.03.2010
 */
public interface IEntity extends IDrawable, IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getZIndex();
	public void setZIndex(final int pZIndex);

	public int getChildCount();

	public void addChild(final IEntity pEntity);
	
	public void setChild(final int pEntityIndex, final IEntity pEntity); // TODO Is working on indexes desireable? Or maybe work on zIndexes or a tag like Cocos2D does. 

	/**
	 * Similar to {@link ILayer#setChild(int, ILayer)} but returns the {@link IEntity} that would be overwritten.
	 * 
	 * @param pEntityIndex
	 * @param pEntity
	 * @return the layer that has been replaced.
	 */
	public IEntity replaceChild(final int pEntityIndex, final IEntity pEntity);

	public void swapChildren(final int pEntityIndexA, final int pEntityIndexB);

	public IEntity getChild(final int pIndex);

	public IEntity findChild(final IEntityMatcher pEntityMatcher);
	
	/**
	 * Sorts the {@link IEntity}s based on their ZIndex. Sort is stable. 
	 */
	public void sortChildren();
	
	/**
	 * Sorts the {@link IEntity}s based on the {@link Comparator} supplied. Sort is stable.
	 * @param pEntityComparator
	 */
	public void sortChildren(final Comparator<IEntity> pEntityComparator);

	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public IEntity removeChild(final int pIndex); // TODO Is working on indexes desireable? Or maybe work on zIndexes or a tag like Cocos2D does.
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public boolean removeChild(final IEntity pEntity);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public boolean removeChild(final IEntityMatcher pEntityMatcher);

	public void clearChildren();
}
