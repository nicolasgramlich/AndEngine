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
			final int dX = this.getTileColumn(pIndex - 1) - this.getTileColumn(pIndex);
			final int dY = this.getTileRow(pIndex - 1) - this.getTileRow(pIndex);
			return Direction.fromDelta(dX, dY);
		}
	}

	public Direction getDirectionToNextStep(final int pIndex) {
		if(pIndex == this.getLength() - 1) {
			return null;
		} else {
			final int dX = this.getTileColumn(pIndex + 1) - this.getTileColumn(pIndex);
			final int dY = this.getTileRow(pIndex + 1) - this.getTileRow(pIndex);
			return Direction.fromDelta(dX, dY);
		}
	}

	public int getTileColumn(final int pIndex) {
		return this.getStep(pIndex).getTileColumn();
	}

	public int getTileRow(final int pIndex) {
		return this.getStep(pIndex).getTileRow();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void append(final int pTileColumn, final int pTileRow) {
		this.append(new Step(pTileColumn, pTileRow));
	}

	public void append(final Step pStep) {
		this.mSteps.add(pStep);
	}

	public void prepend(final int pTileColumn, final int pTileRow) {
		this.prepend(new Step(pTileColumn, pTileRow));
	}

	public void prepend(final Step pStep) {
		this.mSteps.add(0, pStep);
	}

	public boolean contains(final int pTileColumn, final int pTileRow) {
		final ArrayList<Step> steps = this.mSteps;
		for(int i = steps.size() - 1; i >= 0; i--) {
			final Step step = steps.get(i);
			if(step.getTileColumn() == pTileColumn && step.getTileRow() == pTileRow) {
				return true;
			}
		}
		return false;
	}

	public int getFromTileRow() {
		return this.getTileRow(0);
	}

	public int getFromTileColumn() {
		return this.getTileColumn(0);
	}

	public int getToTileRow() {
		return this.getTileRow(this.mSteps.size() - 1);
	}

	public int getToTileColumn() {
		return this.getTileColumn(this.mSteps.size() - 1);
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

		private final int mTileColumn;
		private final int mTileRow;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Step(final int pTileColumn, final int pTileRow) {
			this.mTileColumn = pTileColumn;
			this.mTileRow = pTileRow;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getTileColumn() {
			return this.mTileColumn;
		}

		public int getTileRow() {
			return this.mTileRow;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public int hashCode() {
			return this.mTileColumn << 16 + this.mTileRow;
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
			if(this.mTileColumn != other.mTileColumn) {
				return false;
			}
			if(this.mTileRow != other.mTileRow) {
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
