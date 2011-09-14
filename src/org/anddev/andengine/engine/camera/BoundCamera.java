package org.anddev.andengine.engine.camera;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:55:54 - 27.07.2010
 */
public class BoundCamera extends Camera {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected boolean mBoundsEnabled;

	private float mBoundsMinX;
	private float mBoundsMaxX;
	private float mBoundsMinY;
	private float mBoundsMaxY;

	private float mBoundsCenterX;
	private float mBoundsCenterY;

	private float mBoundsWidth;
	private float mBoundsHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BoundCamera(final float pX, final float pY, final float pWidth, final float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}

	public BoundCamera(final float pX, final float pY, final float pWidth, final float pHeight, final float pBoundMinX, final float pBoundMaxX, final float pBoundMinY, final float pBoundMaxY) {
		super(pX, pY, pWidth, pHeight);
		this.setBounds(pBoundMinX, pBoundMaxX, pBoundMinY, pBoundMaxY);
		this.mBoundsEnabled = true;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isBoundsEnabled() {
		return this.mBoundsEnabled;
	}

	public void setBoundsEnabled(final boolean pBoundsEnabled) {
		this.mBoundsEnabled = pBoundsEnabled;
	}

	public void setBounds(final float pBoundMinX, final float pBoundMaxX, final float pBoundMinY, final float pBoundMaxY) {
		this.mBoundsMinX = pBoundMinX;
		this.mBoundsMaxX = pBoundMaxX;
		this.mBoundsMinY = pBoundMinY;
		this.mBoundsMaxY = pBoundMaxY;

		this.mBoundsWidth = this.mBoundsMaxX - this.mBoundsMinX;
		this.mBoundsHeight = this.mBoundsMaxY - this.mBoundsMinY;

		this.mBoundsCenterX = this.mBoundsMinX + this.mBoundsWidth * 0.5f;
		this.mBoundsCenterY = this.mBoundsMinY + this.mBoundsHeight * 0.5f;
	}

	public float getBoundsWidth() {
		return this.mBoundsWidth;
	}

	public float getBoundsHeight() {
		return this.mBoundsHeight;
	}

	@Override
	public void setCenter(final float pCenterX, final float pCenterY) {
		super.setCenter(pCenterX, pCenterY);

		if(this.mBoundsEnabled) {
			this.ensureInBounds();
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	protected void ensureInBounds() {
		final float centerX;
		if(this.mBoundsWidth < this.getWidth()) {
			centerX = this.mBoundsCenterX;
		} else {
			centerX = getBoundedX(this.getCenterX()); 
		}
		final float centerY;
		if(this.mBoundsHeight < this.getHeight()) {
			centerY = this.mBoundsCenterY;
		} else {
			centerY = getBoundedY(this.getCenterY()); 
		}
		super.setCenter(centerX, centerY);
	}

	protected float getBoundedX(final float pX) {
		final float minXBoundExceededAmount = this.mBoundsMinX - this.getXMin();
		final boolean minXBoundExceeded = minXBoundExceededAmount > 0;

		final float maxXBoundExceededAmount = this.getXMax() - this.mBoundsMaxX;
		final boolean maxXBoundExceeded = maxXBoundExceededAmount > 0;

		if(minXBoundExceeded) {
			if(maxXBoundExceeded) {
				/* Min and max X exceeded. */
				return pX - maxXBoundExceededAmount + minXBoundExceededAmount;
			} else {
				/* Only min X exceeded. */
				return pX + minXBoundExceededAmount;
			}
		} else {
			if(maxXBoundExceeded) {
				/* Only max X exceeded. */
				return pX - maxXBoundExceededAmount;
			} else {
				/* Nothing exceeded. */
				return pX;
			}
		}
	}

	protected float getBoundedY(final float pY) {
		final float minYBoundExceededAmount = this.mBoundsMinY - this.getYMin();
		final boolean minYBoundExceeded = minYBoundExceededAmount > 0;

		final float maxYBoundExceededAmount = this.getYMax() - this.mBoundsMaxY;
		final boolean maxYBoundExceeded = maxYBoundExceededAmount > 0;

		if(minYBoundExceeded) {
			if(maxYBoundExceeded) {
				/* Min and max Y exceeded. */
				return pY - maxYBoundExceededAmount + minYBoundExceededAmount;
			} else {
				/* Only min Y exceeded. */
				return pY + minYBoundExceededAmount;
			}
		} else {
			if(maxYBoundExceeded) {
				/* Only max Y exceeded. */
				return pY - maxYBoundExceededAmount;
			} else {
				/* Nothing exceeded. */
				return pY;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
