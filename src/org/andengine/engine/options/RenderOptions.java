package org.andengine.engine.options;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:01:40 - 02.07.2010
 */
public class RenderOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mMultiSampling = false;
	private boolean mDithering = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isMultiSampling() {
		return this.mMultiSampling;
	}

	public void setMultiSampling(final boolean pMultiSampling) {
		this.mMultiSampling = pMultiSampling;
	}

	public boolean isDithering() {
		return this.mDithering;
	}

	public void setDithering(final boolean pDithering) {
		this.mDithering = pDithering;
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
