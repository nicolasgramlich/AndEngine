package org.andengine.util.algorithm;

import org.andengine.util.algorithm.path.Direction;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 22:40:53 - 10.11.2011
 */
public class Spiral {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCenterX;
	private final int mCenterY;
	private final int mStepSize;

	private int mX;
	private int mY;
	private Direction mDirection;
	private int mDirectionSegmentLength;
	private int mDirectionSegmentIndex;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Spiral(final int pCenterX, final int pCenterY) {
		this(pCenterX, pCenterY, 1);
	}

	public Spiral(final int pCenterX, final int pCenterY, final int pStepSize) {
		this.mCenterX = pCenterX;
		this.mCenterY = pCenterY;
		this.mStepSize = pStepSize;

		this.mX = pCenterX;
		this.mY = pCenterY;

		/* First step goes to the right. */
		this.mDirection = Direction.RIGHT;

		/* Length of current segment into direction. */
		this.mDirectionSegmentLength = 1;
		this.mDirectionSegmentIndex = 0;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCenterX() {
		return this.mCenterX;
	}

	public int getCenterY() {
		return this.mCenterY;
	}

	public int getX() {
		return this.mX;
	}

	public int getY() {
		return this.mY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void step() {
		/* Take a step. */
		this.mX += this.mDirection.getDeltaX() * this.mStepSize;
		this.mY += this.mDirection.getDeltaY() * this.mStepSize;

		this.mDirectionSegmentIndex++;

		if (this.mDirectionSegmentIndex == this.mDirectionSegmentLength) {
			/* Finished current segment. */
			this.mDirectionSegmentIndex = 0;

			/* Rotate 90° to the right. */
			this.mDirection = this.mDirection.rotateRight();

			/* When the direction changed to be horizontal, the next segment is going to be one step longer. */
			if (this.mDirection.isHorizontal()) {
				this.mDirectionSegmentLength++;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
