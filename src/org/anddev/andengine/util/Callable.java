package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 20:52:44 - 03.01.2010
 */
public interface Callable<T> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Computes a result, or throws an exception if unable to do so.
	 *
	 * @return computed result
	 * @throws Exception if unable to compute a result
	 */
	public T call() throws Exception;
}