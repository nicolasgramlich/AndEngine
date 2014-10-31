package org.andengine.extension.svg.adt;

import android.graphics.Picture;
import android.graphics.RectF;

/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:01:21 - 21.05.2011
 */
public class SVG {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Picture mPicture;

	/** These are the bounds for the SVG specified as a hidden "bounds" layer in the SVG. */
	private final RectF mBounds;

	/** These are the estimated bounds of the SVG computed from the SVG elements while parsing.
	 * Note that this could be null if there was a failure to compute limits (i.e. an empty SVG). */
	private final RectF mComputedBounds;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pPicture the parsed picture object.
	 * @param pBounds the bounds computed from the "bounds" layer in the SVG.
	 * @param pComputedBounds
	 */
	public SVG(final Picture pPicture, final RectF pBounds, final RectF pComputedBounds) {
		this.mPicture = pPicture;
		this.mBounds = pBounds;
		this.mComputedBounds = pComputedBounds;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Picture getPicture() {
		return this.mPicture;
	}

	/**
	 * Gets the bounding rectangle for the SVG, specified as a hidden "bounds" layer, if one was specified.
	 * @return rectangle representing the bounds.
	 */
	public RectF getBounds() {
		return this.mBounds;
	}

	/**
	 * Gets the computed bounding rectangle for the SVG that was computed upon parsing.
	 * It may not be entirely accurate for certain curves or transformations, but is often better than nothing.
	 * @return rectangle representing the computed bounds.
	 */
	public RectF getComputedBounds() {
		return this.mComputedBounds;
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
