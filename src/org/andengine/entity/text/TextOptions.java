package org.andengine.entity.text;

import org.andengine.util.HorizontalAlign;

/**
 * (c) Zynga 2012
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
		this(AutoWrap.NONE, 0, Text.LEADING_DEFAULT, HorizontalAlign.LEFT);
	}

	public TextOptions(final HorizontalAlign pHorizontalAlign) {
		this(AutoWrap.NONE, 0, Text.LEADING_DEFAULT, pHorizontalAlign);
	}

	public TextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth) {
		this(pAutoWrap, pAutoWrapWidth, Text.LEADING_DEFAULT, HorizontalAlign.LEFT);
	}

	public TextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final float pLeading, final HorizontalAlign pHorizontalAlign) {
		this.mAutoWrap = pAutoWrap;
		this.mAutoWrapWidth = pAutoWrapWidth;
		this.mLeading = pLeading;
		this.mHorizontalAlign = pHorizontalAlign;
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