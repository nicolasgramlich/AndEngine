package org.andengine.util.progress;

/**
 * Listens to changes in progress, for example in a loading screen, and runs
 * {@link #onProgressChanged(int)}. Progress is defined as an integer between 0
 * and 100;
 * 
 * (c) 2010 Nicolas Gramlich <br>
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
	 * Should be when progress has been made, for example during a loading screen.
	 * @param pProgress
	 *            between 0 and 100.
	 */
	public void onProgressChanged(final int pProgress);
}