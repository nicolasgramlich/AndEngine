package org.anddev.andengine.entity;

import java.util.Comparator;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.runnable.RunnableHandler;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.IDrawable;
import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.Transformation;



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

	public boolean isVisible();
	public void setVisible(boolean pVisible);

	public boolean isIgnoreUpdate();
	public void setIgnoreUpdate(boolean pIgnoreUpdate);

	public int getZIndex();
	public void setZIndex(final int pZIndex);

	public IEntity getParent();
	public void setParent(final IEntity pEntity);

	public float getX();
	public float getY();

	public float getInitialX();
	public float getInitialY();

	public void setInitialPosition();
	public void setPosition(final IEntity pOtherEntity);
	public void setPosition(final float pX, final float pY);

	public float getRotation();
	public void setRotation(final float pRotation);

	public float getRotationCenterX();
	public float getRotationCenterY();
	public void setRotationCenterX(final float pRotationCenterX);
	public void setRotationCenterY(final float pRotationCenterY);
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY);

	public boolean isScaled();
	public float getScaleX();
	public float getScaleY();
	public void setScaleX(final float pScaleX);
	public void setScaleY(final float pScaleY);
	public void setScale(final float pScale);
	public void setScale(final float pScaleX, final float pScaleY);

	public float getScaleCenterX();
	public float getScaleCenterY();
	public void setScaleCenterX(final float pScaleCenterX);
	public void setScaleCenterY(final float pScaleCenterY);
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY);

	public float getRed();
	public float getGreen();
	public float getBlue();
	public float getAlpha();
	public void setAlpha(final float pAlpha);

	public void setColor(final float pRed, final float pGreen, final float pBlue);
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha);

	public float[] getSceneCenterCoordinates();

	public float[] convertLocalToSceneCoordinates(final float pX, final float pY);
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY);

	public Transformation getLocalToSceneTransformation();
	public Transformation getSceneToLocalTransformation();

	public int getChildCount();

	public void onAttached();
	public void onDetached();

	public void attachChild(final IEntity pEntity);

	public IEntity getChild(final int pIndex);
	public IEntity getFirstChild();
	public IEntity getLastChild();

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
	public boolean detachChild(final IEntity pEntity);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public IEntity detachChild(final IEntityMatcher pEntityMatcher);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link ArrayIndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public boolean detachChildren(final IEntityMatcher pEntityMatcher);

	public void detachChildren();

	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler);
	public boolean unregisterUpdateHandler(final IUpdateHandler pUpdateHandler);
	public boolean unregisterUpdateHandlers(final IUpdateHandlerMatcher pUpdateHandlerMatcher);
	public void clearUpdateHandlers();

	public void registerEntityModifier(final IEntityModifier pEntityModifier);
	public boolean unregisterEntityModifier(final IEntityModifier pEntityModifier);
	public boolean unregisterEntityModifiers(final IEntityModifierMatcher pEntityModifierMatcher);
	public void clearEntityModifiers();

	public void setUserData(Object pUserData);
	public Object getUserData();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface IEntityMatcher extends IMatcher<IEntity> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
