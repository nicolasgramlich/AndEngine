package org.anddev.andengine.opengl.font;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:29:21 - 03.04.2010
 */
class Size {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mWidth;
	private int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Size() {
		this(0, 0);
	}

	public Size(final int pWidth, final int pHeight) {
		this.setWidth(pWidth);
		this.setHeight(pHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setWidth(final int width) {
		this.mWidth = width;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public void setHeight(final int height) {
		this.mHeight = height;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public void set(final int pWidth, final int pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
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