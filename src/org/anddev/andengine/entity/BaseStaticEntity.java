package org.anddev.andengine.entity;


/**
 * @author Nicolas Gramlich
 * @since 12:06:43 - 11.03.2010
 */
public abstract class BaseStaticEntity extends BaseEntity implements IStaticEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mBaseX;
	protected final float mBaseY;

	protected float mX;
	protected float mY;

	protected float mOffsetX = 0;
	protected float mOffsetY = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseStaticEntity(final float pX, final float pY) {
		this.mBaseX = pX;
		this.mBaseY = pY;

		this.mX = pX;
		this.mY = pY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getX() {
		return this.mX;
	}

	@Override
	public float getY() {
		return this.mY;
	}

	@Override
	public float getBaseX() {
		return this.mBaseX;
	}

	@Override
	public float getBaseY() {
		return this.mBaseY;
	}

	@Override
	public float getOffsetX() {
		return this.mOffsetX;
	}

	@Override
	public float getOffsetY() {
		return this.mOffsetY;
	}

	@Override
	public void setOffsetX(final float pOffsetX) {
		this.mOffsetX = pOffsetX;
		this.onPositionChanged();
	}

	@Override
	public void setOffsetY(final float pOffsetY) {
		this.mOffsetY = pOffsetY;
		this.onPositionChanged();
	}

	@Override
	public void setOffset(final float pOffsetX, final float pOffsetY) {
		this.mOffsetX = pOffsetX;
		this.mOffsetY = pOffsetY;
		this.onPositionChanged();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onPositionChanged(){
		
	}

	@Override
	public void reset() {
		super.reset();

		this.mOffsetX = 0;
		this.mOffsetY = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
