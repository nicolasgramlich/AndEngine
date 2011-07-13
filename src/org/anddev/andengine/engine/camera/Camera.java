package org.anddev.andengine.engine.camera;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.collision.RectangularShapeCollisionChecker;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:24:18 - 25.03.2010
 */
public class Camera implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final float[] VERTICES_TOUCH_TMP = new float[2];

	// ===========================================================
	// Fields
	// ===========================================================

	private float mMinX;
	private float mMaxX;
	private float mMinY;
	private float mMaxY;

	private float mNearZ = -1.0f;
	private float mFarZ = 1.0f;

	private HUD mHUD;

	private IEntity mChaseEntity;

	protected float mRotation = 0;
	protected float mCameraSceneRotation = 0;

	protected int mSurfaceX;
	protected int mSurfaceY;
	protected int mSurfaceWidth;
	protected int mSurfaceHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Camera(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mMinX = pX;
		this.mMaxX = pX + pWidth;
		this.mMinY = pY;
		this.mMaxY = pY + pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinX() {
		return this.mMinX;
	}

	public float getMaxX() {
		return this.mMaxX;
	}

	public float getMinY() {
		return this.mMinY;
	}

	public float getMaxY() {
		return this.mMaxY;
	}

	public float getNearZClippingPlane() {
		return this.mNearZ;
	}

	public float getFarZClippingPlane() {
		return this.mFarZ;
	}

	public void setNearZClippingPlane(final float pNearZClippingPlane) {
		this.mNearZ = pNearZClippingPlane;
	}

	public void setFarZClippingPlane(final float pFarZClippingPlane) {
		this.mFarZ = pFarZClippingPlane;
	}

	public void setZClippingPlanes(final float pNearZClippingPlane, final float pFarZClippingPlane) {
		this.mNearZ = pNearZClippingPlane;
		this.mFarZ = pFarZClippingPlane;
	}

	public float getWidth() {
		return this.mMaxX - this.mMinX;
	}

	public float getHeight() {
		return this.mMaxY - this.mMinY;
	}

	public float getWidthRaw() {
		return this.mMaxX - this.mMinX;
	}

	public float getHeightRaw() {
		return this.mMaxY - this.mMinY;
	}

	public float getCenterX() {
		final float minX = this.mMinX;
		return minX + (this.mMaxX - minX) * 0.5f;
	}

	public float getCenterY() {
		final float minY = this.mMinY;
		return minY + (this.mMaxY - minY) * 0.5f;
	}

	public void setCenter(final float pCenterX, final float pCenterY) {
		final float dX = pCenterX - this.getCenterX();
		final float dY = pCenterY - this.getCenterY();

		this.mMinX += dX;
		this.mMaxX += dX;
		this.mMinY += dY;
		this.mMaxY += dY;
	}

	public void offsetCenter(final float pX, final float pY) {
		this.setCenter(this.getCenterX() + pX, this.getCenterY() + pY);
	}

	public HUD getHUD() {
		return this.mHUD;
	}

	public void setHUD(final HUD pHUD) {
		this.mHUD = pHUD;
		pHUD.setCamera(this);
	}

	public boolean hasHUD() {
		return this.mHUD != null;
	}

	public void setChaseEntity(final IEntity pChaseEntity) {
		this.mChaseEntity = pChaseEntity;
	}

	public float getRotation() {
		return this.mRotation;
	}

	public void setRotation(final float pRotation) {
		this.mRotation = pRotation;
	}

	public float getCameraSceneRotation() {
		return this.mCameraSceneRotation;
	}

	public void setCameraSceneRotation(final float pCameraSceneRotation) {
		this.mCameraSceneRotation = pCameraSceneRotation;
	}

	public int getSurfaceX() {
		return this.mSurfaceX;
	}

	public int getSurfaceY() {
		return this.mSurfaceY;
	}

	public int getSurfaceWidth() {
		return this.mSurfaceWidth;
	}

	public int getSurfaceHeight() {
		return this.mSurfaceHeight;
	}

	public void setSurfaceSize(final int pSurfaceX, final int pSurfaceY, final int pSurfaceWidth, final int pSurfaceHeight) {
		this.mSurfaceX = pSurfaceX;
		this.mSurfaceY = pSurfaceY;
		this.mSurfaceWidth = pSurfaceWidth;
		this.mSurfaceHeight = pSurfaceHeight;
	}

	public boolean isRotated() {
		return this.mRotation != 0;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mHUD != null) {
			this.mHUD.onUpdate(pSecondsElapsed);
		}

		this.updateChaseEntity();
	}

	@Override
	public void reset() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDrawHUD(final GL10 pGL) {
		if(this.mHUD != null) {
			this.mHUD.onDraw(pGL, this);
		}
	}

	public void updateChaseEntity() {
		if(this.mChaseEntity != null) {
			final float[] centerCoordinates = this.mChaseEntity.getSceneCenterCoordinates();
			this.setCenter(centerCoordinates[VERTEX_INDEX_X], centerCoordinates[VERTEX_INDEX_Y]);
		}
	}

	public boolean isLineVisible(final Line pLine) {
		return RectangularShapeCollisionChecker.isVisible(this, pLine);
	}

	public boolean isRectangularShapeVisible(final RectangularShape pRectangularShape) {
		return RectangularShapeCollisionChecker.isVisible(this, pRectangularShape);
	}

	public void onApplySceneMatrix(final GL10 pGL) {
		GLHelper.setProjectionIdentityMatrix(pGL);

		pGL.glOrthof(this.getMinX(), this.getMaxX(), this.getMaxY(), this.getMinY(), this.mNearZ, this.mFarZ);

		final float rotation = this.mRotation;
		if(rotation != 0) {
			this.applyRotation(pGL, this.getCenterX(), this.getCenterY(), rotation);
		}
	}

	public void onApplySceneBackgroundMatrix(final GL10 pGL) {
		GLHelper.setProjectionIdentityMatrix(pGL);

		final float widthRaw = this.getWidthRaw();
		final float heightRaw = this.getHeightRaw();

		pGL.glOrthof(0, widthRaw, heightRaw, 0, this.mNearZ, this.mFarZ);

		final float rotation = this.mRotation;
		if(rotation != 0) {
			this.applyRotation(pGL, widthRaw * 0.5f, heightRaw * 0.5f, rotation);
		}
	}

	public void onApplyCameraSceneMatrix(final GL10 pGL) {
		GLHelper.setProjectionIdentityMatrix(pGL);

		final float widthRaw = this.getWidthRaw();
		final float heightRaw = this.getHeightRaw();

		pGL.glOrthof(0, widthRaw, heightRaw, 0, this.mNearZ, this.mFarZ);

		final float cameraSceneRotation = this.mCameraSceneRotation;
		if(cameraSceneRotation != 0) {
			this.applyRotation(pGL, widthRaw * 0.5f, heightRaw * 0.5f, cameraSceneRotation);
		}
	}

	private void applyRotation(final GL10 pGL, final float pRotationCenterX, final float pRotationCenterY, final float pAngle) {
		pGL.glTranslatef(pRotationCenterX, pRotationCenterY, 0);
		pGL.glRotatef(pAngle, 0, 0, 1);
		pGL.glTranslatef(-pRotationCenterX, -pRotationCenterY, 0);
	}

	public void convertSceneToCameraSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		this.unapplySceneRotation(pSceneTouchEvent);

		this.applySceneToCameraSceneOffset(pSceneTouchEvent);

		this.applyCameraSceneRotation(pSceneTouchEvent);
	}

	public void convertCameraSceneToSceneTouchEvent(final TouchEvent pCameraSceneTouchEvent) {
		this.unapplyCameraSceneRotation(pCameraSceneTouchEvent);

		this.unapplySceneToCameraSceneOffset(pCameraSceneTouchEvent);

		this.applySceneRotation(pCameraSceneTouchEvent);
	}

	protected void applySceneToCameraSceneOffset(final TouchEvent pSceneTouchEvent) {
		pSceneTouchEvent.offset(-this.mMinX, -this.mMinY);
	}

	protected void unapplySceneToCameraSceneOffset(final TouchEvent pCameraSceneTouchEvent) {
		pCameraSceneTouchEvent.offset(this.mMinX, this.mMinY);
	}

	private void applySceneRotation(final TouchEvent pCameraSceneTouchEvent) {
		final float rotation = -this.mRotation;
		if(rotation != 0) {
			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pCameraSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pCameraSceneTouchEvent.getY();

			MathUtils.rotateAroundCenter(VERTICES_TOUCH_TMP, rotation, this.getCenterX(), this.getCenterY());

			pCameraSceneTouchEvent.set(VERTICES_TOUCH_TMP[VERTEX_INDEX_X], VERTICES_TOUCH_TMP[VERTEX_INDEX_Y]);
		}
	}

	private void unapplySceneRotation(final TouchEvent pSceneTouchEvent) {
		final float rotation = this.mRotation;

		if(rotation != 0) {
			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.revertRotateAroundCenter(VERTICES_TOUCH_TMP, rotation, this.getCenterX(), this.getCenterY());

			pSceneTouchEvent.set(VERTICES_TOUCH_TMP[VERTEX_INDEX_X], VERTICES_TOUCH_TMP[VERTEX_INDEX_Y]);
		}
	}

	private void applyCameraSceneRotation(final TouchEvent pSceneTouchEvent) {
		final float cameraSceneRotation = -this.mCameraSceneRotation;

		if(cameraSceneRotation != 0) {
			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.rotateAroundCenter(VERTICES_TOUCH_TMP, cameraSceneRotation, (this.mMaxX - this.mMinX) * 0.5f, (this.mMaxY - this.mMinY) * 0.5f);

			pSceneTouchEvent.set(VERTICES_TOUCH_TMP[VERTEX_INDEX_X], VERTICES_TOUCH_TMP[VERTEX_INDEX_Y]);
		}
	}

	private void unapplyCameraSceneRotation(final TouchEvent pCameraSceneTouchEvent) {
		final float cameraSceneRotation = -this.mCameraSceneRotation;

		if(cameraSceneRotation != 0) {
			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pCameraSceneTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pCameraSceneTouchEvent.getY();

			MathUtils.revertRotateAroundCenter(VERTICES_TOUCH_TMP, cameraSceneRotation, (this.mMaxX - this.mMinX) * 0.5f, (this.mMaxY - this.mMinY) * 0.5f);

			pCameraSceneTouchEvent.set(VERTICES_TOUCH_TMP[VERTEX_INDEX_X], VERTICES_TOUCH_TMP[VERTEX_INDEX_Y]);
		}
	}

	public void convertSurfaceToSceneTouchEvent(final TouchEvent pSurfaceTouchEvent, final int pSurfaceWidth, final int pSurfaceHeight) {
		final float relativeX;
		final float relativeY;

		final float rotation = this.mRotation;
		if(rotation == 0) {
			relativeX = pSurfaceTouchEvent.getX() / pSurfaceWidth;
			relativeY = pSurfaceTouchEvent.getY() / pSurfaceHeight;
		} else if(rotation == 180) {
			relativeX = 1 - (pSurfaceTouchEvent.getX() / pSurfaceWidth);
			relativeY = 1 - (pSurfaceTouchEvent.getY() / pSurfaceHeight);
		} else {
			VERTICES_TOUCH_TMP[VERTEX_INDEX_X] = pSurfaceTouchEvent.getX();
			VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] = pSurfaceTouchEvent.getY();

			MathUtils.rotateAroundCenter(VERTICES_TOUCH_TMP, rotation, pSurfaceWidth / 2, pSurfaceHeight / 2);

			relativeX = VERTICES_TOUCH_TMP[VERTEX_INDEX_X] / pSurfaceWidth;
			relativeY = VERTICES_TOUCH_TMP[VERTEX_INDEX_Y] / pSurfaceHeight;
		}

		this.convertAxisAlignedSurfaceToSceneTouchEvent(pSurfaceTouchEvent, relativeX, relativeY);
	}

	private void convertAxisAlignedSurfaceToSceneTouchEvent(final TouchEvent pSurfaceTouchEvent, final float pRelativeX, final float pRelativeY) {
		final float minX = this.getMinX();
		final float maxX = this.getMaxX();
		final float minY = this.getMinY();
		final float maxY = this.getMaxY();

		final float x = minX + pRelativeX * (maxX - minX);
		final float y = minY + pRelativeY * (maxY - minY);

		pSurfaceTouchEvent.set(x, y);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
