package org.anddev.andengine.opengl.view.camera;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.StaticEntity;
import org.anddev.andengine.physics.collision.CollisionChecker;

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

	public float relativeToAbsoluteX(final float pRelativeX) {
		return this.mMinX + pRelativeX * (this.mMaxX - this.mMinX);
	}
	
	public float relativeToAbsoluteY(final float pRelativeY) {
		return this.mMinY + pRelativeY * (this.mMaxY - this.mMinY);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		/* Nothing. */
	}

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
		pGL.glMatrixMode(GL10.GL_PROJECTION);
		pGL.glLoadIdentity();
		GLU.gluOrtho2D(pGL, this.mMinX, this.mMaxX, this.mMaxY, this.mMinY);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
