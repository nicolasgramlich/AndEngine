package org.anddev.andengine.entity;

import org.anddev.andengine.physics.collision.CollisionChecker;

/**
 * @author Nicolas Gramlich
 * @since 12:06:43 - 11.03.2010
 */
public abstract class StaticEntity extends BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mInitialX;
	protected final float mInitialY;
	
	protected float mX;
	protected float mY;

	protected float mOffsetX = 0;
	protected float mOffsetY = 0;

	protected int mWidth;
	protected int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StaticEntity(final float pX, final float pY, final int pWidth, final int pHeight) {
		this.mInitialX = pX;
		this.mInitialY = pY;
		this.mX = pX;
		this.mY = pY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getX() {
		return this.mX;
	}

	public float getY() {
		return this.mY;
	}
	
	public void setPosition(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;
	}
	
	public void setPosition(final StaticEntity pOtherStaticEntity) {
		this.mX = pOtherStaticEntity.getX();
		this.mY = pOtherStaticEntity.getY();
	}
	
	public float getInitialX() {
		return this.mInitialX;
	}
	
	public float getInitialY() {
		return this.mInitialY;
	}
	
	public void setInitialPosition() {
		this.mX = this.mInitialX;
		this.mY = this.mInitialY;
		this.onPositionChanged();
	}

	public float getOffsetX() {
		return this.mOffsetX;
	}

	public float getOffsetY() {
		return this.mOffsetY;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}
	
	public void setWidth(final int pWidth) {
		this.mWidth = pWidth;
		this.onPositionChanged();
	}
	
	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.onPositionChanged();
	}

	public void setOffsetX(final float pOffsetX) {
		this.mOffsetX = pOffsetX;
	}

	public void setOffsetY(final float pOffsetY) {
		this.mOffsetY = pOffsetY;
	}

	public void setOffset(final float pOffsetX, final float pOffsetY) {
		this.mOffsetX = pOffsetX;
		this.mOffsetY = pOffsetY;
		this.onPositionChanged();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onPositionChanged();

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean collidesWith(final StaticEntity pOther) {
		final float left = this.mX;
		final float top = this.mY;
		final float right = this.mWidth + left;
		final float bottom = this.mHeight + top;
		
		final float otherLeft = pOther.mX;
		final float otherTop = pOther.mY;		
		final float otherRight = pOther.mWidth + otherLeft;
		final float otherBottom = pOther.mHeight + otherTop;
		
		return CollisionChecker.checkAxisAlignedBoxCollision(left, top, right, bottom, otherLeft, otherTop, otherRight, otherBottom);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
