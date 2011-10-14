package org.anddev.andengine.util;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:01:08 - 03.04.2010
 */
public final class StringUtils {
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

	public static final String padFront(final String pString, final char pPadChar, final int pLength) {
		final int padCount = pLength - pString.length();
		if(padCount <= 0) {
			return pString;
		} else {
			final StringBuilder sb = new StringBuilder();

			for(int i = padCount - 1; i >= 0; i--) {
				sb.append(pPadChar);
			}
			sb.append(pString);

			return sb.toString();
		}
	}

	public static final int countOccurrences(final String pString, final char pCharacter) {
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
	public static final String[] split(final String pString, final char pCharacter) {
		return StringUtils.split(pString, pCharacter, null);
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
	public static final String[] split(final String pString, final char pCharacter, final String[] pReuse) {
		final int partCount = StringUtils.countOccurrences(pString, pCharacter) + 1;

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

	public static final String formatStackTrace(final StackTraceElement pStackTraceElement) {
		return new StringBuilder()
			.append(pStackTraceElement.getClassName())
			.append('.')
			.append(pStackTraceElement.getMethodName())
			.append('(')
			.append(pStackTraceElement.getFileName())
			.append(':')
			.append(pStackTraceElement.getLineNumber())
			.append(')')
			.toString();
	}

	public static final String formatStackTrace(final StackTraceElement[] pStackTraceElements) {
		final StringBuilder sb = new StringBuilder();
		final int stackTraceElementCount = pStackTraceElements.length;
		for(int i = 0; i < stackTraceElementCount; i++) {
			sb.append(pStackTraceElements[i]);
			if(i < stackTraceElementCount - 1) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
