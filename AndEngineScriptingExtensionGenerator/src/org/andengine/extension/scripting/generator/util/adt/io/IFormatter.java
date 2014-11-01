package org.andengine.extension.scripting.generator.util.adt.io;

import org.andengine.extension.scripting.generator.util.adt.FormatterException;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:20:56 - 21.03.2012
 */
public interface IFormatter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public String format(final String pString) throws FormatterException;
}
