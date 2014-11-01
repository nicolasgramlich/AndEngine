package org.andengine.util.progress;


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

	public static final int PROGRESS_MIN = 0;
	public static final int PROGRESS_MAX = 100;

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pProgress between 0 and 100.
	 */
	public void onProgressChanged(final int pProgress);
}