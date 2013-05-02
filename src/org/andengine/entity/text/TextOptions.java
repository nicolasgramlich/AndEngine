package org.andengine.entity.text;

import org.andengine.util.adt.align.HorizontalAlign;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:37:02 - 29.03.2012
 */
public class TextOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	/* package */ AutoWrap mAutoWrap;
	/* package */ float mAutoWrapWidth;
	/* package */ float mLeading;
	/* package */ HorizontalAlign mHorizontalAlign;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextOptions() {
		this(AutoWrap.NONE, 0, HorizontalAlign.LEFT, Text.LEADING_DEFAULT);
	}

	public TextOptions(final HorizontalAlign pHorizontalAlign) {
		this(AutoWrap.NONE, 0, pHorizontalAlign, Text.LEADING_DEFAULT);
	}

	public TextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth) {
		this(pAutoWrap, pAutoWrapWidth, HorizontalAlign.LEFT, Text.LEADING_DEFAULT);
	}

	public TextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign) {
		this(pAutoWrap, pAutoWrapWidth, pHorizontalAlign, Text.LEADING_DEFAULT);
	}

	public TextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign, final float pLeading) {
		this.mAutoWrap = pAutoWrap;
		this.mAutoWrapWidth = pAutoWrapWidth;
		this.mHorizontalAlign = pHorizontalAlign;
		this.mLeading = pLeading;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public AutoWrap getAutoWrap() {
		return this.mAutoWrap;
	}

	public void setAutoWrap(final AutoWrap pAutoWrap) {
		this.mAutoWrap = pAutoWrap;
	}

	public float getAutoWrapWidth() {
		return this.mAutoWrapWidth;
	}

	public void setAutoWrapWidth(final float pAutoWrapWidth) {
		this.mAutoWrapWidth = pAutoWrapWidth;
	}

	public float getLeading() {
		return this.mLeading;
	}

	public void setLeading(final float pLeading) {
		this.mLeading = pLeading;
	}

	public HorizontalAlign getHorizontalAlign() {
		return this.mHorizontalAlign;
	}

	public void setHorizontalAlign(final HorizontalAlign pHorizontalAlign) {
		this.mHorizontalAlign = pHorizontalAlign;
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