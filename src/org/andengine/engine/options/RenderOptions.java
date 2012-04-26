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
	
	private boolean mARGB8888 = false;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isARGB8888(){
		return this.mARGB8888;
	}
	
	public void setARGB8888(final boolean pARGB){
		this.mARGB8888 = pARGB;
	}
	
	public boolean isMultiSampling() {
		return this.mMultiSampling;
	}

	public void setMultiSampling(final boolean pMultiSampling) {
		this.mMultiSampling = pMultiSampling;
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
