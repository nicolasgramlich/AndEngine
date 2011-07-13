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
		super.setCenter(this.determineBoundedX(), this.determineBoundedY());
	}

	private float determineBoundedX() {
		if(this.mBoundsWidth < this.getWidth()) {
			return this.mBoundsCenterX;
		} else {
			final float currentCenterX = this.getCenterX();

			final float minXBoundExceededAmount = this.mBoundsMinX - this.getMinX();
			final boolean minXBoundExceeded = minXBoundExceededAmount > 0;

			final float maxXBoundExceededAmount = this.getMaxX() - this.mBoundsMaxX;
			final boolean maxXBoundExceeded = maxXBoundExceededAmount > 0;

			if(minXBoundExceeded) {
				if(maxXBoundExceeded) {
					/* Min and max X exceeded. */
					return currentCenterX - maxXBoundExceededAmount + minXBoundExceededAmount;
				} else {
					/* Only min X exceeded. */
					return currentCenterX + minXBoundExceededAmount;
				}
			} else {
				if(maxXBoundExceeded) {
					/* Only max X exceeded. */
					return currentCenterX - maxXBoundExceededAmount;
				} else {
					/* Nothing exceeded. */
					return currentCenterX;
				}
			}
		}
	}

	private float determineBoundedY() {
		if(this.mBoundsHeight < this.getHeight()) {
			return this.mBoundsCenterY;
		} else {
			final float currentCenterY = this.getCenterY();

			final float minYBoundExceededAmount = this.mBoundsMinY - this.getMinY();
			final boolean minYBoundExceeded = minYBoundExceededAmount > 0;

			final float maxYBoundExceededAmount = this.getMaxY() - this.mBoundsMaxY;
			final boolean maxYBoundExceeded = maxYBoundExceededAmount > 0;

			if(minYBoundExceeded) {
				if(maxYBoundExceeded) {
					/* Min and max Y exceeded. */
					return currentCenterY - maxYBoundExceededAmount + minYBoundExceededAmount;
				} else {
					/* Only min Y exceeded. */
					return currentCenterY + minYBoundExceededAmount;
				}
			} else {
				if(maxYBoundExceeded) {
					/* Only max Y exceeded. */
					return currentCenterY - maxYBoundExceededAmount;
				} else {
					/* Nothing exceeded. */
					return currentCenterY;
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
