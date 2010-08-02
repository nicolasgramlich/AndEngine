package org.anddev.andengine.util;

import org.anddev.progressmonitor.IProgressListener;

/**
 * @author Nicolas Gramlich
 * @since 20:52:44 - 03.01.2010
 */
public interface ProgressCallable<T> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Computes a result, or throws an exception if unable to do so.
	 * @param pProgressListener
	 * @return computed result
	 * @throws Exception if unable to compute a result
	 */
	public T call(final IProgressListener pProgressListener) throws Exception;
}