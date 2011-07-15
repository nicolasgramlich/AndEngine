package org.anddev.andengine.entity.scene.menu.item.decorator;

import java.util.Comparator;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.Transformation;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:05:44 - 18.11.2010
 */
public abstract class BaseMenuItemDecorator implements IMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IMenuItem mMenuItem;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseMenuItemDecorator(final IMenuItem pMenuItem) {
		this.mMenuItem = pMenuItem;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onMenuItemSelected(final IMenuItem pMenuItem);
	protected abstract void onMenuItemUnselected(final IMenuItem pMenuItem);
	protected abstract void onMenuItemReset(final IMenuItem pMenuItem);

	@Override
	public int getID() {
		return this.mMenuItem.getID();
	}

	@Override
	public final void onSelected() {
		this.mMenuItem.onSelected();
		this.onMenuItemSelected(this.mMenuItem);
	}

	@Override
	public final void onUnselected() {
		this.mMenuItem.onUnselected();
		this.onMenuItemUnselected(this.mMenuItem);
	}

	@Override
	public float getX() {
		return this.mMenuItem.getX();
	}

	@Override
	public float getY() {
		return this.mMenuItem.getY();
	}

	@Override
	public void setPosition(final IEntity pOtherEntity) {
		this.mMenuItem.setPosition(pOtherEntity);
	}

	@Override
	public void setPosition(final float pX, final float pY) {
		this.mMenuItem.setPosition(pX, pY);
	}

	@Override
	public float getBaseWidth() {
		return this.mMenuItem.getBaseWidth();
	}

	@Override
	public float getBaseHeight() {
		return this.mMenuItem.getBaseHeight();
	}

	@Override
	public float getWidth() {
		return this.mMenuItem.getWidth();
	}

	@Override
	public float getWidthScaled() {
		return this.mMenuItem.getWidthScaled();
	}

	@Override
	public float getHeight() {
		return this.mMenuItem.getHeight();
	}

	@Override
	public float getHeightScaled() {
		return this.mMenuItem.getHeightScaled();
	}

	@Override
	public float getInitialX() {
		return this.mMenuItem.getInitialX();
	}

	@Override
	public float getInitialY() {
		return this.mMenuItem.getInitialY();
	}

	@Override
	public float getRed() {
		return this.mMenuItem.getRed();
	}

	@Override
	public float getGreen() {
		return this.mMenuItem.getGreen();
	}

	@Override
	public float getBlue() {
		return this.mMenuItem.getBlue();
	}

	@Override
	public float getAlpha() {
		return this.mMenuItem.getAlpha();
	}

	@Override
	public void setAlpha(final float pAlpha) {
		this.mMenuItem.setAlpha(pAlpha);
	}

	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		this.mMenuItem.setColor(pRed, pGreen, pBlue);
	}

	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mMenuItem.setColor(pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public boolean isRotated() {
		return this.mMenuItem.isRotated();
	}

	@Override
	public float getRotation() {
		return this.mMenuItem.getRotation();
	}

	@Override
	public void setRotation(final float pRotation) {
		this.mMenuItem.setRotation(pRotation);
	}

	@Override
	public float getRotationCenterX() {
		return this.mMenuItem.getRotationCenterX();
	}

	@Override
	public float getRotationCenterY() {
		return this.mMenuItem.getRotationCenterY();
	}

	@Override
	public void setRotationCenterX(final float pRotationCenterX) {
		this.mMenuItem.setRotationCenterX(pRotationCenterX);
	}

	@Override
	public void setRotationCenterY(final float pRotationCenterY) {
		this.mMenuItem.setRotationCenterY(pRotationCenterY);
	}

	@Override
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mMenuItem.setRotationCenter(pRotationCenterX, pRotationCenterY);
	}

	@Override
	public boolean isScaled() {
		return this.mMenuItem.isScaled();
	}

	@Override
	public float getScaleX() {
		return this.mMenuItem.getScaleX();
	}

	@Override
	public float getScaleY() {
		return this.mMenuItem.getScaleY();
	}

	@Override
	public void setScale(final float pScale) {
		this.mMenuItem.setScale(pScale);
	}

	@Override
	public void setScale(final float pScaleX, final float pScaleY) {
		this.mMenuItem.setScale(pScaleX, pScaleY);
	}

	@Override
	public void setScaleX(final float pScaleX) {
		this.mMenuItem.setScaleX(pScaleX);
	}

	@Override
	public void setScaleY(final float pScaleY) {
		this.mMenuItem.setScaleY(pScaleY);
	}

	@Override
	public float getScaleCenterX() {
		return this.mMenuItem.getScaleCenterX();
	}

	@Override
	public float getScaleCenterY() {
		return this.mMenuItem.getScaleCenterY();
	}

	@Override
	public void setScaleCenterX(final float pScaleCenterX) {
		this.mMenuItem.setScaleCenterX(pScaleCenterX);
	}

	@Override
	public void setScaleCenterY(final float pScaleCenterY) {
		this.mMenuItem.setScaleCenterY(pScaleCenterY);
	}

	@Override
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mMenuItem.setScaleCenter(pScaleCenterX, pScaleCenterY);
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		return this.mMenuItem.collidesWith(pOtherShape);
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return this.mMenuItem.getSceneCenterCoordinates();
	}

	@Override
	public boolean isCullingEnabled() {
		return this.mMenuItem.isCullingEnabled();
	}

	@Override
	public void registerEntityModifier(final IEntityModifier pEntityModifier) {
		this.mMenuItem.registerEntityModifier(pEntityModifier);
	}

	@Override
	public boolean unregisterEntityModifier(final IEntityModifier pEntityModifier) {
		return this.mMenuItem.unregisterEntityModifier(pEntityModifier);
	}

	@Override
	public boolean unregisterEntityModifiers(final IEntityModifierMatcher pEntityModifierMatcher) {
		return this.mMenuItem.unregisterEntityModifiers(pEntityModifierMatcher);
	}

	@Override
	public void clearEntityModifiers() {
		this.mMenuItem.clearEntityModifiers();
	}

	@Override
	public void setInitialPosition() {
		this.mMenuItem.setInitialPosition();
	}

	@Override
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mMenuItem.setBlendFunction(pSourceBlendFunction, pDestinationBlendFunction);
	}

	@Override
	public void setCullingEnabled(final boolean pCullingEnabled) {
		this.mMenuItem.setCullingEnabled(pCullingEnabled);
	}

	@Override
	public int getZIndex() {
		return this.mMenuItem.getZIndex();
	}

	@Override
	public void setZIndex(final int pZIndex) {
		this.mMenuItem.setZIndex(pZIndex);
	}

	@Override
	public void onDraw(final GL10 pGL, final Camera pCamera) {
		this.mMenuItem.onDraw(pGL, pCamera);
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mMenuItem.onUpdate(pSecondsElapsed);
	}

	@Override
	public void reset() {
		this.mMenuItem.reset();
		this.onMenuItemReset(this.mMenuItem);
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return this.mMenuItem.contains(pX, pY);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(final float pX, final float pY) {
		return this.mMenuItem.convertLocalToSceneCoordinates(pX, pY);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(final float pX, final float pY, final float[] pReuse) {
		return this.mMenuItem.convertLocalToSceneCoordinates(pX, pY, pReuse);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(final float[] pCoordinates) {
		return this.mMenuItem.convertLocalToSceneCoordinates(pCoordinates);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(final float[] pCoordinates, final float[] pReuse) {
		return this.mMenuItem.convertLocalToSceneCoordinates(pCoordinates, pReuse);
	}

	@Override
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY) {
		return this.mMenuItem.convertSceneToLocalCoordinates(pX, pY);
	}

	@Override
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY, final float[] pReuse) {
		return this.mMenuItem.convertSceneToLocalCoordinates(pX, pY, pReuse);
	}

	@Override
	public float[] convertSceneToLocalCoordinates(final float[] pCoordinates) {
		return this.mMenuItem.convertSceneToLocalCoordinates(pCoordinates);
	}

	@Override
	public float[] convertSceneToLocalCoordinates(final float[] pCoordinates, final float[] pReuse) {
		return this.mMenuItem.convertSceneToLocalCoordinates(pCoordinates, pReuse);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return this.mMenuItem.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	@Override
	public int getChildCount() {
		return this.mMenuItem.getChildCount();
	}

	@Override
	public void attachChild(final IEntity pEntity) {
		this.mMenuItem.attachChild(pEntity);
	}

	@Override
	public boolean attachChild(final IEntity pEntity, final int pIndex) {
		return this.mMenuItem.attachChild(pEntity, pIndex);
	}

	@Override
	public IEntity getFirstChild() {
		return this.mMenuItem.getFirstChild();
	}

	@Override
	public IEntity getLastChild() {
		return this.mMenuItem.getLastChild();
	}

	@Override
	public IEntity getChild(final int pIndex) {
		return this.mMenuItem.getChild(pIndex);
	}

	@Override
	public int getChildIndex(final IEntity pEntity) {
		return this.mMenuItem.getChildIndex(pEntity);
	}

	@Override
	public boolean setChildIndex(final IEntity pEntity, final int pIndex) {
		return this.mMenuItem.setChildIndex(pEntity, pIndex);
	}

	@Override
	public IEntity findChild(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.findChild(pEntityMatcher);
	}

	@Override
	public boolean swapChildren(final IEntity pEntityA, final IEntity pEntityB) {
		return this.mMenuItem.swapChildren(pEntityA, pEntityB);
	}

	@Override
	public boolean swapChildren(final int pIndexA, final int pIndexB) {
		return this.mMenuItem.swapChildren(pIndexA, pIndexB);
	}

	@Override
	public void sortChildren() {
		this.mMenuItem.sortChildren();
	}

	@Override
	public void sortChildren(final Comparator<IEntity> pEntityComparator) {
		this.mMenuItem.sortChildren(pEntityComparator);
	}

	@Override
	public boolean detachSelf() {
		return this.mMenuItem.detachSelf();
	}

	@Override
	public boolean detachChild(final IEntity pEntity) {
		return this.mMenuItem.detachChild(pEntity);
	}

	@Override
	public IEntity detachChild(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.detachChild(pEntityMatcher);
	}

	@Override
	public boolean detachChildren(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.detachChildren(pEntityMatcher);
	}

	@Override
	public void detachChildren() {
		this.mMenuItem.detachChildren();
	}

	@Override
	public void callOnChildren(final IEntityCallable pEntityCallable) {
		this.callOnChildren(pEntityCallable);
	}

	@Override
	public void callOnChildren(final IEntityMatcher pEntityMatcher, final IEntityCallable pEntityCallable) {
		this.mMenuItem.callOnChildren(pEntityMatcher, pEntityCallable);
	}

	@Override
	public Transformation getLocalToSceneTransformation() {
		return this.mMenuItem.getLocalToSceneTransformation();
	}

	@Override
	public Transformation getSceneToLocalTransformation() {
		return this.mMenuItem.getSceneToLocalTransformation();
	}

	@Override
	public boolean hasParent() {
		return this.mMenuItem.hasParent();
	}

	@Override
	public IEntity getParent() {
		return this.mMenuItem.getParent();
	}

	@Override
	public void setParent(final IEntity pEntity) {
		this.mMenuItem.setParent(pEntity);
	}

	@Override
	public boolean isVisible() {
		return this.mMenuItem.isVisible();
	}

	@Override
	public void setVisible(final boolean pVisible) {
		this.mMenuItem.setVisible(pVisible);
	}

	@Override
	public boolean isChildrenVisible() {
		return this.mMenuItem.isChildrenVisible();
	}

	@Override
	public void setChildrenVisible(final boolean pChildrenVisible) {
		this.mMenuItem.setChildrenVisible(pChildrenVisible);
	}

	@Override
	public boolean isIgnoreUpdate() {
		return this.mMenuItem.isIgnoreUpdate();
	}

	@Override
	public void setIgnoreUpdate(final boolean pIgnoreUpdate) {
		this.mMenuItem.setIgnoreUpdate(pIgnoreUpdate);
	}

	@Override
	public boolean isChildrenIgnoreUpdate() {
		return this.mMenuItem.isChildrenIgnoreUpdate();
	}

	@Override
	public void setChildrenIgnoreUpdate(final boolean pChildrenIgnoreUpdate) {
		this.mMenuItem.setChildrenIgnoreUpdate(pChildrenIgnoreUpdate);
	}

	@Override
	public void setUserData(final Object pUserData) {
		this.mMenuItem.setUserData(pUserData);
	}

	@Override
	public Object getUserData() {
		return this.mMenuItem.getUserData();
	}

	@Override
	public void onAttached() {
		this.mMenuItem.onAttached();
	}

	@Override
	public void onDetached() {
		this.mMenuItem.onDetached();
	}

	@Override
	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler) {
		this.mMenuItem.registerUpdateHandler(pUpdateHandler);
	}

	@Override
	public boolean unregisterUpdateHandler(final IUpdateHandler pUpdateHandler) {
		return this.mMenuItem.unregisterUpdateHandler(pUpdateHandler);
	}

	@Override
	public void clearUpdateHandlers() {
		this.mMenuItem.clearUpdateHandlers();
	}

	@Override
	public boolean unregisterUpdateHandlers(final IUpdateHandlerMatcher pUpdateHandlerMatcher) {
		return this.mMenuItem.unregisterUpdateHandlers(pUpdateHandlerMatcher);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
