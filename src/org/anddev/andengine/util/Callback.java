package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 09:40:55 - 14.12.2009S
 */
public interface Callback<T> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onCallback(final T pCallbackValue);
}