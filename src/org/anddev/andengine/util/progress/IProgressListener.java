package org.anddev.andengine.util.progress;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:07:35 - 09.07.2009
 */
public interface IProgressListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pProgress between 0 and 100.
	 */
	public void onProgressChanged(final int pProgress);
}