package org.anddev.andengine.engine.camera;

/**
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

	private float mBoundMinX;
	private float mBoundMaxX;
	private float mBoundMinY;
	private float mBoundMaxY;
	private boolean mBoundsEnabled;

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

	public void setBoundsEnabled(final boolean pBoundsEnabled) {
		this.mBoundsEnabled = pBoundsEnabled;
	}

	public void setBounds(final float pBoundMinX, final float pBoundMaxX, final float pBoundMinY, final float pBoundMaxY) {
		this.mBoundMinX = pBoundMinX;
		this.mBoundMaxX = pBoundMaxX;
		this.mBoundMinY = pBoundMinY;
		this.mBoundMaxY = pBoundMaxY;
	}

	@Override
	public void setCenter(final float pCenterX, final float pCenterY) {
		super.setCenter(pCenterX, pCenterY);

		if(this.mBoundsEnabled) {
			final float newCenterX = this.getCenterX();
			final float newCenterY = this.getCenterY();

			final float minXBoundExceededAmount = this.mBoundMinX - this.getMinX();
			final boolean minXBoundExceeded = minXBoundExceededAmount > 0;

			final float maxXBoundExceededAmount = this.getMaxX() - this.mBoundMaxX;
			final boolean maxXBoundExceeded = maxXBoundExceededAmount > 0;

			final float minYBoundExceededAmount = this.mBoundMinY - this.getMinY();
			final boolean minYBoundExceeded = minYBoundExceededAmount > 0;

			final float maxYBoundExceededAmount = this.getMaxY() - this.mBoundMaxY;
			final boolean maxYBoundExceeded = maxYBoundExceededAmount > 0;

			if(minXBoundExceeded || maxXBoundExceeded || minYBoundExceeded || maxYBoundExceeded) {
				final float boundedCenterX;
				final float boundedCenterY;

				if(minXBoundExceeded) {
					if(maxXBoundExceeded) {
						/* Both X exceeded. */
						boundedCenterX = newCenterX - maxXBoundExceededAmount + minXBoundExceededAmount;
					} else {
						/* Only min X exceeded. */
						boundedCenterX = newCenterX + minXBoundExceededAmount;
					}
				} else {
					if(maxXBoundExceeded) {
						/* Only max X exceeded. */
						boundedCenterX = newCenterX - maxXBoundExceededAmount;
					} else {
						/* Nothing exceeded. */
						boundedCenterX = newCenterX;
					}
				}

				if(minYBoundExceeded) {
					if(maxYBoundExceeded) {
						/* Both Y exceeded. */
						boundedCenterY = newCenterY - maxYBoundExceededAmount + minYBoundExceededAmount;
					} else {
						/* Only min Y exceeded. */
						boundedCenterY = newCenterY + minYBoundExceededAmount;
					}
				} else {
					if(maxYBoundExceeded) {
						/* Only max Y exceeded. */
						boundedCenterY = newCenterY - maxYBoundExceededAmount;
					} else {
						/* Nothing exceeded. */
						boundedCenterY = newCenterY;
					}
				}

				super.setCenter(boundedCenterX, boundedCenterY);
			}
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
