package org.anddev.andengine.util.path;

import java.util.ArrayList;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:00:24 - 16.08.2010
 */
public class Path {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<Step> mSteps = new ArrayList<Step>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getLength() {
		return this.mSteps.size();
	}

	public Step getStep(final int pIndex) {
		return this.mSteps.get(pIndex);
	}

	public Direction getDirectionToPreviousStep(final int pIndex) {
		if(pIndex == 0) {
			return null;
		} else {
			final int dX = this.getX(pIndex - 1) - this.getX(pIndex);
			final int dY = this.getY(pIndex - 1) - this.getY(pIndex);
			return Direction.fromDelta(dX, dY);
		}
	}

	public Direction getDirectionToNextStep(final int pIndex) {
		if(pIndex == this.getLength() - 1) {
			return null;
		} else {
			final int dX = this.getX(pIndex + 1) - this.getX(pIndex);
			final int dY = this.getY(pIndex + 1) - this.getY(pIndex);
			return Direction.fromDelta(dX, dY);
		}
	}

	public int getX(final int pIndex) {
		return this.getStep(pIndex).getX();
	}

	public int getY(final int pIndex) {
		return this.getStep(pIndex).getY();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void append(final int pX, final int pY) {
		this.append(new Step(pX, pY));
	}

	public void append(final Step pStep) {
		this.mSteps.add(pStep);
	}

	public void prepend(final int pX, final int pY) {
		this.prepend(new Step(pX, pY));
	}

	public void prepend(final Step pStep) {
		this.mSteps.add(0, pStep);
	}

	public boolean contains(final int pX, final int pY) {
		final ArrayList<Step> steps = this.mSteps;
		for(int i = steps.size() - 1; i >= 0; i--) {
			final Step step = steps.get(i);
			if(step.getX() == pX && step.getY() == pY) {
				return true;
			}
		}
		return false;
	}

	public int getFromY() {
		return this.getY(0);
	}

	public int getFromX() {
		return this.getX(0);
	}

	public int getToY() {
		return this.getY(this.mSteps.size() - 1);
	}

	public int getToX() {
		return this.getX(this.mSteps.size() - 1);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class Step {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mX;
		private final int mY;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Step(final int pX, final int pY) {
			this.mX = pX;
			this.mY = pY;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getX() {
			return this.mX;
		}

		public int getY() {
			return this.mY;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public int hashCode() {
			return this.mX << 16 + this.mY;
		}

		@Override
		public boolean equals(final Object pOther) {
			if(this == pOther) {
				return true;
			}
			if(pOther == null) {
				return false;
			}
			if(this.getClass() != pOther.getClass()) {
				return false;
			}
			final Step other = (Step) pOther;
			if(this.mX != other.mX) {
				return false;
			}
			if(this.mY != other.mY) {
				return false;
			}
			return true;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

}
