package org.anddev.andengine.entity;

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

	protected float mX = 0;
	protected float mY = 0;

	protected float mOffsetX = 0;
	protected float mOffsetY = 0;

	protected final int mWidth;
	protected final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StaticEntity(final float pX, final float pY, final int pWidth, final int pHeight) {
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

	public void setOffsetX(final float pOffsetX) {
		this.mOffsetX = pOffsetX;
	}

	public void setOffsetY(final float pOffsetY) {
		this.mOffsetY = pOffsetY;
	}

	public void setOffset(final float pOffsetX, final float pOffsetY) {
		this.mOffsetX = pOffsetX;
		this.mOffsetY = pOffsetY;
		onPositionChanged();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onPositionChanged();

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean checkCollision(final StaticEntity pOther) {
		final float left = this.mX;
		final float top = this.mY;
		final float right = this.mWidth + left;
		final float bottom = this.mHeight + top;
		
		final float otherLeft = pOther.mX;
		final float otherTop = pOther.mY;		
		final float otherRight = pOther.mWidth + otherLeft;
		final float otherBottom = pOther.mHeight + otherTop;
		
		return (left < otherRight &&
				otherLeft < right &&
				otherLeft < otherBottom &&
				otherTop < bottom);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
