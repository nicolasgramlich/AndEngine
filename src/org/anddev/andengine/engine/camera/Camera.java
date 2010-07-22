package org.anddev.andengine.engine.camera;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.collision.BaseCollisionChecker;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.util.GLHelper;

import android.opengl.GLU;

/**
 * @author Nicolas Gramlich
 * @since 10:24:18 - 25.03.2010
 */
public class Camera implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mMinX;
	private float mMaxX;
	private float mMinY;
	private float mMaxY;

	private HUD mHUD;

	private boolean mFlipped;

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

	public float getWidth() {
		return (this.mMaxX - this.mMinX);
	}

	public float getHeight() {
		return (this.mMaxY - this.mMinY);
	}

	public float getCenterX() {
		return this.mMinX + (this.mMaxX - this.mMinX) * 0.5f;
	}

	public float getCenterY() {
		return this.mMinY + (this.mMaxY - this.mMinY) * 0.5f;
	}

	public void setCenter(final float pCenterX, final float pCenterY) {
		final float dX = pCenterX - this.getCenterX();
		final float dY = pCenterY - this.getCenterY();

		this.mMinX += dX;
		this.mMaxX += dX;
		this.mMinY += dY;
		this.mMaxY += dY;
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mHUD != null) {
			this.mHUD.onUpdate(pSecondsElapsed);
		}
	}
	
	@Override
	public void reset() {
		
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void flip() {
		this.mFlipped = !this.mFlipped;
	}

	public void onDrawHUD(final GL10 pGL) {
		if(this.mHUD != null) {
			this.mHUD.onDraw(pGL, this);
		}
	}

	public boolean isRectangularShapeVisible(final RectangularShape pRectangularShape) {
		final float otherLeft = pRectangularShape.getX();
		final float otherTop = pRectangularShape.getY();
		final float otherRight = pRectangularShape.getWidthScaled() + otherLeft;
		final float otherBottom = pRectangularShape.getHeightScaled() + otherTop;

		return BaseCollisionChecker.checkAxisAlignedRectangleCollision(this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY(), otherLeft, otherTop, otherRight, otherBottom); // TODO Should also use RectangularShapeCollisionChecker
	}

	public void onApplyMatrix(final GL10 pGL) {
		GLHelper.setProjectionIdentityMatrix(pGL);

		GLU.gluOrtho2D(pGL, this.getMinX(), this.getMaxX(), this.getMaxY(), this.getMinY());

		if(this.mFlipped) {
			this.rotateHalfAround(pGL, this.getCenterX(), this.getCenterY());
		}
	}

	public void onApplyPositionIndependentMatrix(final GL10 pGL) {
		GLHelper.setProjectionIdentityMatrix(pGL);

		final float width = this.mMaxX - this.mMinX;
		final float height = this.mMaxY - this.mMinY;

		GLU.gluOrtho2D(pGL, 0, width, height, 0);

		if(this.mFlipped) {
			this.rotateHalfAround(pGL, width * 0.5f, height * 0.5f);
		}
	}

	private void rotateHalfAround(final GL10 pGL, final float pCenterX, final float pCenterY) {
		pGL.glTranslatef(pCenterX, pCenterY, 0);
		pGL.glRotatef(180, 0, 0, 1);
		pGL.glTranslatef(-pCenterX, -pCenterY, 0);
	}

	public void convertSceneToHUDTouchEvent(final TouchEvent pSceneTouchEvent) {
		final float x = pSceneTouchEvent.getX() - this.getMinX();
		final float y = pSceneTouchEvent.getY() - this.getMinY();
		pSceneTouchEvent.set(x, y);
	}

	public void convertHUDToSceneTouchEvent(final TouchEvent pHUDTouchEvent) {
		final float x = pHUDTouchEvent.getX() + this.getMinX();
		final float y = pHUDTouchEvent.getY() + this.getMinY();
		pHUDTouchEvent.set(x, y);
	}

	public void convertSurfaceToSceneTouchEvent(final TouchEvent pSurfaceTouchEvent, final int pSurfaceWidth, final int pSurfaceHeight) {
		final float relativeX;
		final float relativeY;

		if(this.mFlipped) {
			relativeX = 1 - (pSurfaceTouchEvent.getX() / pSurfaceWidth);
			relativeY = 1 - (pSurfaceTouchEvent.getY() / pSurfaceHeight);
		} else {
			relativeX = pSurfaceTouchEvent.getX() / pSurfaceWidth;
			relativeY = pSurfaceTouchEvent.getY() / pSurfaceHeight;
		}
		
		this.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent, relativeX, relativeY);
	}

	private void convertSurfaceToSceneTouchEvent(final TouchEvent pSurfaceTouchEvent, final float pRelativeX, final float pRelativeY) {
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
