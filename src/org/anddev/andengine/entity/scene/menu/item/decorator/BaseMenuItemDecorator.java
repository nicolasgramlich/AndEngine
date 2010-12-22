package org.anddev.andengine.entity.scene.menu.item.decorator;

import java.util.ArrayList;
import java.util.Comparator;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.IEntityMatcher;
import org.anddev.andengine.util.modifier.IModifier;

import android.graphics.Matrix;

/**
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
	public void accelerate(final float pAccelerationX, final float pAccelerationY) {
		this.mMenuItem.accelerate(pAccelerationX, pAccelerationY);
	}

	@Override
	public void addEntityModifier(final IModifier<IEntity> pEntityModifier) {
		this.mMenuItem.addEntityModifier(pEntityModifier);
	}

	@Override
	public void clearEntityModifiers() {
		this.mMenuItem.clearEntityModifiers();
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		return this.mMenuItem.collidesWith(pOtherShape);
	}

	@Override
	public float getAccelerationX() {
		return this.mMenuItem.getAccelerationX();
	}

	@Override
	public float getAccelerationY() {
		return this.mMenuItem.getAccelerationY();
	}

	@Override
	public float getAlpha() {
		return this.mMenuItem.getAlpha();
	}

	@Override
	public float getAngularVelocity() {
		return this.mMenuItem.getAngularVelocity();
	}

	@Override
	public float getBaseHeight() {
		return this.mMenuItem.getBaseHeight();
	}

	@Override
	public float getBaseWidth() {
		return this.mMenuItem.getBaseWidth();
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
	public float getBlue() {
		return this.mMenuItem.getBlue();
	}

	@Override
	public float getGreen() {
		return this.mMenuItem.getGreen();
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
	public float getRed() {
		return this.mMenuItem.getRed();
	}

	@Override
	public float getRotation() {
		return this.mMenuItem.getRotation();
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
	public float getScaleCenterX() {
		return this.mMenuItem.getScaleCenterX();
	}

	@Override
	public float getScaleCenterY() {
		return this.mMenuItem.getScaleCenterY();
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
	public float[] getSceneCenterCoordinates() {
		return this.mMenuItem.getSceneCenterCoordinates();
	}

	@Override
	public float getVelocityX() {
		return this.mMenuItem.getVelocityX();
	}

	@Override
	public float getVelocityY() {
		return this.mMenuItem.getVelocityY();
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
	public float getX() {
		return this.mMenuItem.getX();
	}

	@Override
	public float getY() {
		return this.mMenuItem.getY();
	}

	@Override
	public boolean isCullingEnabled() {
		return this.mMenuItem.isCullingEnabled();
	}

	@Override
	public boolean isScaled() {
		return this.mMenuItem.isScaled();
	}

	@Override
	public boolean isUpdatePhysics() {
		return this.mMenuItem.isUpdatePhysics();
	}

	@Override
	public boolean removeEntityModifier(final IModifier<IEntity> pEntityModifier) {
		return this.mMenuItem.removeEntityModifier(pEntityModifier);
	}

	@Override
	public void setAcceleration(final float pAcceleration) {
		this.mMenuItem.setAcceleration(pAcceleration);
	}

	@Override
	public void setAcceleration(final float pAccelerationX, final float pAccelerationY) {
		this.mMenuItem.setAcceleration(pAccelerationX, pAccelerationY);
	}

	@Override
	public void setAccelerationX(final float pAccelerationX) {
		this.mMenuItem.setAccelerationX(pAccelerationX);
	}

	@Override
	public void setAccelerationY(final float pAccelerationY) {
		this.mMenuItem.setAccelerationY(pAccelerationY);
	}

	@Override
	public void setAlpha(final float pAlpha) {
		this.mMenuItem.setAlpha(pAlpha);
	}

	@Override
	public void setAngularVelocity(final float pAngularVelocity) {
		this.mMenuItem.setAngularVelocity(pAngularVelocity);
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
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		this.mMenuItem.setColor(pRed, pGreen, pBlue);
	}

	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mMenuItem.setColor(pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public void setCullingEnabled(final boolean pCullingEnabled) {
		this.mMenuItem.setCullingEnabled(pCullingEnabled);
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
	public void setRotation(final float pRotation) {
		this.mMenuItem.setRotation(pRotation);
	}

	@Override
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mMenuItem.setRotationCenter(pRotationCenterX, pRotationCenterY);
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
	public void setScale(final float pScale) {
		this.mMenuItem.setScale(pScale);
	}

	@Override
	public void setScale(final float pScaleX, final float pScaleY) {
		this.mMenuItem.setScale(pScaleX, pScaleY);
	}

	@Override
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mMenuItem.setScaleCenter(pScaleCenterX, pScaleCenterY);
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
	public void setScaleX(final float pScaleX) {
		this.mMenuItem.setScaleX(pScaleX);
	}

	@Override
	public void setScaleY(final float pScaleY) {
		this.mMenuItem.setScaleY(pScaleY);
	}

	@Override
	public void setUpdatePhysics(final boolean pUpdatePhysics) {
		this.mMenuItem.setUpdatePhysics(pUpdatePhysics);
	}

	@Override
	public void setVelocity(final float pVelocity) {
		this.mMenuItem.setVelocity(pVelocity);
	}

	@Override
	public void setVelocity(final float pVelocityX, final float pVelocityY) {
		this.mMenuItem.setVelocity(pVelocityX, pVelocityY);
	}

	@Override
	public void setVelocityX(final float pVelocityX) {
		this.mMenuItem.setVelocityX(pVelocityX);
	}

	@Override
	public void setVelocityY(final float pVelocityY) {
		this.mMenuItem.setVelocityY(pVelocityY);
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
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY) {
		return this.mMenuItem.convertSceneToLocalCoordinates(pX, pY);
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
	public IEntity getFirstChild() {
		return this.mMenuItem.getFirstChild();
	}

	@Override
	public IEntity getLastChild() {
		return this.mMenuItem.getLastChild();
	}

	@Override
	public IEntity findChild(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.findChild(pEntityMatcher);
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
	public boolean detachChild(final IEntity pEntity) {
		return this.mMenuItem.detachChild(pEntity);
	}

	@Override
	public boolean detachChild(final IEntityMatcher pEntityMatcher) {
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
	public ArrayList<ITouchArea> getTouchAreas() {
		return this.mMenuItem.getTouchAreas();
	}

	@Override
	public void registerTouchArea(final ITouchArea pTouchArea) {
		this.mMenuItem.registerTouchArea(pTouchArea);
	}

	@Override
	public void unregisterTouchArea(final ITouchArea pTouchArea) {
		this.mMenuItem.unregisterTouchArea(pTouchArea);
	}

	@Override
	public Matrix getLocalToSceneMatrix() {
		return this.mMenuItem.getLocalToSceneMatrix();
	}

	@Override
	public Matrix getSceneToLocalMatrix() {
		return this.mMenuItem.getSceneToLocalMatrix();
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
	public boolean isIgnoreUpdate() {
		return this.mMenuItem.isIgnoreUpdate();
	}

	@Override
	public void setIgnoreUpdate(final boolean pIgnoreUpdate) {
		this.mMenuItem.setIgnoreUpdate(pIgnoreUpdate);
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
	public IEntity getChild(final int pIndex) {
		return this.mMenuItem.getChild(pIndex);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
