package org.andengine.entity.scene.menu.item.decorator;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityComparator;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.shape.IShape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.transformation.Transformation;
import org.andengine.util.color.Color;

/**
 * I HATE THIS CLASS!
 * 
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

	protected final IMenuItem mMenuItem;

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
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return this.mMenuItem.getVertexBufferObjectManager();
	}

	@Override
	public IVertexBufferObject getVertexBufferObject() {
		return this.mMenuItem.getVertexBufferObject();
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
	public void setX(final float pX) {
		this.mMenuItem.setX(pX);
	}

	@Override
	public void setY(final float pY) {
		this.mMenuItem.setY(pY);
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
	public void setWidth(final float pWidth) {
		this.mMenuItem.setWidth(pWidth);
	}

	@Override
	public void setHeight(final float pHeight) {
		this.mMenuItem.setHeight(pHeight);
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		this.mMenuItem.setSize(pWidth, pHeight);
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
	public void setRed(final float pRed) {
		this.mMenuItem.setRed(pRed);
	}

	@Override
	public void setGreen(final float pGreen) {
		this.mMenuItem.setGreen(pGreen);
	}

	@Override
	public void setBlue(final float pBlue) {
		this.mMenuItem.setBlue(pBlue);
	}

	@Override
	public void setAlpha(final float pAlpha) {
		this.mMenuItem.setAlpha(pAlpha);
	}

	@Override
	public Color getColor() {
		return this.mMenuItem.getColor();
	}

	@Override
	public void setColor(final Color pColor) {
		this.mMenuItem.setColor(pColor);
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
	public boolean isSkewed() {
		return this.mMenuItem.isSkewed();
	}
	
	@Override
	public float getSkewX() {
		return this.mMenuItem.getSkewX();
	}
	
	@Override
	public float getSkewY() {
		return this.mMenuItem.getSkewY();
	}
	
	@Override
	public void setSkew(final float pSkew) {
		this.mMenuItem.setSkew(pSkew);
	}
	
	@Override
	public void setSkew(final float pSkewX, final float pSkewY) {
		this.mMenuItem.setSkew(pSkewX, pSkewY);
	}
	
	@Override
	public void setSkewX(final float pSkewX) {
		this.mMenuItem.setSkewX(pSkewX);
	}
	
	@Override
	public void setSkewY(final float pSkewY) {
		this.mMenuItem.setSkewY(pSkewY);
	}
	
	@Override
	public float getSkewCenterX() {
		return this.mMenuItem.getSkewCenterX();
	}
	
	@Override
	public float getSkewCenterY() {
		return this.mMenuItem.getSkewCenterY();
	}
	
	@Override
	public void setSkewCenterX(final float pSkewCenterX) {
		this.mMenuItem.setSkewCenterX(pSkewCenterX);
	}
	
	@Override
	public void setSkewCenterY(final float pSkewCenterY) {
		this.mMenuItem.setSkewCenterY(pSkewCenterY);
	}
	
	@Override
	public void setSkewCenter(final float pSkewCenterX, final float pSkewCenterY) {
		this.mMenuItem.setSkewCenter(pSkewCenterX, pSkewCenterY);
	}

	@Override
	public boolean isRotatedOrScaledOrSkewed() {
		return this.mMenuItem.isRotatedOrScaledOrSkewed();
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
	public float[] getSceneCenterCoordinates(final float[] pReuse) {
		return this.mMenuItem.getSceneCenterCoordinates(pReuse);
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
	public int getEntityModifierCount() {
		return this.mMenuItem.getEntityModifierCount();
	}

	@Override
	public void clearEntityModifiers() {
		this.mMenuItem.clearEntityModifiers();
	}

	@Override
	public boolean isBlendingEnabled() {
		return this.mMenuItem.isBlendingEnabled();
	}

	@Override
	public void setBlendingEnabled(final boolean pBlendingEnabled) {
		this.mMenuItem.setBlendingEnabled(pBlendingEnabled);
	}

	@Override
	public int getBlendFunctionSource() {
		return this.mMenuItem.getBlendFunctionSource();
	}

	@Override
	public void setBlendFunctionSource(final int pBlendFunctionSource) {
		this.mMenuItem.setBlendFunctionSource(pBlendFunctionSource);
	}

	@Override
	public int getBlendFunctionDestination() {
		return this.mMenuItem.getBlendFunctionDestination();
	}

	@Override
	public void setBlendFunctionDestination(final int pBlendFunctionDestination) {
		this.mMenuItem.setBlendFunctionDestination(pBlendFunctionDestination);
	}

	@Override
	public void setBlendFunction(final int pBlendFunctionSource, final int pBlendFunctionDestination) {
		this.mMenuItem.setBlendFunction(pBlendFunctionSource, pBlendFunctionDestination);
	}

	@Override
	public void setCullingEnabled(final boolean pCullingEnabled) {
		this.mMenuItem.setCullingEnabled(pCullingEnabled);
	}

	@Override
	public int getTag() {
		return this.mMenuItem.getTag();
	}

	@Override
	public void setTag(final int pTag) {
		this.mMenuItem.setTag(pTag);
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
	public ShaderProgram getShaderProgram() {
		return this.mMenuItem.getShaderProgram();
	}

	@Override
	public void setShaderProgram(final ShaderProgram pShaderProgram) {
		this.mMenuItem.setShaderProgram(pShaderProgram);
	}

	@Override
	public void onDraw(final GLState pGLState, final Camera pCamera) {
		this.mMenuItem.onDraw(pGLState, pCamera);
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
	public boolean isDisposed() {
		return this.mMenuItem.isDisposed();
	}

	@Override
	public void dispose() {
		this.mMenuItem.dispose();
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
	public IEntity getFirstChild() {
		return this.mMenuItem.getFirstChild();
	}

	@Override
	public IEntity getLastChild() {
		return this.mMenuItem.getLastChild();
	}

	@Override
	public IEntity getChildByTag(final int pTag) {
		return this.mMenuItem.getChildByTag(pTag);
	}

	@Override
	public IEntity getChildByIndex(final int pIndex) {
		return this.mMenuItem.getChildByIndex(pIndex);
	}

	@Override
	public IEntity getChildByMatcher(IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.getChildByMatcher(pEntityMatcher);
	}

	@Override
	public ArrayList<IEntity> query(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.query(pEntityMatcher);
	}

	@Override
	public IEntity queryFirst(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.queryFirst(pEntityMatcher);
	}

	@Override
	public <L extends List<IEntity>> L query(final IEntityMatcher pEntityMatcher, final L pResult) {
		return this.mMenuItem.query(pEntityMatcher, pResult);
	}

	@Override
	public <S extends IEntity> S queryFirstForSubclass(final IEntityMatcher pEntityMatcher) {
		return this.mMenuItem.queryFirstForSubclass(pEntityMatcher);
	}

	@Override
	public <S extends IEntity> ArrayList<S> queryForSubclass(final IEntityMatcher pEntityMatcher) throws ClassCastException {
		return this.mMenuItem.queryForSubclass(pEntityMatcher);
	}

	@Override
	public <L extends List<S>, S extends IEntity> L queryForSubclass(final IEntityMatcher pEntityMatcher, final L pResult) throws ClassCastException {
		return this.mMenuItem.queryForSubclass(pEntityMatcher, pResult);
	}

	@Override
	public void sortChildren() {
		this.mMenuItem.sortChildren();
	}
	
	@Override
	public void sortChildren(final boolean pImmediate) {
		this.mMenuItem.sortChildren(pImmediate);
	}

	@Override
	public void sortChildren(final IEntityComparator pEntityComparator) {
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
	public IEntity detachChild(final int pTag) {
		return this.mMenuItem.detachChild(pTag);
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
	public void callOnChildren(final IEntityParameterCallable pEntityParameterCallable) {
		this.mMenuItem.callOnChildren(pEntityParameterCallable);
	}

	@Override
	public void callOnChildren(final IEntityParameterCallable pEntityParameterCallable, final IEntityMatcher pEntityMatcher) {
		this.mMenuItem.callOnChildren(pEntityParameterCallable, pEntityMatcher);
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
	public Transformation getLocalToParentTransformation() {
		return this.mMenuItem.getLocalToParentTransformation();
	}

	@Override
	public Transformation getParentToLocalTransformation() {
		return this.mMenuItem.getParentToLocalTransformation();
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
	public boolean isCulled(final Camera pCamera) {
		return this.mMenuItem.isCulled(pCamera);
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
	public int getUpdateHandlerCount() {
		return this.mMenuItem.getUpdateHandlerCount();
	}

	@Override
	public void clearUpdateHandlers() {
		this.mMenuItem.clearUpdateHandlers();
	}

	@Override
	public boolean unregisterUpdateHandlers(final IUpdateHandlerMatcher pUpdateHandlerMatcher) {
		return this.mMenuItem.unregisterUpdateHandlers(pUpdateHandlerMatcher);
	}

	@Override
	public void toString(final StringBuilder pStringBuilder) {
		this.mMenuItem.toString(pStringBuilder);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
