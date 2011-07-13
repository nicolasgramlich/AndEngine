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

	private float mWidth;
	private float mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Size() {
		this(0, 0);
	}

	public Size(final float pWidth, final float pHeight) {
		this.setWidth(pWidth);
		this.setHeight(pHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setWidth(final float width) {
		this.mWidth = width;
	}

	public float getWidth() {
		return this.mWidth;
	}

	public void setHeight(final float height) {
		this.mHeight = height;
	}

	public float getHeight() {
		return this.mHeight;
	}

	public void set(final int pWidth, final int pHeight) {
		this.setWidth(pWidth);
		this.setHeight(pHeight);
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