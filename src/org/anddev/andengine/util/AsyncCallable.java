package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 15:00:30 - 14.05.2010
 * @param <T>
 */
public interface AsyncCallable<T> {
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
	public void call(final Callback<T> pCallback, final Callback<Exception> pExceptionCallback);
}