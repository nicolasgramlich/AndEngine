package org.andengine.extension.scripting.generator.util.adt;

import org.andengine.extension.scripting.generator.util.adt.io.IFormatter;

import de.hunsicker.jalopy.Jalopy;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:25:56 - 21.03.2012
 */
public enum JavaFormatter implements IFormatter {
	// ===========================================================
	// Elements
	// ===========================================================

	JALOPY() {
		@Override
		public String format(final String pString) throws FormatterException {
			final Jalopy jalopy = new Jalopy();
			jalopy.setInput(pString, "<inline>");

			final StringBuffer output = new StringBuffer();
			jalopy.setOutput(output);

			final boolean success = jalopy.format();

			if(success) {
				return output.toString();
			} else {
				throw new FormatterException("Could not format:\n###########################\n" + pString + "\n###########################");
			}
		}
	};

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
