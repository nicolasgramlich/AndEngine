package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 23:40:42 - 27.12.2010
 */
public interface ParameterCallable<T> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void call(final T pParameter);
}