package org.andengine.util.call;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:52:44 - 03.01.2010
 */
public interface Callable<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Computes a result, or throws an exception if unable to do so.
	 *
	 * @return the computed result.
	 * @throws Exception if unable to compute a result.
	 */
	public T call() throws Exception;
}