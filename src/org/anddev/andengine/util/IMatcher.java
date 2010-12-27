package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 12:32:22 - 26.12.2010
 */
public interface IMatcher<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean matches(final T pObject);
}

