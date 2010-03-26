package org.anddev.andengine.opengl.view.camera;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.StaticEntity;
import org.anddev.andengine.physics.collision.CollisionChecker;

import android.opengl.GLU;
import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 10:24:18 - 25.03.2010
 */
public class Camera {
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
	
	private int mSurfaceWidth = 1; // 1 to prevent accidental DIV/0
	private int mSurfaceHeight = 1; // 1 to prevent accidental DIV/0

	// ===========================================================
	// Constructors
	// ===========================================================

	public Camera(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.set(pX, pX + pWidth, pY, pY + pHeight);
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
		return this.mMaxX - this.mMinX;
	}

	public float getHeight() {
		return this.mMaxY - this.mMinY;
	}

	public float getCenterX() {
		return this.mMinX + (this.mMaxX - this.mMinX) / 2;
	}

	public float getCenterY() {
		return this.mMinY + (this.mMaxY - this.mMinY) / 2;
	}

	public void setCenter(final float pCenterX, final float pCenterY) {
		final float dX = pCenterX - this.getCenterX();
		final float dY = pCenterY - this.getCenterY();

		this.mMinX += dX;
		this.mMaxX += dX;
		this.mMinY += dY;
		this.mMaxY += dY;
	}

	private void set(final float pMinX, final float pMaxX, final float pMinY, final float pMaxY) {
		assert(pMinX < pMaxX);
		assert(pMinY < pMaxY);
		
		this.mMinX = pMinX;
		this.mMaxX = pMaxX;
		this.mMinY = pMinY;
		this.mMaxY = pMaxY;
	}

	public void setSurfaceSize(final int pSurfaceWidth, final int pSurfaceHeight) {
		this.mSurfaceWidth = pSurfaceWidth;
		this.mSurfaceHeight = pSurfaceHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEntityVisible(final StaticEntity pStaticEntity) {
		final float otherLeft = pStaticEntity.getX();
		final float otherTop = pStaticEntity.getY();
		final float otherRight = pStaticEntity.getWidth()+ otherLeft;
		final float otherBottom = pStaticEntity.getHeight() + otherTop;

		return CollisionChecker.checkAxisAlignedBoxCollision(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, otherLeft, otherTop, otherRight, otherBottom);
	}

	public void onApply(final GL10 pGL) {
		GLU.gluOrtho2D(pGL, this.mMinX, this.mMaxX, this.mMaxY, this.mMinY);
	}

	public void surfaceToSceneMotionEvent(final MotionEvent pMotionEvent) {
		final float x = this.mMinX + (pMotionEvent.getX() / this.mSurfaceWidth) * (this.mMaxX - this.mMinX);
		final float y = this.mMinY + (pMotionEvent.getY() / this.mSurfaceHeight) * (this.mMaxY - this.mMinY);
		pMotionEvent.setLocation(x, y);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
