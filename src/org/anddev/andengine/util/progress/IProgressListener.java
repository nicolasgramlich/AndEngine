package org.anddev.andengine.util.progress;


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