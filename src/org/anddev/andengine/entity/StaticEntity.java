package org.anddev.andengine.entity;

import org.anddev.andengine.input.touch.ITouchArea;
import org.anddev.andengine.physics.collision.CollisionChecker;

/**
 * @author Nicolas Gramlich
 * @since 12:06:43 - 11.03.2010
 */
public abstract class StaticEntity extends BaseEntity implements ITouchArea {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mInitialX;
	protected final float mInitialY;
	protected final float mInitialWidth;
	protected final float mInitialHeight;

	protected float mX;
	protected float mY;

	protected float mOffsetX = 0;
	protected float mOffsetY = 0;

	protected float mWidth;
	protected float mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StaticEntity(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mInitialX = pX;
		this.mInitialY = pY;
		this.mInitialWidth = pWidth;
		this.mInitialHeight = pHeight;

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

	public float getInitialX() {
		return this.mInitialX;
	}

	public float getInitialY() {
		return this.mInitialY;
	}

	public float getCenterX() {
		return this.mX + this.getWidth() / 2;
	}

	public float getCenterY() {
		return this.mY + this.getHeight() / 2;
	}

	public float getOffsetX() {
		return this.mOffsetX;
	}

	public float getOffsetY() {
		return this.mOffsetY;
	}

	public float getWidth() {
		return this.mWidth;
	}

	public float getHeight() {
		return this.mHeight;
	}

	public float getInitialWidth() {
		return this.mInitialWidth;
	}

	public float getInitialHeight() {
		return this.mInitialHeight;
	}

	public void setOffsetX(final float pOffsetX) {
		this.mOffsetX = pOffsetX;
		this.onPositionChanged();
	}

	public void setOffsetY(final float pOffsetY) {
		this.mOffsetY = pOffsetY;
		this.onPositionChanged();
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

	@Override
	public void reset() {
		super.reset();
		this.mOffsetX = 0;
		this.mOffsetY = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean contains(final float pX, final float pY) {
		return pX >= this.mX
		&& pY >= this.mY
		&& pX <= this.mX + this.mWidth
		&& pY <= this.mY + this.mHeight;
	}

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
