package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 19:01:08 - 03.04.2010
 */
public class StringUtils {
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

	public static int countOccurrences(final String pString, final char pCharacter) {
		int count = 0;

		int lastOccurrence = pString.indexOf(pCharacter, 0);

		while (lastOccurrence != -1) {
			count++;
			lastOccurrence = pString.indexOf(pCharacter, lastOccurrence + 1);
		}

		return count;
	}

	/**
	 * Split a String by a Character, i.e. Split lines by using '\n'.<br/>
	 * Same behavior as <code>String.split("" + pCharacter);</code> .
	 * 
	 * @param pString
	 * @param pCharacter
	 * @return
	 */
	public static String[] split(final String pString, final char pCharacter) {
		return split(pString, pCharacter, null);
	}

	/**
	 * Split a String by a Character, i.e. Split lines by using '\n'.<br/>
	 * Same behavior as <code>String.split("" + pCharacter);</code> .
	 * 
	 * @param pString
	 * @param pCharacter
	 * @param pReuse tries to reuse the String[] if the length is the same as the length needed.
	 * @return
	 */
	public static String[] split(final String pString, final char pCharacter, final String[] pReuse) {
		final int partCount = countOccurrences(pString, pCharacter) + 1;

		final boolean reuseable = pReuse != null && pReuse.length == partCount;
		final String[] out = (reuseable) ? pReuse : new String[partCount];

		if(partCount == 0) {
			out[0] = pString;
		} else {
			int from = 0;
			int to;

			for (int i = 0; i < partCount - 1; i++) {
				to = pString.indexOf(pCharacter, from);
				out[i] = pString.substring(from, to);
				from = to + 1;
			}

			out[partCount - 1] = pString.substring(from, pString.length());
		}

		return out;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
